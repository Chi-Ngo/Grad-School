package edu.cmu.cngongoc.p1t3;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DSClickerServlet",
        urlPatterns = {"/submit","/getResults"})
public class DSClickerServlet extends HttpServlet {
    DSClickerModel dsm = null;  // The "business model" for this app

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        dsm = new DSClickerModel();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        if (request.getServletPath().equals("/submit")) {
            //get the choice submitted by the user and pass it as an Attribute for the next view
            String choice = request.getParameter("choice");
            request.setAttribute("answer", choice);
            //if the user makes a choice
            if (choice != null) {
                //increase the counter of that choice in the array
                dsm.addScore(choice);
                //forward the user to nextqn.jsp view where the user sees a feedback and submit another answer
                RequestDispatcher view = request.getRequestDispatcher("nextqn.jsp");
                view.forward(request, response);
            }
            //if the user didn't submit any choice but just press submit
            else {
                //forward the user to index.jsp so the user answers the current question
                RequestDispatcher view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        //if the page is /getResults
        if (request.getServletPath().equals("/getResults")){
            //get the array that stores the count of all choices
            int[] freq = dsm.getFreq();
            //forward these counts as Attribute to the result.jsp view
            request.setAttribute("freqArray",freq);
            RequestDispatcher view = request.getRequestDispatcher("result.jsp");
            view.forward(request, response);
            //clear these results
            dsm.clearFreq();
        }
        //if the page is /Project1Task3
        else {
            RequestDispatcher view = request.getRequestDispatcher("index.jsp");
            view.forward(request, response);
        }
    }
}
