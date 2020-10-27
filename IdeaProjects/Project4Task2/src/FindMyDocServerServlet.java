//Chi Ngo
//cngongoc
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "FindMyDocServerServlet")
public class FindMyDocServerServlet extends HttpServlet {
    FindMyDocModel db = null;  // Initiate the model

    //Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        db = new FindMyDocModel();
    }

    //POST method is used for logging to MongoDB database
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("POST visited");
        //get the specialty from the request
        String specialty = request.getParameter("specialty");
        //get the city from the request
        String city = request.getParameter("city");
        //get the response code of the query
        String code = request.getParameter("code");
        //capitalize the city name for standardization
        //I get this code from https://stackoverflow.com/questions/1892765/how-to-capitalize-the-first-character-of-each-word-in-a-string/1892778
        if (!city.equals("")) {
            String[] arr = city.split(" ");
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < arr.length; i++) {
                sb.append(Character.toUpperCase(arr[i].charAt(0)))
                        .append(arr[i].substring(1)).append(" ");
            }
            city = sb.toString().trim();
        }
        //get the state from the request
        String state = request.getParameter("state");
        //Upper case the state abbreviation for standardization
        state = state.toUpperCase();
        //get the zip from the request
        String zip = request.getParameter("zip");
        //get the number of doctors found in the query from the request
        String number = request.getParameter("number");
        //get the time the query was sent
        String timestamp = request.getParameter("timestamp");
        //get information about the device that sent the query
        String ua = request.getHeader("User-Agent");
        String device = ua.split("\\(")[1].split("\\)")[0];
        //insert 1 document to database
        db.log(specialty,city,state,zip,timestamp,number,code,device);
    }

    //for displaying the dashboard
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("GET visited");
        //if the GET request is for the dashboard
        if (request.getRequestURI().contains("/getDashboard")) {
            //read all documents to a list
            List<Document> documents;
            documents = db.read();

            //find 3 pieces of interesting analytics
            //these 3 pieces are top 3 most common specialty searched, most common city searched, and most common state searched
            Map<String, String[]> analytics = db.analytics();

            //set the list of documents and map of analytics to the request
            request.setAttribute("documents", documents);
            request.setAttribute("analytics", analytics);
            //forward view to result.jsp
            RequestDispatcher view = request.getRequestDispatcher("result.jsp");
            view.forward(request, response);
            //else if the user does not want to see the dashboard
        } else {
            //if the request comes with parameters
            if (request.getQueryString() != null) {
                //get the city name from the request
                String city = request.getParameter("city").replaceAll(" ","+");
                //get the state name from the request
                String state = request.getParameter("state");
                //get the zip code from the request
                String zip = request.getParameter("zip");
                //get the specialty from the request
                String specialty = request.getParameter("specialty").replaceAll(" ","+");
                ///this array stores the doctors' names and basic information
                JSONArray info = new JSONArray();
                //if none of the parameters is null
                if (city != null && state != null && zip != null && specialty != null) {
                    //call fetchNames to find doctors based on the search parameters
                    info = db.fetchNames(city, state, zip, specialty);
                }
                //this JSONObject stores the result for response sent to the client
                JSONObject result = new JSONObject();
                result.put("doctors", info);
                //send response to client
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                out.print(result.toJSONString());
                out.flush();
            }
        }
    }
}
