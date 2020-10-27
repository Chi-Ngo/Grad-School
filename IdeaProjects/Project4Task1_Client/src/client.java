// ****************  Client side code  *********************

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;


public class client {


    public static void main(String[] args) throws Exception {

        String city = "";
        String state = "PA";
        String zip = "15217";
        String specialty = "dermatology";
        long timestamp = System.currentTimeMillis();
        if (!city.equals("")) {
            String[] arr = city.split(" ");
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < arr.length; i++) {
                sb.append(Character.toUpperCase(arr[i].charAt(0)))
                        .append(arr[i].substring(1)).append(" ");
            }
            city = sb.toString().trim();
        }

       // System.out.println(city);
        Timestamp time = new Timestamp(System.currentTimeMillis());
        //System.out.println(time);
        //System.out.println(doGet(city, state, zip, specialty));
        //System.out.println(doPost(timestamp, specialty, city, state, zip));

    }

    //this method is used to test task 2
    public static int doPost(long timestamp, String specialty, String city, String state, String zip) {
        HttpURLConnection conn;
        int status = 0;
        try {
            specialty = specialty.replaceAll(" ", "+");
            city = city.replaceAll(" ","+");
            // pass the name on the URL line
            URL url = new URL("http://localhost:8080/Project4Task2/?city=" + city + "&state=" + state + "&zip=" + zip + "&specialty=" + specialty + "&timestamp=" + timestamp);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "text/plain");

            // wait for response
            status = conn.getResponseCode();

            // If things went poorly, don't try to read any response, just return.
            if (status != 200) {
                // not using msg
                String msg = conn.getResponseMessage();
                return conn.getResponseCode();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    //this method is used to test task 1
    public static int doGet(String city, String state, String zip, String specialty) {

        // Make an HTTP GET passing the name on the URL line

        String response = "";
        HttpURLConnection conn;
        int status = 0;

        try {
            city = city.replaceAll(" ","+");
            // pass the name on the URL line
            URL url = new URL("http://localhost:8080/Project4Task1/?city=" + city + "&state=" + state + "&zip=" + zip + "&specialty=" + specialty);
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // tell the server what format we want back
            conn.setRequestProperty("Accept", "text/plain");

            // wait for response
            status = conn.getResponseCode();

            // If things went poorly, don't try to read any response, just return.
            if (status != 200) {
                // not using msg
                String msg = conn.getResponseMessage();
                return conn.getResponseCode();
            }
            String output = "";
            // things went well so let's read the response
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((output = br.readLine()) != null) {
                response += output;

            }
            JSONParser p = new JSONParser();
            JSONObject reply = (JSONObject) p.parse(response);
            JSONArray info = (JSONArray) reply.get("doctors");
            for (int i = 0; i < info.size(); i++) {
                JSONObject doc = (JSONObject) info.get(i);
                System.out.println(doc.get("full_name"));
                System.out.println(doc.get("address_1"));
                if (doc.containsKey("address_2")) {
                    System.out.println(doc.get("address_2"));
                }
                System.out.println(doc.get("city") + ", " + doc.get("state") + " " + doc.get("zip"));
                System.out.println(doc.get("phone"));
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // return value from server
        // set the response object
        // return HTTP status to caller
        return status;
    }

}