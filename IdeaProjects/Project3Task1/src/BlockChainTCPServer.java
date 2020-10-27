import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This server listens to port 7777 and receive the request coming from this port
 * It then check if the message was properly signed
 * If it is, the server performs the requested operation on the blockchain
 * It then return a reply to the client in JSON format
 */

public class BlockChainTCPServer {

    public static void main (String[] args) {
        //initialize a new blockchain
        BlockChain bc = new BlockChain();
        //add in the first block
        Block genesis = new Block(0, bc.getTime(), "Genesis", 2);
        bc.addBlock(genesis);
        BlockChainTCPServer server = new BlockChainTCPServer();
        System.out.println("Server Running");
        Socket clientSocket;
        int serverPort = 7777; // the server port we are using
        // Create a new server socket
        ServerSocket listenSocket = null;
        try {
            listenSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this while is for the server to keep accepting new socket connection
        while (true) {
            try {
                /*
                 * Block waiting for a new connection request from a client.
                 * When the request is received, "accept" it, and the rest
                 * the tcp protocol handshake will then take place, making
                 * the socket ready for reading and writing.
                 */
                clientSocket = listenSocket.accept();
                // If we get here, then we are now connected to a client.

                // Set up "in" to read from the client socket
                Scanner in;
                in = new Scanner(clientSocket.getInputStream());

                // Set up "out" to write to the client socket
                PrintWriter out;
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

                /*
                 * Forever,
                 *   read a line from the socket
                 *   print it to the console
                 *   echo it (i.e. write it) back to the client
                 */
                while (true) {
                    //get the message string from the client
                    String message = in.nextLine();
                    //parse the message into the JSON object request
                    JSONParser parser = new JSONParser();
                    JSONObject request = (JSONObject) parser.parse(message);
                    //create a new JSON object for reply
                    JSONObject reply = new JSONObject();
                    //get the signature from the request
                    String signature = (String) request.get("signature");
                    //remove the signature field from the request object
                    request.remove("signature");
                    //get the client's id from the request
                    BigInteger userId = new BigInteger((String) request.get("userId"));
                    BigInteger checkId = server.findID((String) request.get("public key"));
                    //if the id created from the public key is the same as the client's id in the message
                    //and the signature verification is true
                    if (userId.equals(checkId) && server.verify(request.toString(), signature, (String) request.get("public key"))) {
                        //get the user's option
                        long choice = (Long) request.get("option");
                        //if the user choose to view information about the blockchain
                        if (choice == 0) {
                            //put the appropriate pair into reply
                            reply.put("chain size", bc.getChainSize());
                            reply.put("hashesPS", bc.hashesPerSecond());
                            reply.put("difficulty", bc.getLatestBlock().getDifficulty());
                            reply.put("nonce", bc.getLatestBlock().getNonce());
                            reply.put("chain hash", bc.recentHash);
                            //set status to OK
                            reply.put("status", "OK");
                        } else if (choice == 1) {
                            //if the user wants to add a new block
                            //get the start time of the process
                            Timestamp begin = bc.getTime();
                            //get the index for this new block
                            int index = bc.getLatestBlock().getIndex() + 1;
                            //get the difficulty of this new block
                            long difficulty = (Long) request.get("difficulty");
                            String data = (String) request.get("data");
                            //create a new block using the data obtained
                            Block newBlock = new Block(index, bc.getTime(), data, (int) difficulty);
                            //add the block to the blockchain
                            bc.addBlock(newBlock);
                            //get the end time of the process
                            Timestamp end = bc.getTime();
                            //calculate the time taken
                            long difference = end.getTime() - begin.getTime();
                            //set it to reply
                            reply.put("difference", difference);
                            //set reply status to OK
                            reply.put("status", "OK");
                        } else if (choice == 2) {
                            //if the user wants to verify the chain
                            //get the time the process begins
                            Timestamp begin = bc.getTime();
                            //check if the chain is valid
                            boolean valid = bc.isChainValid();
                            //put the found validity in reply
                            reply.put("validity", valid);
                            //get the end time of the process
                            Timestamp end = bc.getTime();
                            //find the total time taken to verify the chain and put it in reply
                            long difference = end.getTime() - begin.getTime();
                            reply.put("difference", difference);
                            //if the chain is not valid
                            if (!valid) {
                                //get the information related to the corrupted block and put them in reply
                                String[] info = bc.invalidInfo();
                                reply.put("index", info[0]);
                                reply.put("zeros", info[1]);
                            }
                            //set status to OK
                            reply.put("status", "OK");
                        } else if (choice == 3) {
                            //if the user wants to see the whole chain
                            //reply now holds the chain in JSON string format
                            reply.put("block chain", bc.toString());
                            //set status to OK
                            reply.put("status", "OK");
                        } else if (choice == 4) {
                            //if the user wants to corrupt the chain
                            //get the id of the block to corrupt
                            long id = (Long) request.get("id");
                            //get the new data for the block
                            String data = (String) request.get("data");
                            bc.blocks.get((int) id).setData(data);
                            //put the new data in reply
                            reply.put("id", id);
                            reply.put("data", data);
                            //set status to OK
                            reply.put("status", "OK");
                        } else if (choice == 5) {
                            //if the user wants to repair the entire chain
                            //get the start time of the process
                            Timestamp begin = bc.getTime();
                            //repair the chain
                            bc.repairChain();
                            //get the end time when the repair process is done
                            Timestamp end = bc.getTime();
                            //find the time taken to repair the chain
                            long difference = end.getTime() - begin.getTime();
                            //send that difference in reply
                            reply.put("difference", difference);
                            //set status to OK
                            reply.put("status", "OK");
                        }
                    } else {
                        //set status to the error message if signature is not valid or the ids are not the same
                        reply.put("status", "Message is invalid");
                    }
                    //send the reply to the client by converting the JSON object to a String
                    out.println(reply.toString());
                    out.flush();
                }
                //catch exceptions
                //if client closes, ignore the exception, server continues running
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                continue;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    //this code is similar to BabyVerify
    //To check if the signature received matched the signature formed using the message and public key
    boolean verify(String messageToCheck, String encryptedHashStr, String publicKey) {
        BigInteger e = new BigInteger (publicKey.split(",")[0]);
        BigInteger n = new BigInteger (publicKey.split(",")[1]);
        // Take the encrypted string and make it a big integer
        BigInteger encryptedHash = new BigInteger(encryptedHashStr);
        // Decrypt it
        BigInteger decryptedHash = encryptedHash.modPow(e, n);

        // Get the bytes from messageToCheck
        byte[] bytesOfMessageToCheck = new byte[0];
        try {
            bytesOfMessageToCheck = messageToCheck.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        // compute the digest of the message with SHA-256
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        byte[] messageToCheckDigest = md.digest(bytesOfMessageToCheck);

        // Make it a big int
        BigInteger bigIntegerToCheck = new BigInteger(1, messageToCheckDigest);
        // inform the client on how the two compare
        if(bigIntegerToCheck.compareTo(decryptedHash) == 0) {
            System.out.println(decryptedHash);
            return true;
        } else {
            return false;
        }
    }

    //this method computes the client ID associated with the public key sent in the message
    BigInteger findID(String publicKey) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //get the hash of the public key and subsequently, its last 20 bytes
        md.update(publicKey.getBytes());
        byte[] pubKey = md.digest();
        byte[] key20 = Arrays.copyOfRange(pubKey, pubKey.length - 20, pubKey.length);
        //create BigInteger representation of these last 20 bytes
        BigInteger checkID = new BigInteger(key20);
        return checkID;
    }
}
