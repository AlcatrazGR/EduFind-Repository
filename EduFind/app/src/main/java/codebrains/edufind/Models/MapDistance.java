package codebrains.edufind.Models;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static codebrains.edufind.Activities.StudentActivity.GetSortedBookList;
import static codebrains.edufind.Activities.StudentActivity.GetStudentGeolocationInfo;

/**
 * Class that contains methods and attributes to calculate the distance between two points, and
 * set a JSON object with all the areas in range selected to be displayed to the map view.
 */
public class MapDistance {

    //Constructor
    public MapDistance() {

    }

    /**
     * Method that handles the calculation of distance between the user and a certain point in the
     * map and creates a json array object holding all the appropriate data to be displayed.
     * @param limitDistance The max distance to search, selected by the user.
     * @return Returns a json array containing all the info to be displayed to the map view.
     * @throws JSONException Exception that occurs whenever error while parsing json data.
     */
    public JSONArray SetMapPointData(double limitDistance) throws JSONException {

        JSONObject studentGeoInfo = GetStudentGeolocationInfo();
        JSONObject sortedList = GetSortedBookList();
        JSONArray usersArray = (JSONArray) sortedList.get("users");

        JSONArray sortedByDistProv = new JSONArray();

        for(int i = 0; i < usersArray.length(); i++) {

            JSONObject userObject = (JSONObject) usersArray.get(i);
            double userLong = Double.parseDouble(studentGeoInfo.get("longitude").toString());
            double userLat = Double.parseDouble(studentGeoInfo.get("latitude").toString());
            double pointLong = Double.parseDouble(userObject.get("longitude").toString());
            double pointLat = Double.parseDouble(userObject.get("latitude").toString());

            double distance = this.CalculateDistanceForSortedItems(userLong, userLat,
                    pointLong, pointLat);
            if(distance <= limitDistance) {
                JSONObject approvedUser = new JSONObject();
                approvedUser.put("provider", userObject.get("provider"));
                approvedUser.put("address", userObject.get("address"));
                approvedUser.put("number", userObject.get("number"));
                approvedUser.put("distance", distance);

                sortedByDistProv.put(approvedUser);
            }

        }

        return sortedByDistProv;
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
