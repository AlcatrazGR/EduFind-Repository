package codebrains.edufind.Utils;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Class that contains methods which handle the HTTP requests and responses from the remote server
 * and process the response so that the output will be a JSON object.
 */
public class JSONParser {

    private String path;

    //Constructor
    public JSONParser() {
        this.path = "http://edufind.hol.es/Android";
    }


    /**
     * Method that retrieves a JSON string from a URL and converts it to JSON object.
     *
     * @param script The name of the script.
     * @return Returns the JSON object that was the response from server.
     */
    public JSONObject HttpRequestGetData(String script) {

        InputStream is = null;
        String result = "";

        // Making HTTP request
        try {
            // Construct the client and the HTTP request.
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(this.path + script);
            httpPost.setHeader(HTTP.CONTENT_TYPE,
                    "application/x-www-form-urlencoded;charset=UTF-8");

            // Execute the POST request and store the response locally.
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            // Create a BufferedReader to parse through the inputStream.
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Build the string until null.
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            // Close the input stream.
            is.close();
            result = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            throw new NullPointerException();
        }

        Log.d("Before Analytics : ", result);

        //Calls the analytics remover method to clean the response.
        ServerAnalytics sc = new ServerAnalytics();
        result = sc.RemoveServerAnalyticsFromResponse(result);

        Log.d("After Analytics : ", result);

        JSONObject responseJSON = null;
        try {
            responseJSON = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Return the JSON Object.
        return responseJSON;
    }

    /**
     * Method that initializes an HTTP request with the given data, url and method, retrieves the
     * response and converts it into JSON object.
     *
     * @param params The data t be transmitted.
     * @return Returns the JSON response of the server.
     */
    public JSONObject HttpRequestPostData(List<NameValuePair> params, String script) {

        InputStream is = null;
        JSONObject jsonObject = null;
        String result = "";

        // Making HTTP request
        try {

            // request method is POST defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(this.path + script);

            Log.d("Data to be send : ", String.valueOf(params));

            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
            result = sb.toString();

        } catch (UnsupportedEncodingException e) {
            Log.e("Exception!!! : ", "UnsupportedEncodingException, "+e);
        } catch (UnknownHostException e) {
            Log.e("Exception!!! : ", "UnknownHostException, " + e);
        } catch (IOException e) {
            Log.e("Exception!!! : ", "IOException, " + e);
        } catch (NullPointerException e){
            throw new NullPointerException();
        }

        Log.d("Before Analytics : ", result);

        //Calls the analytics remover method to clean the response.
        ServerAnalytics sc = new ServerAnalytics();
        result = sc.RemoveServerAnalyticsFromResponse(result);

        Log.d("After Analytics : ", result);

        // try parse the string to a JSON object
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jsonObject;
    }




}