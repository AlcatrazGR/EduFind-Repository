package codebrains.edufind.Utils;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Class that contains methods which handle the HTTP requests and responses from the remote server
 * and process the response so that the output will be a JSON object.
 */
public class JSONParser {

    private String path;

    //Constructor
    public JSONParser() {
        this.path = "http://www.edufind.comlu.com/Android";
    }


    /**
     * Method that retrieves a JSON string from a URL and converts it to JSON object.
     *
     * @param url The url in string form.
     * @return Returns the JSON object that was the response from server.
     */
    /*public JSONObject GetJSONFromUrl(final String url) {

        // Making HTTP request
        try {
            // Construct the client and the HTTP request.
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            // Execute the POST request and store the response locally.
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // Extract data from the response.
            HttpEntity httpEntity = httpResponse.getEntity();

            // Open an inputStream with the data content.
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Create a BufferedReader to parse through the inputStream.
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);

            // Declare a string builder to help with the parsing.
            StringBuilder sb = new StringBuilder();

            // Declare a string to store the JSON object data in string form.
            String line = null;

            // Build the string until null.
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            // Close the input stream.
            is.close();

            // Convert the string builder data to an actual string.
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // Try to parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // Return the JSON Object.
        return jObj;
    } */

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

        // try parse the string to a JSON object
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jsonObject;
    }
















































    /**
     * Method that handles post data to the remote server, receiving back the response processing
     * it in JSON object format.
     * @param script The name of the script to post / get data.
     * @param data The data to be transmitted in a JSON object format.
     * @param mActivity The activity that fired the async task.
     * @return Returns a JSON object with the response of the server.
     * @throws IOException Exception for the input and output data while processing them.
     * @throws JSONException Exception that occurs whenever processing JSON data and something goes wrong.
     */
   /*public JSONObject HttpRequestPostData(String script, JSONObject data, Activity mActivity) throws IOException, JSONException {

        String response = ""; //String that will contain raw JSON response.
        BufferedReader br = null; //Buffered reader for reading the input stream.
        HttpURLConnection urlConnection = null; //The url connection object.
        JSONObject checkJSON = new JSONObject();

        MessageCenter msgCent = new MessageCenter(mActivity);

        try {

            URL url = new URL(this.path + script);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST"); //Transmit method initialize.
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);


            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("data", String.valueOf(data).trim());
            String query = builder.build().getEncodedQuery();

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));

            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();

            //If the http connection was ok (response code = 200) then start processing the
            //response, if not then display message and do nothing.
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                checkJSON.put("status", 0);
                checkJSON.put("title", "Connection Error");
                checkJSON.put("message", "There was an error while trying to communicate with the server." +
                        " Please try again later, or contact the support team.");
                return checkJSON;
            }

        }
        catch(MalformedURLException e){
            throw new MalformedURLException("The url that is given is invalid!" + e);
        }
        catch (NullPointerException e){
            throw new NullPointerException("Trying to process the value of an object which is null" +
                e);
        }
        finally {
            if (br != null) br.close();
            if (urlConnection != null) urlConnection.disconnect();
        }

        Log.d("Before Analytics : ", response);

        //Calls the analytics remover method to clean the response.
        ServerAnalytics sa = new ServerAnalytics();
        response = sa.RemoveServerAnalyticsFromResponse(response);

        Log.d("After Analytics : ", response);

        JSONObject responseJSON = new JSONObject(response);

        return responseJSON;
    } */


    /**
     * Method that handled the connection with a script at the remote server without posting any data.
     * After that it processes the response and returns it to JSON object format.
     * @param script The name of the script to post / get data.
     * @param mActivity The activity that fired the async task.
     * @return Returns a JSON object which is the response from the remote server.
     * @throws IOException Exception for the input and output data while processing them.
     * @throws JSONException Exception that occurs whenever processing JSON data and something goes wrong.
     */
    /* public JSONObject HTTPRequestGetData(String script, Activity mActivity) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;
        String response = "";
        BufferedReader br = null;
        JSONObject checkJSON = null;

        try {
            URL url = new URL(this.path + script);
            urlConnection = (HttpURLConnection) url.openConnection();
            int connectionStatus = urlConnection.getResponseCode();

            //If connection is ok (status code 200)
            if(connectionStatus == HttpURLConnection.HTTP_OK) {
                InputStream in = urlConnection.getInputStream();
                br = new BufferedReader(new InputStreamReader(in));

                String line = "";
                while((line = br.readLine()) != null) {
                    response += line;
                }

            }
            else {

                checkJSON.put("status", 0);
                checkJSON.put("title", "Connection Error");
                checkJSON.put("message", "There was an error while trying to communicate with the server." +
                        " Please try again later, or contact the support team.");
                return checkJSON;
            }

        }
        catch (MalformedURLException e) {
            throw new MalformedURLException("The url that is given is invalid!" + e);
        }
        catch (NullPointerException e) {
            throw new NullPointerException("Trying to process the value of an object which is null" +
                    e);
        }
        finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d("Before Analytics : ", response);

        //Calls the analytics remover method to clean the response.
        ServerAnalytics sa = new ServerAnalytics();
        response = sa.RemoveServerAnalyticsFromResponse(response);

        Log.d("After Analytics : ", response);

        JSONObject responseJSON = new JSONObject(response);

        return responseJSON;
    } */

}