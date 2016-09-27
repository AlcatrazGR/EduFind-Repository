package codebrains.edufind.Utils;


/**
 * Class that contains methods that handle the server analytics from a HTTP response.
 */
public class ServerAnalytics {

    //Constructor
    public ServerAnalytics(){

    }

    /**
     * Method that removes the analytics characters from the php server response.
     *
     * @param response The response string message from the server.
     * @return Returns a string with the analytics removed (the string is a JSON).
     */
    public String RemoveServerAnalyticsFromResponse(String response){

        String[] end = response.split("@EOR@"); //Separate analytics with data.

        String result = end[0]; // Get the data part
        return result.replace("\\", ""); // Replace all the regex chars.
    }

}