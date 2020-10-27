package edu.cmu.cngongoc.p1t1;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet(name = "ComputeHashes",
        urlPatterns = {"/getATextInput"})
public class ComputeHashes extends javax.servlet.http.HttpServlet {

    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //get the user's text input and the type of hash through request.getParameter()
        String input = request.getParameter("input");
        String type = request.getParameter("type");
        MessageDigest md = null;
        //use MessageDigest to find a byte array of the hash using the appropriate type.
        //First, instantiate MessageDigest with the type of hash (MD5/SHA-256)
        //Then, use update() to find a byte array representation of the hash
        try {
            md = MessageDigest.getInstance(type);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(input.getBytes());
        byte[] value = md.digest();
        //change the byte array to text using base64 encoding or hexadecimal encoding and print out the values
        String base64Coding = javax.xml.bind.DatatypeConverter.printBase64Binary(value);
        String hexCoding = javax.xml.bind.DatatypeConverter.printHexBinary(value);
        //print out the response in html format
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTPE htm.>");
            out.println("<html>");
            out.println("<head>");
            out.println("<Title>Hash Computer</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Hash Result</h1>");
            out.println("<p>Original input: " + input + "</p>");
            out.println("<p>" + type + " hash value in base 64 notation: " + base64Coding + "</p>");
            out.println("<p>" + type + " hash value in hexadecimal notation: " + hexCoding + "</p>");
            out.println("</body>");
            //button to direct back to the original input page for a new input
            out.println("<input type=\"button\" value=\"Continue\" onclick=\"window.location='index.jsp'\">");
            out.println("</html>");
        }
    }
}
