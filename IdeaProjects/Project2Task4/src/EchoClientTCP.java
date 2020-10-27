import java.net.*;
import java.io.*;

/**
 * This program displays a menu to the user
 * The user can choose the operation they want
 * The user id, operation, and value are then sent to the server using TCP
 * The server will then compute the appropriate operation and return the status
 */

public class EchoClientTCP {
    public static void main(String args[]) {
        System.out.println("Client Running");
        EchoClientTCP client = new EchoClientTCP();
        //make new socket using the method makeSocket()
        Socket clientSocket = client.makeSocket();
        System.out.print("Enter your ID: ");
        try {
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            String id;
            //display the menu in a loop
            //this loop keeps running as long as user still enters new information and does not quit
            while ((id = typed.readLine()) != null) {
                System.out.println("Here are the operations. Pick one:");
                System.out.println("1. add");
                System.out.println("2. subtract");
                System.out.println("3. view");
                System.out.println("4. quit");
                System.out.print("Enter your choice number: ");
                String ops = typed.readLine();
                //if the client chooses quit, break the loop and close the socket
                if (ops.equals("4")) {
                    break;
                }
                String data;
                //if the operation is not view, ask for value
                if (!ops.equals("3")) {
                    System.out.print("Enter a value: ");
                    String value = typed.readLine();
                    //create a message using "," as separator
                    data = id + "," + ops + "," + value;
                }
                else {
                    //create a message using "," as separator but without operand
                    data = id + "," + ops;
                }
                //send the message and receive the reply through the doThings method
                System.out.println ("Received: " + client.doThings(data,clientSocket));
                System.out.print("Enter your ID: ");
            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        }
        System.out.println("Client Quitting");
        //close the socket after the client is done
        client.closeSocket(clientSocket);
    }

    //make a new socket at port 7777
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

    //closeSocket if it's not null (i.e. has been opened)
    void closeSocket(Socket clientSocket) {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            // ignore exception on close
        }
    }

    //this method handles communication with the server
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
}