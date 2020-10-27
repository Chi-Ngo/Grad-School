package edu.cmu.cngongoc;

import java.io.IOException;
import java.io.PrintWriter;

public class HelloWorldServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //response.getWriter().append("Hello World!");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTPE htm.>");
            out.println("<html>");
            out.println("<head>");
            out.println("<Title>HelloWorldServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Hello World!<h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
