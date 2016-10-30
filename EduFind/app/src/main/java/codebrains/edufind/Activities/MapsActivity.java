package codebrains.edufind.Activities;

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
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.Controllers.CreateAccountController;
import codebrains.edufind.Controllers.MapDistanceController;
import codebrains.edufind.R;
import static codebrains.edufind.Activities.StudentActivity.GetStudentGeolocationInfo;
import static codebrains.edufind.Activities.StudentActivity.SetStudentGeolocationInfo;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private JSONArray sortedByDistList;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private boolean drawerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.drawerState = false;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navList);
        String[] osArray = { "200m", "500m", "1000m", "2000m", "3000m", "4000m", "5000m" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    /**
     * Method that configures the results to be display to the map by distance selected by the user.
     */
    public void ConfigureGoogleMaps() {
        this.CalculateDistanceForMap(200.0);
    }

    /**
     * Method that calls the appropriate code to calculate the distances between the user and the
     * providers.
     * @param distance The max distance area the user selected.
     */
    private void CalculateDistanceForMap(double distance) {

        try {
            if(GetStudentGeolocationInfo().get("longitude") == 0 || GetStudentGeolocationInfo().get("latitude") == 0) {
                CreateAccountController cac = new CreateAccountController();
                SetStudentGeolocationInfo(cac.HandleGeoLocationInfo(this));
            }
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : SetPointsOnMap->" + e);
            CreateAccountController cac = new CreateAccountController();
            SetStudentGeolocationInfo(cac.HandleGeoLocationInfo(this));
        }

        MapDistanceController mdc = new MapDistanceController();
        if(mdc.CalculateDistanceControl(distance)) {
            this.sortedByDistList = mdc.GetListOfProvidersSortedByDistance();
        }
        else {
            this.sortedByDistList = null;
        }

        this.SetPointsOnMap();
    }


    /**
     * Method that sets the pin points on the google map of the providers and also of the user.
     */
    private void SetPointsOnMap() {

        //Set providers markers on map
        if(this.sortedByDistList != null || this.sortedByDistList.length() != 0) {
            for(int i = 0; i < this.sortedByDistList.length(); i++) {

                try {
                    JSONObject providerJSON = (JSONObject) this.sortedByDistList.get(i);

                    String title = "Provider: " + providerJSON.get("provider");
                            //"\nAddress: " + providerJSON.get("address") +
                            //"\nNumber: " + providerJSON.get("number") +
                            //"\nDistance: " + providerJSON.get("distance");

                    LatLng point = new LatLng(Double.parseDouble(providerJSON.get("latitude").toString()),
                            Double.parseDouble(providerJSON.get("longitude").toString()));
                    mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .title(title));

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



    public void DrawerDisplayProcess(View view) {

        if(this.drawerState) { //if its opened
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            this.drawerState = false;
        }
        else { //if its closed
            mDrawerLayout.openDrawer(Gravity.LEFT);
            this.drawerState = true;
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

        this.ConfigureGoogleMaps();
    }

}
