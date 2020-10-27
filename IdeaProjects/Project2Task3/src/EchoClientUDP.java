import java.net.*;
import java.io.*;
import java.util.Arrays;

/**
 * This program displays a menu to the user
 * The user can choose the operation they want
 * The user id, operation, and value are then packaged and sent to the server
 * The packet is sent to the server asynchronously.
 * The program then blocks waiting for the server to perform
 * the requested operation. When the response packet arrives,
 * a String object is created and the reply is displayed.
 */

public class EchoClientUDP{
    public static void main(String args[]){
        EchoClientUDP client = new EchoClientUDP();
        //create a new socket
        DatagramSocket aSocket = client.makeSocket();
        //announce at start up
        System.out.println("Client Running");
        String result = "";
        String id;
        //get the input from the terminal
        BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
        //let the client run as long as new messages are entered
        System.out.print("Enter your ID: ");
        try {
            while ((id = typed.readLine()) != null || result != null) {
                //menu for the user
                System.out.println("Here are the operations. Pick one:");
                System.out.println("1. add");
                System.out.println("2. subtract");
                System.out.println("3. view");
                System.out.println("4. quit");
                System.out.print("Enter your choice number: ");
                String ops = typed.readLine();
                //if the user chooses to quit, break the loop and close the socket
                if (ops.equals("4")) {
                    break;
                }
                String data;
                //if the user chooses add or subtract, ask for value
                if (!ops.equals("3")) {
                    System.out.print("Enter a value: ");
                    String value = typed.readLine();
                    //create a data string using "," as separator
                    data = id + "," + ops + "," + value;
                }
                else {
                    //create a data string using "," as separator but without operand
                    data = id + "," + ops;
                }
                //doThings method will communicate with server
                result = client.doThings(data, aSocket);
                System.out.println("Received: " + result);
                System.out.print("Enter your ID: ");
            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        }
        //announce when the client is quitting
        System.out.println("Client Quitting");
        //close the socket
        client.closeSocket(aSocket);
    }

    //this method is used to open a new socket as a new client starts up
    DatagramSocket makeSocket() {
        //make new DatagramSocket to carry the message to the server and to receive it from the server
        DatagramSocket aSocket = null;
        try {
            //create new DatagramSocket to communicate with the server, an available port is assigned randomly
            aSocket = new DatagramSocket();
        }catch (SocketException e) {System.out.println("Socket: " + e.getMessage());}
        return aSocket;
    }

    //this method closes the socket
    void closeSocket(DatagramSocket aSocket) {
        if(aSocket != null) aSocket.close();
    }

    /**
     * This method communicates with the server through port 6789
     * It takes the data string and the opened socket as arguments
     * It packaged the string and send to the server
     */
    String doThings(String data, DatagramSocket aSocket) {
        try {
            //specify the IP address used. In this case, just the IP address of the localhost (this laptop)
            InetAddress aHost = InetAddress.getByName("localhost");
            //specify the port of the server
            int serverPort = 6789;
            //change the user-input string to a byte array representation
            byte [] m = data.getBytes();
            //create new DatagramPacket to send requests to the server to the specified port and host
            DatagramPacket request = new DatagramPacket(m,  m.length, aHost, serverPort);
            //use the socket to send the request
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            //create a new DatagramPacket to receive the reply message from the server
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            //use the socket to receive the message
            aSocket.receive(reply);
            //return to main the reply without trailing zeros
            return new String(Arrays.copyOf(reply.getData(),reply.getLength()));
        }catch (IOException e){System.out.println("IO: " + e.getMessage());}
        return null;
    }
}