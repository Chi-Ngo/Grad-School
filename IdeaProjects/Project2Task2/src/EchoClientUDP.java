// imports required for UDP/IP
import java.net.*;
import java.io.*;
import java.util.Arrays;

/**
 * This program sends 1000 requests to the server
 * Each time, i is incremented and send to the server
 * The server adds i to the sum
 * The packet is sent to the server asynchronously.
 * The program then blocks waiting for the server to perform
 * the requested operation. When the response packet arrives,
 * a String object is created and the reply is displayed.
 * The server is left running and the previous sum is not reset
 */
public class EchoClientUDP{
    public static void main(String args[]){
        EchoClientUDP client = new EchoClientUDP();
        //call method makeSocket to create a new socket
        DatagramSocket aSocket = client.makeSocket();
        System.out.println("Client Running");
        int i = 1;
        String sum = new String();
        //send 1000 requests to the server through the add method
        //i is incremented until 1000
        while (i <= 1000) {
            sum = client.add(i, aSocket);
            i += 1;
        }
        //Print the returned final sum
        System.out.println(sum);
        //call closeSocket to close the socket
        client.closeSocket(aSocket);
    }

    //this method creates a new socket
    private DatagramSocket makeSocket() {
        //make new DatagramSocket to carry the message to the server and to receive it from the server
        DatagramSocket aSocket = null;
        try {
            //create new DatagramSocket to communicate with the server, an available port is assigned randomly
            aSocket = new DatagramSocket();
        }catch (SocketException e) {System.out.println("Socket: " + e.getMessage());}
        return aSocket;
    }

    //this method closes the socket
    private void closeSocket(DatagramSocket aSocket) {
        if(aSocket != null) aSocket.close();
    }

    /**
     * This method communicates with the server through port 6789
     * It takes the number and the opened socket as arguments
     * It packaged the number and send to the server
     */
    private String add(int number, DatagramSocket aSocket) {
        try {
            //specify the IP address used. In this case, just the IP address of the localhost (this laptop)
            InetAddress aHost = InetAddress.getByName("localhost");
            //specify the port of the server
            int serverPort = 6789;
            //change the user-input number to a byte array representation
            byte [] m = String.valueOf(number).getBytes();
            //create new DatagramPacket to send requests to the server to the specified port and host
            DatagramPacket request = new DatagramPacket(m,  m.length, aHost, serverPort);
            //use the socket to send the request
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            //create a new DatagramPacket to receive the reply message from the server
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            //use the socket to receive the message
            aSocket.receive(reply);
            //return the reply as a string with all its trailing zeros truncated
            return new String(Arrays.copyOf(reply.getData(),reply.getLength()));
        }catch (IOException e){System.out.println("IO: " + e.getMessage());}
        return null;
    }
}