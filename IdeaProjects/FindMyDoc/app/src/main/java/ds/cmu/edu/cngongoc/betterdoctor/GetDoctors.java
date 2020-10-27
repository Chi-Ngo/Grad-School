//Chi Ngo
//cngongoc
package ds.cmu.edu.cngongoc.betterdoctor;

import android.os.AsyncTask;

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
import java.util.HashMap;

public class GetDoctors {
    MainActivity ma = null;

    /*
     * search is the public GetDoctors method.  Its arguments are the search terms, and the MainActivity object that called it.  This provides a callback
     * path such that the doctorsReady method in that object is called when the picture is available from the search.
     */
    public void search(String city, String state, String zip, String specialty, MainActivity ma) {
        this.ma = ma;
        new AsyncDoctorsSearch().execute(city, state, zip,specialty);
    }

    //the thread to search for doctors asynchronously
    private class AsyncDoctorsSearch extends AsyncTask<String,Void, HashMap<String, JSONObject>> {

        @Override
        protected HashMap<String, JSONObject> doInBackground(String... strings) {
            return search (strings[0], strings[1], strings[2], strings[3]);
        }

        //when the task is done, pass the result to MainActivity
        protected void onPostExecute(HashMap<String, JSONObject> results) {
            ma.doctorsReady(results);
        }

        //method to connect with the server to send the search terms
        private HashMap<String, JSONObject> search (String city, String state, String zip, String specialty) {

            //results are put in a hashmap with doctors' names as keys and their information as values
            HashMap<String,JSONObject> results = new HashMap<>();
            Timestamp timestamp = null;
            int number = 0;
            int code = 0;
            try {
                //timestamp of when the request is sent
                timestamp = new Timestamp(System.currentTimeMillis());
                //replace space in strings with +
                specialty = specialty.replaceAll(" ", "+");
                city = city.replaceAll(" ", "+");
                // Make call to a particular server URL
                URL url = new URL("https://chi-ngo-coolio-task2.herokuapp.com/?city=" + city + "&state=" + state
                        + "&zip=" + zip + "&specialty=" + specialty);
                //use this URL if task 2 fails
                //URL url = new URL("https:chi-ngo-coolio.herokuapp.com/?city=" + city + "&state=" + state + "&zip=" + zip + "&specialty=" + specialty);
                /*
                 * Create an HttpURLConnection.  This is useful for setting headers
                 * and for getting the path of the resource that is returned (which
                 * may be different than the URL above if redirected).
                 * HttpsURLConnection (with an "s") can be used if required by the site.
                 */
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                code = conn.getResponseCode();
                //if the connection was successful
                if (code == 200) {
                    //read all the response
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String str;
                    StringBuffer content = new StringBuffer();
                    while ((str = in.readLine()) != null) {
                        content.append(str);
                    }
                    in.close();
                    //make a new parser to parse the response string into a JSONObject
                    JSONParser p = new JSONParser();
                    JSONObject response = (JSONObject) p.parse(content.toString());
                    System.out.println(response.toJSONString());
                    JSONArray doctors = (JSONArray) response.get("doctors");
                    //add each doctor and their information to the hashmap with their full names as keys
                    for (int i = 0; i < doctors.size(); i++) {
                        JSONObject info = (JSONObject) doctors.get(i);
                        results.put((String) info.get("full_name"),info);
                    }
                    //get the number of doctors found
                    number = doctors.size();
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
            //send a POST request for logging data
            try {
                //convert the timestamp object to a string
                String time = timestamp.toString();
                //replace all spaces with +
                time = time.replaceAll(" ", "+");
                /*
                 * Create an HttpURLConnection.  This is useful for setting headers
                 * and for getting the path of the resource that is returned (which
                 * may be different than the URL above if redirected).
                 * HttpsURLConnection (with an "s") can be used if required by the site.
                 */
                //connect to the server URL with a POST request for data logging to MongoDB
                URL url2 = new URL("https://chi-ngo-coolio-task2.herokuapp.com/?city=" + city + "&state=" + state + "&zip="
                        + zip + "&specialty=" + specialty + "&timestamp=" + time + "&number=" + number + "&code=" + code);
                HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "text/plain");
                connection.getResponseCode();
                //close the connection
                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //return the result hashmap
            return results;
        }
    }
}
