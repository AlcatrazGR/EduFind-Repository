package codebrains.edufind.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.Adapters.AdminExpandableListAdapter;
import codebrains.edufind.Adapters.StudentExpandableListAdapter;
import codebrains.edufind.AsyncTasks.AsyncBookSearch;
import codebrains.edufind.Controllers.BookController;
import codebrains.edufind.Controllers.CreateAccountController;
import codebrains.edufind.Interfaces.IAsyncResponse;
import codebrains.edufind.R;

public class StudentActivity extends AppCompatActivity implements IAsyncResponse {

    private String sortingValue;
    private int sortingMethod;
    /* 0 -> by area
     * 1 -> by sector
     * 2 -> by publisher
     * 3 -> by title
     */

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    private static JSONObject studentGeo;
    private static JSONObject sortedList; //It can either be 1 or 2

    private JSONObject lastSortingPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //Setting new geolocation info for the student.
        CreateAccountController cac = new CreateAccountController();
        studentGeo = cac.HandleGeoLocationInfo(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("process", 3);
            jsonObject.put("shCode", 0);

            this.lastSortingPref = jsonObject;
            this.lastSortingPref.put("citySortFlag", true);
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : " + e);
        }
        this.CallBookSearchAsyncTask(jsonObject, true);

        this.sortingMethod = 0;
        try {
            this.sortingValue = this.studentGeo.get("city").toString();
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : OnCreate->" + e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            this.sortingValue = data.getStringExtra("sortingValue");
            String sortingKey = data.getStringExtra("sortingKey");
            try {
                this.sortingMethod = Integer.parseInt(data.getStringExtra("sortingMethod"));
            }
            catch(NumberFormatException ex) {
                Log.e("Exception! ->", "NumberFormatException : onActivityResult ->"+ex);
                this.sortingMethod = 0;
            }

            //Build json for service call.
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("process", 3);
                jsonObject.put("shCode", this.sortingMethod);
                jsonObject.put(sortingKey, this.sortingValue);

                this.lastSortingPref = jsonObject;
                this.lastSortingPref.put("citySortFlag", false);
            } catch (JSONException e) {
                Log.e("Excepiton ! ->", "JSONException : onActivityResult->" + e);
            }
            this.CallBookSearchAsyncTask(jsonObject, false);

        }

    }

    /**
     * Method that opens the activity search of books.
     * @param view The view of the activity that called this method.
     */
    public void BookSortingProcess(View view) {
        Intent sortingIntent = new Intent(this, SortingActivity.class);
        startActivityForResult(sortingIntent, 2);
    }

    /**
     * Method that calls the asynchronous task that sets the new sorted book data.
     * @param data A json object containing the necessary data and sorting codes to determine the process to follow.
     */
    private void CallBookSearchAsyncTask(JSONObject data, boolean citySortFlag) {
        AsyncBookSearch abs = new AsyncBookSearch(this, data, citySortFlag);
        abs.delegate = this;
        abs.execute();
    }

    /**
     * Event listener for the button in the book search activity that opens the activity of the
     * google maps.
     * @param view The view of the activity that called this event.
     */
    public void OpenGoogleMaps(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        this.startActivity(intent);
    }

    /**
     * Method that handles the setting of the expandable list data.
     */
    public void SetExpandableListContent() {

        JSONObject bookData = GetSortedBookList();

        BookController bc = new BookController();
        this.expListView = (ExpandableListView) this.findViewById(R.id.listView);

        if(bc.SetStudentExpandableListContent(bookData, this)) {
            this.listAdapter = new StudentExpandableListAdapter(this, bc.GetListDataHeader(),
                    bc.GetListDataChild(), true);
            this.expListView.setAdapter(this.listAdapter);
        }
        else {
            this.listAdapter = new AdminExpandableListAdapter(this, bc.GetListDataHeader(),
                    bc.GetListDataChild(), false);
            this.expListView.setAdapter(this.listAdapter);
        }


    }

    /**
     * Event lister for the refresh floating button on the bottom right side of teh screen.
     * It makes a http request to the server to retrieve books with the same sorting method
     * that was previously selected by the user.
     * @param view The view of the activity that called this event.
     */
    public void RefreshSearchBooksList(View view) {

        boolean citySortFlag = true;
        try {
            citySortFlag = (boolean) this.lastSortingPref.get("citySortFlag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.CallBookSearchAsyncTask(this.lastSortingPref, citySortFlag);

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
     * Method that sets the static book list json object.
     * @param newList The new json sorted book list that will replace the old one.
     */
    public static void SetSortedBookList(JSONObject newList) {
        sortedList = newList;
    }

    /**
     * Method that gets the static json book list object.
     * @return Returns a json tha contains all the info for the sorted book list.
     */
    public static JSONObject GetSortedBookList() {
        return sortedList;
    }

    /**
     * Method that sets the static student geolocation info JSON Object
     * @param newGeo The new geo json that will replace the current.
     */
    public static void SetStudentGeolocationInfo(JSONObject newGeo) {
        studentGeo = newGeo;
    }

    /**
     * Method that retrieves the static student's geolocation info
     * @return Returns the student geolocation information.
     */
    public static JSONObject GetStudentGeolocationInfo() {
        return studentGeo;
    }

    @Override
    public void ProcessFinish(JSONObject output, Activity mActivity) {

        SetSortedBookList(output); //sets the new sorted book list.
        this.SetExpandableListContent();

    }


}

