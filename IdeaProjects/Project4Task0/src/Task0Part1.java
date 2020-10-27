import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Task0Part1 {

    public static void main(String[] args) {

        System.out.println("Find some dermatologists in Pittsburgh, PA near zip code 15217");
        doGet();
    }

    public static void doGet() {
        try {
            // Make call to a particular URL
            URL url = new URL("https://npiregistry.cms.hhs.gov/api/?number=&enumeration_type=&taxonomy_description=primary+care&first_name=&use_first_name_alias=&last_name=&organization_name=&address_purpose=&city=pittsburgh&state=pa&postal_code=&country_code=&limit=10&skip=&version=2.1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if (code == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String str;
                StringBuffer content = new StringBuffer();
                while ((str = in.readLine()) != null) {
                    content.append(str);
                }
                in.close();
                JSONParser parser = new JSONParser();
                JSONObject response = (JSONObject) parser.parse(content.toString());
                System.out.println("Total number of records: " + response.get("result_count"));
                JSONArray data = (JSONArray) response.get("results");
                for (int i = 0; i < data.size(); i++) {
                    JSONObject obj = (JSONObject) data.get(i);
                    JSONObject doc = (JSONObject) obj.get("basic");
                    System.out.println((String) doc.get("name_prefix") + doc.get("first_name") + " " + doc.get("last_name"));
                    JSONArray addresses = (JSONArray) obj.get("addresses");
                    for (int j = 0; j < addresses.size(); j++) {
                        JSONObject address = (JSONObject) addresses.get(j);
                        System.out.println(address.get("address_purpose"));
                        System.out.println(address.get("address_1") + ", " + address.get("address_2"));
                        System.out.println(address.get("city") + ", " + address.get("state") + " " + address.get("postal_code"));
                        System.out.println(address.get("telephone_number"));
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
    }
}
