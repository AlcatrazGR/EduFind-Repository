package codebrains.edufind.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import codebrains.edufind.R;


public class MapSearchFragment extends Fragment {

    private MapView mapView;
    private static GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map_search_fragment, container, false);

        /*
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);

        this.ConfigureGoogleMaps();
        */
        try {

                map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

                //Disable zoom buttons
                map.getUiSettings().setZoomControlsEnabled(false); // true to enable
                map.getUiSettings().setCompassEnabled(true);

                MarkerOptions marker = new MarkerOptions().position(new LatLng(0.0, 0.0)).title("Hello Maps ");
                map.addMarker(marker);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(17.385044, 78.486671)).zoom(8).build();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



                // check if map is created successfully or not
                if (map == null) {
                    //TODO: Print error message
                }

        } catch (Exception e) {
            e.printStackTrace();
        }


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Log.d("---das-ds--", "fab clicked");
            }
        });



        return view;
    }



    private void ConfigureGoogleMaps() {

        /*
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(0.0, 0.0), 4);
        map.animateCamera(cameraUpdate);

        map.addMarker(new MarkerOptions().position(new LatLng(0.0, 0.0)));
        */

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
