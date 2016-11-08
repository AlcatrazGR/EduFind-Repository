package codebrains.edufind.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import codebrains.edufind.Controllers.CreateAccountController;
import codebrains.edufind.Controllers.MapDistanceController;
import codebrains.edufind.R;
import codebrains.edufind.Utils.MessageCenter;

import static codebrains.edufind.Activities.StudentActivity.GetStudentGeolocationInfo;
import static codebrains.edufind.Activities.StudentActivity.SetStudentGeolocationInfo;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private JSONArray sortedByDistList;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private boolean drawerState;
    private String[] distancesArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.drawerState = false;

        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerList = (ListView) findViewById(R.id.navList);
        this.distancesArray = new String[]{"200m", "500m", "1000m", "2000m", "4000m", "6000m"};
        this.mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.distancesArray);
        this.mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                double realDistance;
                try {
                    String selectedStrDist = distancesArray[position];
                    realDistance = Double.parseDouble(selectedStrDist.replace("m", ""));
                } catch (Exception ex) {
                    Log.e("Exception! -> ", "Exception : DrawerItemClickListener ->" + ex);
                    realDistance = 200.0;
                }

                Log.d("--- Selected Dist ---", String.valueOf(realDistance));
                ConfigureGoogleMaps(realDistance);

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * Method that configures the results to be display to the map by distance selected by the user.
     */
    public void ConfigureGoogleMaps(double distance) {
        this.CalculateDistanceForMap(distance);
    }

    /**
     * Method that calls the appropriate code to calculate the distances between the user and the
     * providers.
     * @param distance The max distance area the user selected.
     */
    private void CalculateDistanceForMap(double distance) {

        try {
            if (GetStudentGeolocationInfo().get("longitude") == 0 || GetStudentGeolocationInfo().get("latitude") == 0) {
                CreateAccountController cac = new CreateAccountController();
                SetStudentGeolocationInfo(cac.HandleGeoLocationInfo(this));
            }
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : SetPointsOnMap->" + e);
            CreateAccountController cac = new CreateAccountController();
            SetStudentGeolocationInfo(cac.HandleGeoLocationInfo(this));
        }

        MapDistanceController mdc = new MapDistanceController();
        if (mdc.CalculateDistanceControl(distance)) {
            this.sortedByDistList = mdc.GetListOfProvidersSortedByDistance();
        } else {
            this.sortedByDistList = null;
        }

        this.SetPointsOnMap();
    }


    /**
     * Method that sets the pin points on the google map of the providers and also of the user.
     */
    private void SetPointsOnMap() {

        mMap.clear();

        //Set providers markers on map
        if (this.sortedByDistList != null) {
            for (int i = 0; i < this.sortedByDistList.length(); i++) {

                try {
                    JSONObject providerJSON = (JSONObject) this.sortedByDistList.get(i);

                    String title = providerJSON.get("provider") + ", " + providerJSON.get("distance") + "m";

                    LatLng point = new LatLng(Double.parseDouble(providerJSON.get("latitude").toString()),
                            Double.parseDouble(providerJSON.get("longitude").toString()));
                    mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .title(title)
                            .snippet("Call: " + providerJSON.get("number")));

                } catch (JSONException e) {
                    Log.e("Excepiton ! ->", "JSONException : SetPointsOnMap->" + e);
                    break;
                } catch (NumberFormatException e) {
                    Log.e("Excepiton ! ->", "NumberFormatException : SetPointsOnMap->" + e);
                    break;
                }
            }
        }

        //set student marker on map
        JSONObject userGeoInfo = GetStudentGeolocationInfo();
        try {
            LatLng student = new LatLng(Double.parseDouble(userGeoInfo.get("latitude").toString()),
                    Double.parseDouble(userGeoInfo.get("longitude").toString()));
            mMap.addMarker(new MarkerOptions()
                    .position(student)
                    .title("You")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(student, 12));

        } catch (JSONException | NumberFormatException e) {
            Log.e("Excepiton ! ->", " SetPointsOnMap->" + e);
        }


    }

    /**
     * Event listener for the positioning button. It re setts the geo location info of the student
     * and re sets the markers to their new position (the student if it moves).
     * @param view The view of the activity that fired this event.
     */
    public void GetNewPositionOfUser(View view) {

        try {
            CreateAccountController cac = new CreateAccountController();
            SetStudentGeolocationInfo(cac.HandleGeoLocationInfo(this));
        } catch (Exception ex) {
            Log.e("Excepiton ! ->", "Exception : GetNewPositionOfUser->" + ex);
            CreateAccountController cac = new CreateAccountController();
            SetStudentGeolocationInfo(cac.HandleGeoLocationInfo(this));
        }

        this.SetPointsOnMap();
    }


    /**
     * Event listener that opens and closes the navigation drawer and also changes a boolean
     * flag that represents the current state of the drawer (if its closed or opened).
     * @param view The view of the activity that fired this listener.
     */
    public void DrawerDisplayProcess(View view) {

        if (this.drawerState) { //if its opened
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            this.drawerState = false;
        } else { //if its closed
            mDrawerLayout.openDrawer(Gravity.LEFT);
            this.drawerState = true;
        }

    }

    /**
     * Event listener for the information button, which opens on the web browser the web page
     * of the application.
     * @param view The view of the activity that called this event.
     */
    public void OpenInformationWebPage(View view) {
        Uri uri = Uri.parse("http://edufind.hol.es");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * Method that handles the call of the selected provider.
     * @param title The title of the selected marker.
     * @param snippet The snippet of the selected marker.
     */
    private void CallProviderProcess(String title, String snippet) {

        if (!title.equals("You")) {

            String numberStr = snippet.replace("Call:", "").trim();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + numberStr));
            startActivity(callIntent);

        }
        else {
            MessageCenter msgCenter = new MessageCenter(this);
            msgCenter.DisplayErrorDialog("Call Error", "You must first select a provider in order " +
                    "to start the calling process...");
        }

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        this.ConfigureGoogleMaps(200.0);

        //Marker click listener.
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                String title = marker.getTitle();
                String snippet = marker.getSnippet();
                Log.d("-- Marker Title --", title);
                CallProviderProcess(title, snippet);

            }
        });

    }

}
