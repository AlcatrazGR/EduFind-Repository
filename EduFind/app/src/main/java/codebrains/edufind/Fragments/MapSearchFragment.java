package codebrains.edufind.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
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

public class MapSearchFragment extends Fragment {

    private MapView mapView;
    private static GoogleMap map;
    private JSONArray sortedByDistList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map_search_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Log.d("---das-ds--", "fab clicked");
            }
        });

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();


        this.CalculateDistanceForMap(200.0);

        return view;
    }



    private void CalculateDistanceForMap(double distance) {

        try {
            if(GetStudentGeolocationInfo().get("longitude") == 0 || GetStudentGeolocationInfo().get("latitude") == 0) {
                CreateAccountController cac = new CreateAccountController();
                SetStudentGeolocationInfo(cac.HandleGeoLocationInfo(this.getActivity()));
            }
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : SetPointsOnMap->" + e);
            CreateAccountController cac = new CreateAccountController();
            SetStudentGeolocationInfo(cac.HandleGeoLocationInfo(this.getActivity()));
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


    private void SetPointsOnMap() {

        JSONObject userGeoInfo = GetStudentGeolocationInfo();

        if(this.sortedByDistList != null || this.sortedByDistList.length() != 0) {
            for(int i = 0; i < this.sortedByDistList.length(); i++) {

                try {
                    JSONObject providerJSON = (JSONObject) this.sortedByDistList.get(i);

                    String title = "Provider: " + providerJSON.get("provider") +
                            "\nAddress: " + providerJSON.get("address") +
                            "\nNumber: " + providerJSON.get("number") +
                            "\nDistance: " + providerJSON.get("distance");

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(providerJSON.get("latitude").toString()),
                                    Double.parseDouble(providerJSON.get("longitude").toString())))
                            .title(title)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

                } catch (JSONException e) {
                    Log.e("Excepiton ! ->", "JSONException : SetPointsOnMap->" + e);
                    break;
                } catch (NumberFormatException e) {
                    Log.e("Excepiton ! ->", "NumberFormatException : SetPointsOnMap->" + e);
                    break;
                }



            }
        }









        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = null;
        try {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(userGeoInfo.get("latitude").toString()),
                    Double.parseDouble(userGeoInfo.get("longitude").toString())), 7);
        } catch (JSONException e) {
            CreateAccountController cac = new CreateAccountController();
            SetStudentGeolocationInfo(cac.HandleGeoLocationInfo(this.getActivity()));
        }


        map.animateCamera(cameraUpdate);
    }






    @Override
    public void onResume()
    {
       // mapView.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
       // mapView.onDestroy();
    }
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
       // mapView.onLowMemory();
    }

}
