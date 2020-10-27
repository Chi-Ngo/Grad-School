//Chi Ngo
//cngongoc

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


public class FindMyDocUsingWS {

    //this method find doctors based on the input search parameters
    public JSONArray fetchNames(String city, String state, String zip, String specialty) {
        //this array stores all found doctors and their information
        JSONArray info = new JSONArray();
        try {
            /*
             * Create an HttpURLConnection.  This is useful for setting headers
             * and for getting the path of the resource that is returned (which
             * may be different than the URL above if redirected).
             * HttpsURLConnection (with an "s") can be used if required by the site.
             */
            //connect to the NPIRegistry API
            //find doctors based on the parameters
            //limit the number found to 8
            URL url = new URL("https://npiregistry.cms.hhs.gov/api/?taxonomy_description=" + specialty + "&city=" + city + "&state=" + state
                    + "&postal_code=" + zip + "&limit=8&skip=&version=2.1");
            //open the connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Read all the text returned by the server
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            //if the connection was made successfully
            if (code == 200) {
                //get the sent response
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String str;
                StringBuffer content = new StringBuffer();
                while ((str = in.readLine()) != null) {
                    content.append(str);
                }
                in.close();
                //create a new parser to parse the received response
                JSONParser parser = new JSONParser();
                //parse the response into a JSONObject
                JSONObject response = (JSONObject) parser.parse(content.toString());
                //get the number of doctors found
                long count = (long) response.get("result_count");
                //if a doctor was found
                if (count > 0) {
                    JSONArray data = (JSONArray) response.get("results");
                    //for each doctor in the response, get the basic information
                    for (int i = 0; i < count; i++) {
                        JSONObject obj = (JSONObject) data.get(i);
                        JSONObject doc = (JSONObject) obj.get("basic");
                        String full_name;
                        //get the name of the doctor, concatenate to create a full name string
                        if (doc.containsKey("first_name") && doc.containsKey("last_name")) {
                            full_name = doc.get("first_name") + " " + doc.get("last_name");
                        } else {
                            full_name = doc.get("authorized_official_first_name") + " " + doc.get("authorized_official_last_name");
                        }
                        //get all the addresses
                        JSONArray addresses = (JSONArray) obj.get("addresses");
                        //create a new object to store each doctor and just the information needed
                        JSONObject doctor = new JSONObject();
                        //put in the full name
                        doctor.put("full_name",full_name);
                        //get just the location address
                        JSONObject address = (JSONObject) addresses.get(0);
                        //put in the address
                        doctor.put("address_1", address.get("address_1"));
                        //if there is a second line to the address, add that too
                        if (!address.get("address_2").equals("")) {
                            doctor.put("address_2", address.get("address_2"));
                        }
                        //put in the city
                        doctor.put("city", address.get("city"));
                        //put in the state
                        doctor.put("state", address.get("state"));
                        //put in the zip
                        doctor.put("zip", address.get("postal_code"));
                        //put in the phone number
                        doctor.put("phone", address.get("telephone_number"));
                        //add the doctor to the result array
                        info.add(doctor);
                    }
                }
            }
            //close the connection
            conn.disconnect();
        }
        // handle exceptions
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //return the array of doctors
        return info;
    }
}
