package codebrains.edufind.Utils;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Class the contains methods necessary to calculate the geolocation of a device (longitude, latitude,
 * city, state etc).
 */
public class Coordinates extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    //Constructor
    public Coordinates(Context context) {
        this.mContext = context;
        GetLocation();
    }

    /**
     * Method that gets the location (longitude, latitude) of the device.
     * @return Returns a Location object containing the location info.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public Location GetLocation() {

        MessageCenter msgCent = new MessageCenter(this.mContext);

        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // If GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {

                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
            else {
                msgCent.GPSNotEnabledErrorDialog();
            }


            // If the network in not enabled
            if (isNetworkEnabled) {

                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Method that returns the ciy name with the coordinates given.
     */
    public JSONObject GetLocationInfoFromCoordinates() {

        JSONObject locationAreaJson = new JSONObject();
        if (location != null) {
            Geocoder gcd = new Geocoder(this.mContext, Locale.getDefault());
            List<Address> addresses = null;

            try {
                addresses = gcd.getFromLocation(this.latitude, this.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                locationAreaJson.put("address", addresses.get(0).getAddressLine(0));
                locationAreaJson.put("city", addresses.get(0).getLocality()+ "  " +addresses.get(0).getSubThoroughfare() );
                locationAreaJson.put("postal", addresses.get(0).getPostalCode());

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return locationAreaJson;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    @TargetApi(Build.VERSION_CODES.M)
    public void StopUsingGPS() {

        if (locationManager != null) {
            locationManager.removeUpdates(Coordinates.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double GetLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double GetLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean CanGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

