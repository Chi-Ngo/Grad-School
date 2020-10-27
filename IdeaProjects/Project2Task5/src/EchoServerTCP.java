import java.math.BigInteger;
import java.net.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class EchoServerTCP {

    public static void main(String args[]) {
        EchoServerTCP server = new EchoServerTCP();
        System.out.println("Server Running");
        Socket clientSocket = null;
        Map<BigInteger,Integer> ID_Sum = new HashMap<>();
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
                    String data = in.nextLine();
                    //split on ";" to get message and signature
                    String signature = data.split(";")[1];
                    String message = data.split(";")[0];
                    BigInteger id;
                    String ops;
                    int value = 0;
                    String publicKey;
                    String[] messageInfo = message.split(",");
                    //the message contains an operand
                    if (messageInfo.length == 4) {
                        id = new BigInteger(messageInfo[0]);
                        ops = messageInfo[1];
                        value = Integer.parseInt(messageInfo[2]);
                        publicKey = messageInfo[3];
                    }
                    //if the message doesn't contain an operand
                    else {
                        id = new BigInteger(messageInfo[0]);
                        ops = messageInfo[1];
                        publicKey = messageInfo[2];
                    }
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
                    String s;
                    //if the id obtained from the message and the id created from the public key does not match
                    //or if the signature received does not match the signature created using the message and public key
                    if (!id.equals(checkID) || (server.verify(message, signature, publicKey) == false)) {
                        //do not perform the operation
                        //send back error message
                        s = "Error in request";
                    }
                    //if everything checks out
                    else {
                        int result = 0;
                        //add new id and their results to map
                        if (!ID_Sum.containsKey(id)) {
                            ID_Sum.put(id, 0);
                        }
                        //if operation is not view, update the result and set reply message as OK
                        if (!ops.equals("3")) {
                            if (ops.equals("1")) {
                                result = ID_Sum.get(id) + value;
                            } else {
                                result = ID_Sum.get(id) - value;
                            }
                            ID_Sum.put(id, result);
                            s = "OK";
                        }
                        //if operation is view, reply with the current result
                        else {
                            s = String.valueOf(ID_Sum.get(id));
                        }
                    }
                    //send back the reply
                    out.println(s);
                    out.flush();
                }
                //catch exceptions
                //if client closes, ignore the exception, server continues running
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                continue;
            }
        }
    }

    //this code is similar to BabyVerify
    //To check if the signature received matched the signature formed using the message and public key
    boolean verify(String messageToCheck, String encryptedHashStr, String publicKey) {
        BigInteger e = new BigInteger ("65537");
        BigInteger n = new BigInteger (publicKey.substring(5));
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
            return true;
        }
        else {
            return false;
        }
    }
}