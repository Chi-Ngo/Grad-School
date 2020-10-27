import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

/**
 * This program displays a menu to the user
 * A public key and a private key will be generated
 * The user can choose the operation they want with the blockchain
 * The user operation and related values will be signed using the private key
 * The user's signed message are then sent to the server using TCP
 * The server will then compute the appropriate operation and return the status
 * The time taken to compute proof of work increases with difficulty
 * While it takes approximately the same time for difficulty 2,3 and 4
 * It takes longer for difficulty 5, and exponentially longer for difficulty 6 and above
 */

public class BlockChainTCPClient {
    //main method of the program
    //the average time to add a block of difficulty 4 is 329 milliseconds
    //the average time to add a block of difficulty 5 is 1534 milliseconds
    //the average time to verify a chain with blocks of difficulty 4 is 0 milliseconds
    //the average time to verify a chain with blocks of difficulty 5 is 1 milliseconds
    //the average time to repair a chain with blocks of difficulty 4 is 124 milliseconds
    //the average time to repair a chain of blocks of difficulty 5 is 2354 milliseconds
    public static void main(String[] args) {
        System.out.println("Client Running");
        BlockChainTCPClient client = new BlockChainTCPClient();
        //make new public and private keys for the client
        BigInteger[][] keys = client.makeRSAKeys();
        //open new socket
        Socket clientSocket = client.makeSocket();
        //concatenate e and n to create the public key
        String publicKey = String.valueOf(keys[0][0]) + "," + String.valueOf(keys[0][1]);
        //hash the key with SHA-256
        byte[] pubKey = client.makeHash(publicKey);
        //get the least significant 20 bytes
        byte[] key20 = Arrays.copyOfRange(pubKey, pubKey.length-20, pubKey.length);
        //the id is the BigInteger representation of these 20 bytes
        BigInteger userId = new BigInteger(key20);
        BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
        //this value holds the menu option input by the user
        int input;
        try {
            //keep doing this loop as long as user doesn't choose 6 (exit)
            do {
                //request is in the form of a JSON object
                //different user inputs will be put to the appropriate key-value pair
                JSONObject request = new JSONObject();
                //send the client's id in the request
                request.put("userId", userId.toString());
                //display the menu with choices
                System.out.println("0. View basic blockchain status.\n" +
                        "1. Add a transaction to the blockchain.\n" +
                        "2. Verify the blockchain.\n" +
                        "3. View the blockchain.\n" +
                        "4. Corrupt the chain.\n" +
                        "5. Hide the corruption by repairing the chain.\n" +
                        "6. Exit.");
                input = Integer.parseInt(typed.readLine());
                request.put("option", input);
                if (input == 1) {
                    //if user chooses option 1
                    System.out.println("Enter difficulty > 0");
                    //prompt for difficulty of the new block
                    int difficulty = Integer.parseInt(typed.readLine());
                    request.put("difficulty", difficulty);
                    //prompt for the transaction
                    System.out.println("Enter transaction");
                    String data = typed.readLine();
                    request.put("data", data);
                } else if (input == 2) {
                    //inform the user that the chain is being verified
                    System.out.println("Verifying entire chain");
                } else if (input == 4) {
                    //inform the user that the chain will be corrupted
                    System.out.println("Corrupt the Blockchain");
                    //prompt the user for the block to corrupt
                    System.out.print("Enter block ID of block to Corrupt ");
                    int id = Integer.parseInt(typed.readLine());
                    request.put("id", id);
                    //prompt the user for the new data
                    System.out.println("Enter new data for block " + id);
                    String data = typed.readLine();
                    request.put("data", data);
                } else if (input == 5) {
                    //inform the user that the chain is being repaired
                    System.out.println("Repairing the entire chain");
                }
                //if input isn't to quit
                if (input != 6) {
                    //add the public key to the message to the server
                    request.put("public key", publicKey);
                    //hash the message with SHA-256
                    byte[] hashIt = client.makeHash(request.toString());
                    //make sure the BigInteger representation of the hash is positive
                    BigInteger m = new BigInteger(1, hashIt);
                    //encrypt the digest with the private key to create a signature
                    BigInteger signature = m.modPow(keys[1][0], keys[1][1]);
                    //add the signature to the message
                    request.put("signature", String.valueOf(signature));
                    //pass the request to doThings method to interact with the server through the socket
                    String message = client.doThings(request, clientSocket);
                    //print the result according to the option chosen by the user and the reply from the server
                    client.printResult(input, message);
                } //run the loop while the user doesn't choose 6
            } while (input != 6);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //close the socket after program is done
        client.closeSocket(clientSocket);
    }

    //make new socket at port 7777
    Socket makeSocket() {
        Socket clientSocket = null;
        int serverPort = 7777;
        try {
            clientSocket = new Socket("localhost", serverPort);
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        }
        return clientSocket;
    }

    //close the socket if it is opened
    void closeSocket(Socket clientSocket) {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            // ignore exception on close
        }
    }

    //this method communicates with the server
    String doThings(JSONObject value, Socket clientSocket) {
        String data = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
            //send value to the server
            out.println(value.toString());
            out.flush();
            //receive the reply from the server
            data = in.readLine(); // read a line of data from the stream
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return the reply to the main method
        return data;
    }

    //RSA code copied from project website
    //this program returns the private and public as pair of d and n and pair of e and n respectively
    //keys[0][0] is e, keys[0][1] is n
    //keys[1][0] is d, keys[1][1] is n
    BigInteger[][] makeRSAKeys() {
        // Each public and private key consists of an exponent and a modulus
        BigInteger n; // n is the modulus for both the private and public keys
        BigInteger e; // e is the exponent of the public key
        BigInteger d; // d is the exponent of the private key

        Random rnd = new Random();

        // Step 1: Generate two large random primes.
        // We use 400 bits here, but best practice for security is 2048 bits.
        // Change 400 to 2048, recompile, and run the program again and you will
        // notice it takes much longer to do the math with that many bits.
        BigInteger p = new BigInteger(400,100,rnd);
        BigInteger q = new BigInteger(400,100,rnd);

        // Step 2: Compute n by the equation n = p * q.
        n = p.multiply(q);

        // Step 3: Compute phi(n) = (p-1) * (q-1)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Step 4: Select a small odd integer e that is relatively prime to phi(n).
        // By convention the prime 65537 is used as the public exponent.
        e = new BigInteger ("65537");

        // Step 5: Compute d as the multiplicative inverse of e modulo phi(n).
        d = e.modInverse(phi);
        BigInteger[][] keys = new BigInteger[2][];
        keys[0] = new BigInteger[]{e, n}; //store e and n in keys[0]
        keys[1] = new BigInteger[]{d,n}; //store d and n in keys[1]
        return keys;
    }

    //method to make a SHA-256 hash of the key
    byte[] makeHash(String publicKey) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(publicKey.getBytes());
        return md.digest();
    }

    //this method prints the appropriate result to the user
    void printResult(int input, String message) {
        JSONParser parser = new JSONParser();
        //the reply will be parsed into a JSON object
        JSONObject reply = null;
        try {
            //parse the reply string to a JSON object
            reply = (JSONObject) parser.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //if there is nothing wrong with request and the server carries it out successfully
        if (reply.get("status").equals("OK")) {
            //if the user wants the general information of the blockchain
            if (input == 0) {
                //print out the blockchain information
                //the values are stored in the JSON object reply
                System.out.println("Current size of chain: " + reply.get("chain size"));
                System.out.println("Current hashes per second by this machine: " + reply.get("hashesPS"));
                System.out.println("Difficulty of most recent block: " + reply.get("difficulty"));
                System.out.println("Nonce for most recent block: " + reply.get("nonce"));
                System.out.println("Chain hash: " + reply.get("chain hash"));
            } else if (input == 1) {
                //if the user wants to add a new block
                //print out the time it took to add the new block
                System.out.println("Total execution time to add this block was " + reply.get("difference") + " milliseconds");
            } else if (input == 2) {
                //if the user wants to verify the block
                //print the status of the verification
                boolean valid = (boolean) reply.get("validity");
                System.out.println("Chain verification: " + valid);
                //if chain is not valid
                if (!valid) {
                    //inform the user of where the improper hash happens
                    System.out.println("..Improper hash on node " + reply.get("index") + ". Does not begin with " + reply.get("zeros"));
                }
                //print out the time it took to verify the entire chain
                System.out.println("Total execution time required to verify the chain was " + reply.get("difference") + " milliseconds");
            } else if (input == 3) {
                //if the user wants to see the chain
                //print the chain in JSON format
                System.out.println(reply.get("block chain"));
            } else if (input == 4) {
                //if the user wants to corrupt the chain
                //after the chain is successfully corrupted, inform the user
                System.out.println("Block " + reply.get("id") + " now holds " + reply.get("data"));
            } else if (input == 5) {
                //if the user wants to repair the chain
                //print out the time it took for reparation
                System.out.println("Total execution time required to repair the chain was " + reply.get("difference") + " milliseconds");
            }
        } else {
            //if there is an error with the request
            //display the error message to the user
            System.out.println(reply.get("status"));
        }
    }
}
