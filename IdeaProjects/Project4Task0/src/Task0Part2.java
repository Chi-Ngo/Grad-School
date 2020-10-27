import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Task0Part2 {

    public static void main (String[] args) throws IOException {

        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://cngongoc:dsproject4@cluster0-sztnn.mongodb.net/test?retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("task0");

        BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        while (!input.equals("1")) {
            System.out.println("Enter a string or 1 to exit:");
            input = typed.readLine();
            if (!input.equals("1")) {
                Document doc = new Document("data", input);
                collection.insertOne(doc);
            }
        }

        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().get("data"));
            }
        } finally {
            cursor.close();
        }
        AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
                new Document("$group",
                        new Document("_id", "$" + "data").append("count", new Document("$sum", 1)
                        )), Aggregates.sort(Sorts.descending("count"))));
        for (Document d: output) {
            System.out.println(d.get("_id"));
            break;
        }
    }
}
