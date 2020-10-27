//import required for UDP/IP
import java.net.*;
import java.io.*;
import java.util.Arrays;

/**
 * The user input contains a single message
 * The input is packaged and placed in a single UDP packet.
 * The packet is sent to the server asynchronously.
 * The program then blocks waiting for the server to perform
 * the requested operation. When the response packet arrives,
 * a String object is created and the reply is displayed.
*/

public class EchoServerUDP{
    public static void main(String args[]){
        //announce at start up
        System.out.println("Server Running");
        //make new DatagramSocket to receive the message from the client and to send a reply to the client
        DatagramSocket aSocket = null;
        //create a byte array to hold request message from client
        byte[] buffer = new byte[1000];
        try{
            //this socket will be at port 6789
            aSocket = new DatagramSocket(6789);
            //make a new DatagramPacket to store requests from the client with the buffer as placeholder
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            //server running forever if nothing breaks it
            while(true) {
                //socket receives request from client, put into request
                aSocket.receive(request);
                //convert the request byte array to one without trailing zeros
                byte[] corrected = Arrays.copyOf(request.getData(),request.getLength());
                //create a new DatagramPacket to send replies to the client using the sent request's information
                DatagramPacket reply = new DatagramPacket(request.getData(),
                        request.getLength(), request.getAddress(), request.getPort());
                //make a String representation from the byte-array of the request message
                String requestString = new String(corrected);
                //if this string is "quit!"
                if (requestString.equals("quit!")) {
                    //announce to the terminal
                    System.out.println("Server Quitting");
                    //break the loop
                    break;
                }
                //print out the exact request message (the user-input value from the client side)
                System.out.println("Echoing: "+requestString);
                //send the reply to the client through the socket
                aSocket.send(reply);
            }
        //catch SocketException if the socket can't be created or accessed
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        //catch IOException if there is a problem with the user's input
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        //close the socket if there's an exception or if while loop stops
        }finally {if(aSocket != null) aSocket.close();}
    }
}