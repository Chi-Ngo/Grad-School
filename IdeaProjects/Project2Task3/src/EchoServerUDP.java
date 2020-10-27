import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EchoServerUDP{
    public static void main(String args[]){
        //announce at start up
        System.out.println("Server Running");
        //make new DatagramSocket to receive the message from the client and to send a reply to the client
        DatagramSocket aSocket = null;
        //create a byte array to hold request message from client
        byte[] buffer = new byte[1000];
        //map to store id and their sums
        Map<Integer,Integer> ID_Sum = new HashMap<>();
        int result = 0;
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
                //parse the requestString with "," to get the id, operation, and value respectively
                int id = Integer.parseInt(requestString.split(",")[0]);
                //if the user id doesn't already exist in the map, put it in with sum = 0
                if (!ID_Sum.containsKey(id)) {
                    ID_Sum.put(id,0);
                }
                String ops = requestString.split(",")[1];
                //if operation is not 3, send back OK
                //update the respective id with its new result
                String s;
                if (!ops.equals("3")) {
                    int value = Integer.parseInt(requestString.split(",")[2]);
                    if (ops.equals("1")) {
                        result = ID_Sum.get(id) + value;
                    }
                    else {
                        result = ID_Sum.get(id) - value;
                    }
                    ID_Sum.put(id,result);
                    s = "OK";
                }
                //if operation is 3, send back the current result of that id
                else {
                    s = String.valueOf(ID_Sum.get(id));
                }
                try{
                    //create a new DatagramPacket to send replies to the client using the sent request's information
                    DatagramPacket reply = new DatagramPacket(s.getBytes(),
                            s.length(), request.getAddress(), request.getPort());
                    //send the reply to the client through the socket
                    aSocket.send(reply);
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