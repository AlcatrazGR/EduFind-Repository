package codebrains.edufind.Utils;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
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
     * Method that handles post data to the remote server, receiving back the response processing
     * it in JSON object format.
     * @param script The name of the script to post / get data.
     * @param data The data to be transmitted in a JSON object format.
     * @param mActivity The activity that fired the async task.
     * @return Returns a JSON object with the response of the server.
     * @throws IOException Exception for the input and output data while processing them.
     * @throws JSONException Exception that occurs whenever processing JSON data and something goes wrong.
     */
    public JSONObject HttpRequestPostData(String script, JSONObject data, Activity mActivity) throws IOException, JSONException {

        String response = ""; //String that will contain raw JSON response.
        BufferedReader br = null; //Buffered reader for reading the input stream.
        HttpURLConnection urlConnection = null; //The url connection object.

        MessageCenter msgCent = new MessageCenter(mActivity);
        SystemControl sc = new SystemControl(mActivity);

        // If the server is not reachable.
        if(!sc.RemoteServerIsReachable(this.path)) {
            String title = "Connection Error";
            String message = "There was an error while trying to connect to the server. It seems " +
                    "that the server is currently offline. Please try again later or contact the " +
                    "support team.";
            msgCent.DisplayErrorDialog(mActivity, title, message);
            return null;
        }

        try {

            URL url = new URL(this.path + script);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST"); //Transmit method initialize.
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("account", String.valueOf(data).trim());
            String query = builder.build().getEncodedQuery();

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

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
                response = null;
                String title = "Connection Error";
                String message = "There was an error while trying to communicate with the server." +
                        " Please try again later, or contact the support team.";
                msgCent.DisplayErrorDialog(mActivity,title, message);
            }

        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            throw new NullPointerException();
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


        return new JSONObject(response);
    }


    /**
     * Method that handled the connection with a script at the remote server without posting any data.
     * After that it processes the response and returns it to JSON object format.
     * @param script The name of the script to post / get data.
     * @param mActivity The activity that fired the async task.
     * @return Returns a JSON object which is the response from the remote server.
     * @throws IOException Exception for the input and output data while processing them.
     * @throws JSONException Exception that occurs whenever processing JSON data and something goes wrong.
     */
    public JSONObject HTTPRequestGetData(String script, Activity mActivity) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;
        String response = "";
        BufferedReader br = null;

        MessageCenter msgCent = new MessageCenter(mActivity);
        SystemControl sc = new SystemControl(mActivity);

        // If the server is not reachable.
        if(!sc.RemoteServerIsReachable(this.path)) {
            String title = "Connection Error";
            String message = "There was an error while trying to connect to the server. It seems " +
                    "that the server is currently offline. Please try again later or contact the " +
                    "support team.";
            msgCent.DisplayErrorDialog(mActivity, title, message);
            return null;
        }

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
                response = null;
                String title = "Connection Error";
                String message = "There was an error while trying to communicate with the server." +
                        " Please try again later, or contact the support team.";
                msgCent.DisplayErrorDialog(mActivity,title, message);
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
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


        return new JSONObject(response);
    }

}