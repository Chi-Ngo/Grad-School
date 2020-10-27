package edu.cmu.cngongoc.p1t2;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

public class BirdModel {
    public List<String> fetchNames(String input) {
        //this is code to manage SSLHandshakeExceptions by professors
        try {
            // Create trust manager, which lets you ignore SSLHandshakeExceptions
            createTrustManager();
        } catch (KeyManagementException ex) {
            System.out.println("Shouldn't come here: ");
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Shouldn't come here: ");
            ex.printStackTrace();
        }
        List<String> birds = new ArrayList<>();
        try {
            URL url = new URL("https://nationalzoo.si.edu/scbi/migratorybirds/featured_photo/");
            /*
             * Create an HttpURLConnection.  This is useful for setting headers
             * and for getting the path of the resource that is returned (which
             * may be different than the URL above if redirected).
             * HttpsURLConnection (with an "s") can be used if required by the site.
             */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            // Read each line of "in" until done, adding each to "response"
            while ((str = in.readLine()) != null) {
                // if str is part of the dropdown menu html code, split it to get the bird name
                if (str.contains("option value=")) {
                    birds.add(str.split(">")[1].split("</")[0]);
                }
                //if we reach the photographer dropdown, stop the loop
                if (str.contains("<select name=\"photographer\" id=\"photographer\"")) {
                    break;
                }
            }
            in.close();
            //birds now contains the list of all birds
        } catch (IOException e) {
            System.out.println("Eeek, an exception");
        }
        List<String> someBirds = new ArrayList<>();
        //populate list someBirds with just birds that contain the input string
        for (String b: birds) {
            if (b.toLowerCase().contains(input.toLowerCase())) {
                someBirds.add(b);
            }
        }
        //check for size of someBirds list
        int size = Math.min(someBirds.size(),10);
        //if the size exceeds 10
        if (size == 10) {
            //add 10 random birds from someBirds to set birdies
            //this is to ensure we don't add duplicated names
            Set<String> birdies = new HashSet<>();
            while (birdies.size() < 10) {
                Random r = new Random();
                int i = r.nextInt(someBirds.size());
                birdies.add(someBirds.get(i));
            }
            //convert to list and return
            List<String> random10Birds = new ArrayList<>(birdies);
            return random10Birds;
        }
        //if size is less than 10, return the whole list of someBirds
        else{
            return someBirds;
        }
    }

    public String[] fetchBird(String urlString) {
        //this is code to manage SSLHandshakeExceptions by professors
        try {
            // Create trust manager, which lets you ignore SSLHandshakeExceptions
            createTrustManager();
        } catch (KeyManagementException ex) {
            System.out.println("Shouldn't come here: ");
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Shouldn't come here: ");
            ex.printStackTrace();
        }
        //for the chosen bird, get the list of all picture URL's and all corresponding photographer
        List<String> picURLs = new ArrayList<>();
        List<String> photographers = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            /*
             * Create an HttpURLConnection.  This is useful for setting headers
             * and for getting the path of the resource that is returned (which
             * may be different than the URL above if redirected).
             * HttpsURLConnection (with an "s") can be used if required by the site.
             */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            // Read each line of "in" until done, adding each to "response"
            // only get the first 20 pictures and first 20 photographers
            while ((str = in.readLine()) != null && picURLs.size() <= 20) {
                //if string is of picture URL, add just the url part to picURL
                if (str.contains("Enlarge photograph")) {
                    picURLs.add(str.split("src=")[1].split("alt=")[0].replace('"',' ').strip());
                }
                //if string is of name of photographer, add just the name to the photographers list
                else if (str.contains("this photographer")) {
                    String name = str.split(">&copy; ")[1].split("</a")[0].strip();
                    //replace special character ' to display the actual '
                    name = name.replace("&apos;","'");
                    photographers.add(name);
                }
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Eeek, an exception");
        }
        String[] result;
        //if some picURLs are found
        if (picURLs.size() != 0) {
            //pick out a picture url and its corresponding photographer at random
            Random r = new Random();
            int bound = Math.min(picURLs.size(),20);
            int ind = r.nextInt(bound);
            result = new String[]{picURLs.get(ind), photographers.get(ind)};
        }
        //if no picture is found, return null for both picURL and photographer name
        else {
            result = new String[]{null, null};
        }
        return result;
    }

    //this is code to manage SSLHandshakeExceptions by professors
    private void createTrustManager() throws KeyManagementException, NoSuchAlgorithmException {

        /**
         * Annoying SSLHandShakeException. After trying several methods, finally this
         * seemed to work.
         * Taken from: http://www.nakov.com/blog/2009/07/16/disable-certificate-validation-in-java-ssl-connections/
         */
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}
