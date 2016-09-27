package codebrains.edufind.Utils;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Vasilhs on 1/18/2016.
 */
public class JSONParser {


    public JSONObject HttpRequestPostData(String path, String method, JSONObject data) throws IOException, JSONException {

        String response = ""; //String that will contain raw JSON response.
        BufferedReader reader = null; //Buffered reader for reading the input stream.
        HttpURLConnection urlConnection = null; //The url connection object.

        try {

            URL url = new URL(path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method); //Transmit method initialize.
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

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = null;
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

        Log.d("Before Analytics : ", response);

        //Calls the analytics remover method to clean the response.
        ServerAnalytics sa = new ServerAnalytics();
        response = sa.RemoveServerAnalyticsFromResponse(response);

        Log.d("After Analytics : ", response);


        return new JSONObject(response);

            /*
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            if (in == null) {
                return null; // Nothing to do.
            }

            reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            if (sb.length() == 0) {
                return null;  // Stream was empty.  No point in parsing.
            }

            in.close();
            response = sb.toString();

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

            if(urlConnection != null)
                urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        Log.d("Response Before : ", response);

        //Calls the analytics remover method to clean the response.
        ServerAnalytics sa = new ServerAnalytics();
        response = sa.RemoveServerAnalyticsFromResponse(response);

        Log.d("Response After : ", response);

        return new JSONObject(response);
        */
    }









    /*
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public JSONParser() {

    }

    public JSONObject GetJSONFromUrl(final String url) {

        try {

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
    }

    public JSONObject MakeHttpRequest(String url, String method, List<NameValuePair> params) {

        // Making HTTP request
        try {

            // check for request method
            if(method == "POST"){

                // request method is POST defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }else if(method == "GET"){

                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            throw new NullPointerException();
        }


        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        Log.d("JSON Response - ", json);

        //Calls the analytics remover method to clean the response.
        ServerAnalytics sa = new ServerAnalytics();
        json = sa.RemoveServerAnalyticsFromResponse(json);

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jObj;
    }

    */
}