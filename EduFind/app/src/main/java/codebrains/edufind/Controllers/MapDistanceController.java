package codebrains.edufind.Controllers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import codebrains.edufind.Models.MapDistance;

/**
 * Controller class that handles the calculation of the distances between two points in order
 * to display the appropriate data to the google map.
 */
public class MapDistanceController {

    private JSONArray sortedByDistanceList;

    //Constructor
    public MapDistanceController() {
    }

    /**
     * Method that controls the process of setting the provider data, calculating the distances and
     * display them on map or display error instead if needed.
     * @param distance The maximum distance to search, selected by the user.
     * @return Returns a boolean variable showing the status of the process.
     */
    public boolean CalculateDistanceControl(double distance) {

        MapDistance md = new MapDistance();
        try {
            this.sortedByDistanceList = md.SetMapPointData(distance);

            if(this.sortedByDistanceList == null || this.sortedByDistanceList.length() == 0)
                return false;

        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : CalculateDistanceControl->" + e);
            return false;
        }

        return true;
    }

    /**
     * Method that returns the private sorted by distance list.
     * @return Returns a json array containing all the appropriate data to be displayed to the
     * map view after search.
     */
    public JSONArray GetListOfProvidersSortedByDistance() {
        return this.sortedByDistanceList;
    }

}
