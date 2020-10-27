import java.net.*;
import java.io.*;
import java.util.Arrays;

public class EchoServerUDP{
    public static void main(String args[]){
        //announce at start up
        System.out.println("Server Running");
        //make new DatagramSocket to receive the message from the client and to send a reply to the client
        DatagramSocket aSocket = null;
        //create a byte array to hold request message from client
        byte[] buffer = new byte[1000];
        int sum = 0;
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
                //make a String representation from the byte-array of the request message
                String requestString = new String(corrected);
                //print out the exact request message (the user-input value from the client side)
                try {
                    //add the sent value to sum
                    sum += Integer.parseInt(requestString);
                    //print out partial sum
                    System.out.println("Echoing: "+sum);
                    String s = String.valueOf(sum);
                    //create a new DatagramPacket to send replies to the client using the sent request's information
                    DatagramPacket reply = new DatagramPacket(s.getBytes(),
                            s.length(), request.getAddress(), request.getPort());
                    //send the reply to the client through the socket
                    aSocket.send(reply);
                    //if the client closes and does not send any new info
                    //catch the exception and let the server continue running
                } catch (NumberFormatException e) {
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //catch SocketException if the socket can't be created or accessed
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
            //catch IOException if there is a problem with the user's input
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
            //close the socket if there's an exception or if while loop stops
        }finally {if(aSocket != null) aSocket.close();}
    }
}