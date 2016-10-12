package codebrains.edufind.Fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONObject;
import codebrains.edufind.R;

public class InsertBookFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_insert_book_fragment, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * Method that handles the addintion of a new book into the database by calling the
     * responsible async task.
     * @param mActivity The activity that called the event.
     * @param userdata The json object that holds all the necessary information.
     */
    public void AddNewBookProcess(Activity mActivity, JSONObject userdata){




    }






}
