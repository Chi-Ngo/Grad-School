import java.math.BigInteger;
import java.net.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

/**
 * Every time the client starts, a unique id is assigned to the user
 * This program displays a menu to the user
 * The user can choose the operation they want
 * The user id, operation, and value are then sent to the server using TCP
 * The server will then compute the appropriate operation and return the status
 */

public class EchoClientTCP {
    public static void main(String args[]) {
        System.out.println("Client Running");
        EchoClientTCP client = new EchoClientTCP();
        //make new public and private keys for the client
        BigInteger[][] keys = client.makeRSAKeys();
        //display those keys
        System.out.println("The public key is: " + Arrays.toString(keys[0]));
        System.out.println("The private key is: " + Arrays.toString(keys[1]));

        //open new socket
        Socket clientSocket = client.makeSocket();
        //concatenate e and n to create the public key
        String publicKey = String.valueOf(keys[0][0]) + String.valueOf(keys[0][1]);
        //hash the key with SHA-256
        byte[] pubKey = client.makeHash(publicKey);
        //get the least significant 20 bytes
        byte[] key20 = Arrays.copyOfRange(pubKey, pubKey.length-20, pubKey.length);
        //the id is the BigInteger representation of these 20 bytes
        BigInteger id = new BigInteger(key20);
        System.out.println("Here are the operations. Pick one:");
        System.out.println("1. add");
        System.out.println("2. subtract");
        System.out.println("3. view");
        System.out.println("4. quit");
        System.out.print("Enter your choice number: ");
        try {
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            String ops;
            while ((ops = typed.readLine()) != null) {
                //if the user chooses quit, break the loop and close the socket
                if (ops.equals("4")) {
                    break;
                }
                String data;
                //if operation is not view, ask the user for input value
                //create data string using "," as separator
                if (!ops.equals("3")) {
                    System.out.print("Enter a value: ");
                    String value = typed.readLine();
                    data = id + "," + ops + "," + value + "," + publicKey;
                }
                //if operation is view
                //create data string without operand
                else {
                    data = id + "," + ops + "," + publicKey;
                }
                //hash the message with SHA-256
                byte[] message = client.makeHash(data);
                //make sure the BigInteger representation of the hash is positive
                BigInteger m = new BigInteger(1, message);
                // encrypt the digest with the private key
                BigInteger signature = m.modPow(keys[1][0], keys[1][1]);
                //create the complete message with the data and signature
                String signedData = data + ";" + signature;
                System.out.println ("Received: " + client.doThings(signedData,clientSocket));
                System.out.println("Here are the operations. Pick one:");
                System.out.println("1. add");
                System.out.println("2. subtract");
                System.out.println("3. view");
                System.out.println("4. quit");
                System.out.print("Enter your choice number: ");
            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        }
        System.out.println("Client Quitting");
        //close the socket when user chooses quit
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
    String doThings(String value, Socket clientSocket) {
        String data = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
            //send value to the server
            out.println(value);
            out.flush();
            //receive the reply from the server
            data = in.readLine(); // read a line of data from the stream
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
