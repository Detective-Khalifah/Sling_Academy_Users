package com.blogspot.thengnet.slingacademyusers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchUsers {

    private final static String LOG_TAG = FetchUsers.class.getName();

    private static Context mAppContext;

    // Make class non-instantiable; not a singleton either
    private FetchUsers () {
    }

    /**
     * @param context    of the app
     * @param requestURL an initial, un-parsed search query
     * @return a {@link List} of {@link User} objects.
     */
    protected static List<User> lookupArticles (Context context, String requestURL) {

        mAppContext = context;

        // Create URL obj
        URL url = createURL(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String JSONResponse = null;

        try {
            JSONResponse = makeHTTPRequest(url);
        } catch (IOException requestException) {
            Log.i(LOG_TAG, "makeHTTPRequest(): " + requestException);
        }

        // Generate a list of thoughtsList from fetched JSON
        return extractJSONData(JSONResponse);
    }

    /**
     * @param stringURL generated by {@link MainActivity}
     * @return a {@link URL} object that could be utilized over the net to send/receive request(s).
     */
    private static URL createURL (String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException mURLE) {
            Log.i(LOG_TAG, mURLE.toString());
        }
        return url;
    }

    /**
     * @param url parsed by method createURL
     * @return JSON response from the request sent to server.
     * @throws IOException if there's a problem with the radio making connection.
     */
    private static String makeHTTPRequest (URL url) throws IOException {
        String JSONResponse = "";

        // If the URL is null, then return early
        if (url == null) {
            return JSONResponse;
        }

        HttpURLConnection urlConn = null;
        InputStream inputStream = null;

        try {
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");

            urlConn.connect();

            // If the request was successful (response code 200), then read the input stream and
            // parse the response.
            int responseCode = urlConn.getResponseCode();
            Log.i(LOG_TAG, "getResponseCode" + urlConn.getResponseCode());
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i(LOG_TAG, "HTTP_OK");

                inputStream = urlConn.getInputStream();

                JSONResponse = readFromStream(inputStream);
            } else {
                Log.i(LOG_TAG, "from makeHTTPRequest responseCode:: " + urlConn.getResponseCode()
                        + "\nresponseMessage::" + urlConn.getResponseMessage()
                        + "\nErrorStream:: " + urlConn.getErrorStream().read());
                getJSONErrorMessage(readFromStream(urlConn.getInputStream()));
            }
        } catch (IOException e) {
            Log.i(LOG_TAG, "urlConn: " + e);
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return JSONResponse;
    }

    /**
     * @param inStream received from net server.
     * @return a {@link String} value of the stream.
     * @throws IOException while processing {@link InputStream} if there's something amiss!
     */
    private static String readFromStream (InputStream inStream) throws IOException {
//        Log.i(LOG_TAG, "readFromStream()");
        StringBuilder myList = new StringBuilder();
        if (inStream != null) {
            InputStreamReader inStreamDecode = new InputStreamReader(inStream);
            BufferedReader reader = new BufferedReader(inStreamDecode);

            String line = reader.readLine();
            while (line != null) {
                myList.append(line);
                line = reader.readLine();
            }
            Log.i(LOG_TAG, "readFromStream():: " + myList.toString());
        }
        return myList.toString();
    }

    private static List<User> extractJSONData (String thoughtsJSONResponse) {

        // If the JSON String is empty or null, then return early.
        if (TextUtils.isEmpty(thoughtsJSONResponse))
            return null;

        // Create an empty ArrayList that we can start adding DeepThoughts to
        List<User> users = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        // Otherwise, build up a list of Thoughts objects with the corresponding data.
        try {

            // Create a JSONObject from the SAMPLE_JSON_RESPONSE string
            JSONObject rootJSONObj = new JSONObject(thoughtsJSONResponse);


            // Extract the JSONArray associated with the key called "data"
            JSONArray usersArray = rootJSONObj.getJSONArray("users");
            Log.i(LOG_TAG, "usersArray: " + usersArray.toString());

            // For each user in the usersArray, create an {@link User} object
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject dataObj = usersArray.getJSONObject(i);
                Log.i(LOG_TAG, "dataObj: " + dataObj.toString());

                // Extract the value for the keys called "first_name", "last_name",...
                String lastName = dataObj.getString("last_name");
                String eMail = dataObj.getString("email");
                int id = dataObj.getInt("id");
                String phone = dataObj.getString("phone");
                String street = dataObj.getString("street");
                String state = dataObj.getString("state");
                String zipcode = dataObj.getString("zipcode");
                double latitude = dataObj.getDouble("latitude");
                String gender = dataObj.getString("gender");
                String firstName = dataObj.getString("first_name");
                String dateOfBirth = dataObj.getString("date_of_birth");
                String job = dataObj.getString("job");
                String city = dataObj.getString("city");
                String country = dataObj.getString("country");
                double longitude = dataObj.getDouble("longitude");
                Log.i(LOG_TAG,
                        "id: " + id + "\n" +
                                "first name: " + firstName + "\n" +
                                "last name: " + lastName + "\n" +
                                ""
                );

                // Format the date using the Utils formatDate function
                String formattedDate = Utils.Companion.formatDate(dateOfBirth);

                users.add(new User(
                        id,
                        firstName,
                        lastName,
                        dateOfBirth,
                        gender,
                        eMail,
                        phone,
                        country,
                        state,
                        city,
                        street,
                        latitude,
                        longitude,
                        zipcode,
                        job
                ));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements of the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.i(LOG_TAG, "Problem parsing the JSON results_page", e);
            JSONObject root;
            try {
                root = new JSONObject(thoughtsJSONResponse);
                Log.i(LOG_TAG, "success: " + root.getString("success")
                        + "\nmessage: " + root.getString("message")
                );
            } catch (JSONException ex) {
                Log.i(LOG_TAG, ex.toString());
            }
        }

        // Return the list (polymorphed ArrayList) of thoughtsList
        return users;
    }

    private static void getJSONErrorMessage (String JSON) {
        JSONObject root;
        try {
            root = new JSONObject(JSON);
            Log.i(LOG_TAG, "success: " + root.getString("success")
                    + "\nmessage: " + root.getString("message")
            );
        } catch (JSONException ex) {
            Log.i(LOG_TAG, ex.toString());
        }
    }

}
