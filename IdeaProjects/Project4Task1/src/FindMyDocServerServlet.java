//Chi Ngo
//cngongoc

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "FindMyDocServerServlet")
public class FindMyDocServerServlet extends javax.servlet.http.HttpServlet {
    FindMyDocUsingWS bd = null;  // Initiate the model

    //Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        bd = new FindMyDocUsingWS();
    }


    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws IOException {
        //if the request comes with parameters
        if (request.getQueryString() != null) {
            //get the city name from the request
            String city = request.getParameter("city").replaceAll(" ","+");
            //get the state name from the request
            String state = request.getParameter("state");
            //get the zip number from the request
            String zip = request.getParameter("zip");
            //get the doctor's specialty from the request
            String specialty = request.getParameter("specialty").replaceAll(" ","+");
            //this array stores the doctors' names and basic information
            JSONArray info = new JSONArray();
            //if none of the parameters is null
            if (city != null && state != null && zip != null && specialty != null) {
                //call fetchNames to find doctors based on the search parameters
                info = bd.fetchNames(city, state, zip, specialty);
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
