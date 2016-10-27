package codebrains.edufind.Models;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static codebrains.edufind.Activities.StudentActivity.GetSortedBookList;

/**
 * Class that contains methods and attributes to calculate the distance between two points, and
 * set a JSON object with all the areas in range selected to be displayed to the map view.
 */
public class MapDistance {

    //Constructor
    public MapDistance() {

    }

    public void SetMapPointData(double distance) throws JSONException {

        JSONObject sortedList = GetSortedBookList();

        JSONArray usersArray = (JSONArray) sortedList.get("users");

        for(int i = 0; i < usersArray.length(); i++) {



        }


    }

    /**
     * Method that calculated the distance between the users position and a points position.
     * @param userLong The longitude of the user.
     * @param userLat The latitude of the user.
     * @param provLong The longitude of the provider.
     * @param provLat The latitude of the provider.
     * @return Returns the distance between those two points.
     */
    public double CalculateDistanceForSortedItems(double userLong, double userLat,
                                                double provLong, double provLat) {

        Location userLocation = new Location("User Location");
        userLocation.setLatitude(userLat);
        userLocation.setLongitude(userLong);

        Location pointLocation = new Location("Point Location");
        pointLocation.setLongitude(provLong);
        pointLocation.setLatitude(provLat);

        double distance = userLocation.distanceTo(pointLocation);
        return distance;

    }



}
