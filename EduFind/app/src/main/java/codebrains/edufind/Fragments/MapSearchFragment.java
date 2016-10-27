package codebrains.edufind.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import codebrains.edufind.Controllers.CreateAccountController;
import codebrains.edufind.R;
import codebrains.edufind.Utils.Coordinates;

import static codebrains.edufind.Activities.StudentActivity.GetStudentGeolocationInfo;
import static codebrains.edufind.Activities.StudentActivity.SetStudentGeolocationInfo;

public class MapSearchFragment extends Fragment {

    private MapView mapView;
    private static GoogleMap map;

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


        this.SetPointsOnMap(200.0);

        return view;
    }



    private void SetPointsOnMap(double distance) {


        JSONObject userGeoInfo = GetStudentGeolocationInfo();
        try {
            if(userGeoInfo.get("longitude") == 0 || userGeoInfo.get("latitude") == 0) {
                CreateAccountController cac = new CreateAccountController();
                userGeoInfo = cac.HandleGeoLocationInfo(this.getActivity());
                SetStudentGeolocationInfo(userGeoInfo);
            }
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : SetPointsOnMap->" + e);
            CreateAccountController cac = new CreateAccountController();
            userGeoInfo = cac.HandleGeoLocationInfo(this.getActivity());
            SetStudentGeolocationInfo(userGeoInfo);
        }


        CalculateDistancesController cdc = new CalculateDistancesController();

        cdc.ControlMethodForCalculatingDistance(output, coordinates.GetLongitude(),
                coordinates.GetLatitude(), spinner.getSelectedItem().toString());
        JSONArray jsonArray = cdc.GetArrayOfProductInArea();

        try {
            this.DisplayMarkersForProductsOnArea(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(coordinates.GetLatitude(),
                coordinates.GetLongitude()), 10);
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
