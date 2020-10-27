package edu.cmu.cngongoc.p1t2;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BirdServlet",
        urlPatterns = {"/getAName","/getABird","/redirect"})
public class BirdServlet extends javax.servlet.http.HttpServlet {
    BirdModel bm = null;  // Initiate the model

    //Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        bm = new BirdModel();
    }

    //code to check if browser is on laptop or mobile phone and resize page appropriately
    //copied from InterestingPicture
    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        // determine what type of device our user is
        String ua = request.getHeader("User-Agent");

        boolean mobile;
        // prepare the appropriate DOCTYPE for the view pages
        if (ua != null && ((ua.indexOf("Android") != -1) || (ua.indexOf("iPhone") != -1))) {
            mobile = true;
            /*
             * This is the latest XHTML Mobile doctype. To see the difference it
             * makes, comment it out so that a default desktop doctype is used
             * and view on an Android or iPhone.
             */
            request.setAttribute("doctype", "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.2//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd\">");
        } else {
            mobile = false;
            request.setAttribute("doctype", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        }
        //check if the request comes from the user's input string
        if (request.getServletPath().equals("/getAName")) {
            String input = request.getParameter("name");
            List<String> birdies;
            List<String> someBirds = new ArrayList<>();
            String nextView;
            if (input != null) {
                //create a list that only contains birds that match user's input string
                //this list either contains 10 birds or less, depending on the number of matches
                //set the next view to dropdown.jsp, where the dropdown menu is
                birdies = bm.fetchNames(input);
                //set this list as an attribute of request
                request.setAttribute("birdsList",birdies);
                nextView = "dropdown.jsp";
            }
            else {
                //if the user didn't make any input, show them the input page again
                nextView = "index.jsp";
            }
            //forward the nextView to the user
            RequestDispatcher view = request.getRequestDispatcher(nextView);
            view.forward(request, response);
        }
        //check if the request comes from the dropdown menu page
        else if (request.getServletPath().equals("/getABird")) {
            String picSize = (mobile) ? "mobile" : "desktop";
            //get the bird chosen by the user in the menu
            //split the bird's name by space to form a URL that directs to the page of that bird
            String input = request.getParameter("bird");
            String [] parts = input.split(" ");
            String url = "https://nationalzoo.si.edu/scbi/migratorybirds/featured_photo/bird.cfm?pix=";
            for (int i = 0; i < parts.length - 1; i++) {
                url = url + parts[i] +  "+";
            }
            url += parts[parts.length-1];
            //using the url, fetch a random picture and the name of the photographer, store both info in an array
            //the url of the picture is at index 0
            //the name of the author is at index 1
            String[] result = bm.fetchBird(url);
            request.setAttribute("pictureURL",result[0]);
            request.setAttribute("birdName",input);
            request.setAttribute("author",result[1]);
            //forward the result page to the user
            RequestDispatcher view = request.getRequestDispatcher("result.jsp");
            view.forward(request, response);
        }
        //if action is "/redirect", aka the action from clicking the Continue button in the result.jsp page
        //forward the user back to the index.jsp page
        else {
            RequestDispatcher view = request.getRequestDispatcher("index.jsp");
            view.forward(request, response);
        }
    }
}
