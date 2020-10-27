//Chi Ngo
//cngongoc
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

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
import java.util.*;


public class FindMyDocModel {
    //establish mongoDB connection
    //code snippet from MongoDB Atlas
    MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://cngongoc:dsproject4@cluster0-sztnn.mongodb.net/test?retryWrites=true&w=majority");
    MongoClient mongoClient = new MongoClient(uri);
    MongoDatabase database = mongoClient.getDatabase("Project4");
    MongoCollection<Document> collection = database.getCollection("Task2");

    //make a new document and insert it to the collection
    void log(String specialty, String city, String state, String zip, String timestamp,
             String number, String code, String device) {
        Document doc = new Document("timestamp", timestamp);
        doc.append("specialty",specialty);
        doc.append("city", city);
        doc.append("state",state);
        doc.append("zip", zip);
        doc.append("number", number);
        doc.append("code",code);
        doc.append("device",device);
        collection.insertOne(doc);
    }

    //read all documents from the collection
    List<Document> read() {
        List<Document> result = new ArrayList<>();
        //use cursor to move through each document
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        //store all bson document in a list
        return result;
    }

    //analyzing the documents
    Map<String,String[]> analytics() {
        //analytics information is top 3 most searched specialties
        //top 3 most searched cities
        //top 3 most searched states
        //the information is stored in map of arrays as values
        Map<String,String[]> analytics = new HashMap<>();
        String[] specialties = new String[3];
        String[] cities = new String[3];
        String[] states = new String[3];
        //find most common specialty searched
        //I took this code from
        //http://ashutosh-srivastav-mongodb.blogspot.com/2017/09/mongodb-aggregation-using-java-api.html
        //https://www.programcreek.com/java-api-examples/?api=com.mongodb.client.model.Aggregates
        //sort from highest to lowest by count
        AggregateIterable<Document> specialty = collection.aggregate(Arrays.asList(
                new Document("$group",
                        new Document("_id", "$" + "specialty").append("count", new Document("$sum", 1)
                        )), Aggregates.sort(Sorts.descending("count"))));
        int i = 0;
        //get the top 3 specialties and populate the array
        for (Document d: specialty) {
            String value = (String) d.get("_id");
            if (!value.equals("")) {
                specialties[i] = (String) d.get("_id");
                i += 1;
            }
            if (i == 3) {
                break;
            }
        }
        //put in map
        analytics.put("specialty", specialties);
        i = 0;
        //find most common city searched
        AggregateIterable<Document> city = collection.aggregate(Arrays.asList(
                new Document("$group",
                        new Document("_id", "$" + "city").append("count", new Document("$sum", 1)
                        )), Aggregates.sort(Sorts.descending("count"))));
        //get the top 3 cities and populate the array
        for (Document d: city) {
            String value = (String) d.get("_id");
            if (!value.equals("")) {
                cities[i] = (String) d.get("_id");
                i += 1;
            }
            if (i == 3) {
                break;
            }
        }
        //put in map
        analytics.put("city", cities);
        i = 0;

        //find most common state searched
        AggregateIterable<Document> state = collection.aggregate(Arrays.asList(
                new Document("$group",
                        new Document("_id", "$" + "state").append("count", new Document("$sum", 1)
                        )), Aggregates.sort(Sorts.descending("count"))));
        //get the top 3 states and populate the array
        for (Document d: state) {
            String value = (String) d.get("_id");
            if (!value.equals("")) {
                states[i] = (String) d.get("_id");
                i += 1;
            }
            if (i == 3) {
                break;
            }
        }
        //put in map
        analytics.put("state", states);
        //map contains 3 key-value pairs
        return analytics;
    }

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
            URL url = new URL("https://npiregistry.cms.hhs.gov/api/?taxonomy_description=" + specialty + "&city=" + city + "&state="
                    + state+ "&postal_code=" + zip + "&limit=8&skip=&version=2.1");
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
