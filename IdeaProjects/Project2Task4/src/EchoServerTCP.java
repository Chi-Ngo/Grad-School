import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServerTCP {
    public static void main(String args[]) {
        System.out.println("Server Running");
        Socket clientSocket;
        //map to store id and their respective sum
        Map<Integer,Integer> ID_Sum = new HashMap<>();
        int serverPort = 7777; // the server port we are using

        // Create a new server socket
        ServerSocket listenSocket = null;
        try {
            listenSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //put everything inside a while loop so the server accepts for new socket connection
        while (true) {
            try {
                /*
                 * Block waiting for a new connection request from a client.
                 * When the request is received, "accept" it, and the rest
                 * the tcp protocol handshake will then take place, making
                 * the socket ready for reading and writing.
                 */
                clientSocket = listenSocket.accept();

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
                    //split the data according to the separator "," to get id, operation, and value
                    int id = Integer.parseInt(data.split(",")[0]);
                    //if the user id hasn't been used before, add it to map, set sum to 0
                    if (!ID_Sum.containsKey(id)) {
                        ID_Sum.put(id, 0);
                    }
                    String s;
                    int result = 0;
                    String ops = data.split(",")[1];
                    //if operation is not view, perform the appropriate add/subtract operation
                    if (!ops.equals("3")) {
                        int value = Integer.parseInt(data.split(",")[2]);
                        if (ops.equals("1")) {
                            result = ID_Sum.get(id) + value;
                            //System.out.println("Echoing: " + result);
                        } else {
                            result = ID_Sum.get(id) - value;
                            //System.out.println("Echoing: " + result);
                        }
                        //put new result into map, message is set to OK
                        ID_Sum.put(id, result);
                        s = "OK";
                    } else {
                        //if the operation is view, reply message is set to the current value of that id
                        s = String.valueOf(ID_Sum.get(id));
                    }
                    //send message back to client
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
}