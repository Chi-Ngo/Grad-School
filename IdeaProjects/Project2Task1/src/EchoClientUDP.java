import java.net.*;
import java.io.*;
import java.util.Arrays;

public class EchoClientUDP{
    public static void main(String args[]){
        //args give message contents and server hostname
        //announce at start up
        System.out.println("Client Running");
        //make new DatagramSocket to carry the message to the server and to receive it from the server
        DatagramSocket aSocket = null;
        try {
            //specify the IP address used. In this case, just the IP address of the localhost (this laptop)
            InetAddress aHost = InetAddress.getByName("localhost");
            //specify the port of the server
            int serverPort = 6789;
            //create new DatagramSocket to communicate with the server, an available port is assigned randomly
            aSocket = new DatagramSocket();
            //make a string to store the user's input
            String nextLine;
            //get the input from the terminal
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            //let the client run as long as new messages are entered
            while ((nextLine = typed.readLine()) != null) {
                //change the user-input string to a byte array representation
                byte [] m = nextLine.getBytes();
                //create new DatagramPacket to send requests to the server to the specified port and host
                DatagramPacket request = new DatagramPacket(m,  m.length, aHost, serverPort);
                //use the socket to send the request
                aSocket.send(request);
                //check if the user-input request is "quit!"
                if (nextLine.equals("quit!")) {
                    //if it is, announce to the user that client is quitting
                    System.out.println("Client Quitting");
                    //break from the loop
                    break;
                }
                //create a byte array to hold reply message from server
                byte[] buffer = new byte[1000];
                //create a new DatagramPacket to receive the reply message from the server
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                //use the socket to receive the message
                aSocket.receive(reply);
                //announce the message received with a String without trailing zeros
                System.out.println("Reply: " + new String(Arrays.copyOf(reply.getData(),reply.getLength())));
            }
        //catch SocketException if the socket can't be created or accessed
        }catch (SocketException e) {System.out.println("Socket: " + e.getMessage());
        //catch IOException if there is a problem with the user's input
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        //close the socket if there's an exception or if while loop stops
        }finally {if(aSocket != null) aSocket.close();}
    }
}