package codebrains.edufind.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import codebrains.edufind.Adapters.ProviderTabsAdapter;
import codebrains.edufind.AsyncTasks.AsyncGetProviderBooks;
import codebrains.edufind.Controllers.BookController;
import codebrains.edufind.Fragments.DisplayProvidersBooks;
import codebrains.edufind.Fragments.InsertBookFragment;
import codebrains.edufind.Fragments.ProvidersProfileFragment;
import codebrains.edufind.Initializers.Book;
import codebrains.edufind.Interfaces.IAsyncResponse;
import codebrains.edufind.R;
import codebrains.edufind.Utils.MessageCenter;

public class ProviderActivity extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener, IAsyncResponse {

    //Tab objects
    private ViewPager tabsviewPager;
    private ProviderTabsAdapter mTabsAdapter;
    private ActionBar actionBar;

    //Fragment objects
    private ProvidersProfileFragment ppf;
    private InsertBookFragment ibf;
    private DisplayProvidersBooks dpb;

    private static JSONObject userData;
    private static JSONObject bookList;
    private static int selectedItemPos;

    private int bookAmount;
    private boolean refreshFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        Intent intent = getIntent();
        try {
            this.userData = new JSONObject(intent.getStringExtra("userdata"));

            //Get book list data.
            JSONObject retDataJson = new JSONObject();
            retDataJson.put("username", this.userData.get("username").toString());
            retDataJson.put("process", 1);
            AsyncGetProviderBooks agpb = new AsyncGetProviderBooks(this, retDataJson);
            agpb.delegate = this;
            agpb.execute();

        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException->SetBookList : " + e);
        }

        this.bookAmount = 0;

        //Initializing the fragment data
        this.ppf = new ProvidersProfileFragment();
        this.ibf = new InsertBookFragment();
        this.dpb = new DisplayProvidersBooks();

        //Initializing the tab view of this activity
        tabsviewPager = (ViewPager) findViewById(R.id.tabspager);
        mTabsAdapter = new ProviderTabsAdapter(getSupportFragmentManager());
        tabsviewPager.setAdapter(mTabsAdapter);
        actionBar = getSupportActionBar();

        this.getSupportActionBar().setHomeButtonEnabled(false);
        this.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab profileTab = getSupportActionBar().newTab()
                .setText(" Profile")
                .setTabListener(this);

        ActionBar.Tab addBookTab = getSupportActionBar().newTab()
                .setText(" Add")
                .setTabListener(this);

        ActionBar.Tab displayBookTab = getSupportActionBar().newTab()
                .setText(" Display")
                .setTabListener(this);

        getSupportActionBar().addTab(profileTab);
        getSupportActionBar().addTab(addBookTab);
        getSupportActionBar().addTab(displayBookTab);

        //Event listener that runs whenever the tabs are swiped, it is used to make enable the
        //respective tab.
        tabsviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position); //Enable the respective tab.
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    /**
     * Event listener for the geolocation button.
     * @param view The view of the activity that fired the event.
     */
    public void GetGeolocationOfUser(View view) {
        this.ppf.GetGeolocationOfUser(this);
    }

    /**
     * Event listener for the update providers profile button.
     * @param view The view of the activity that fired the event.
     */
    public void UpdateProvidersProfile(View view) {
        this.ppf.UpdateProvidersProfile(this);
    }

    /**
     * Event listener for the delete providers profile button.
     * @param view The view of the activity that fired the event.
     */
    public void DeleteProvidersProfile(View view) {
        this.ppf.DeleteProvidersProfile(this);
    }

    /**
     * Event listener on click of the refresh button in the book list fragment.
     * @param view The view of the activity that called this event.
     */
    public void RefreshProviderBookListEvent(View view) {
        this.refreshFlag = true;
        JSONObject retDataJson = new JSONObject();
        try {
            retDataJson.put("username", this.userData.get("username").toString());
            retDataJson.put("process", 1);
            AsyncGetProviderBooks agpb = new AsyncGetProviderBooks(this, retDataJson);
            agpb.delegate = this;
            agpb.execute();
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException->SetBookList : " + e);
        }
    }

    /**
     * Method that calls the get provider books asynchronous task.
     * @param mActivity The activity that called this event.
     * @param data The data to be send to the server.
     */
    public void CallAsyncForDelete(Activity mActivity, JSONObject data) {

        this.refreshFlag = true;
        try {
            data.put("process", 4);

            Log.d("Data to be sent --", data.toString());

            AsyncGetProviderBooks agpb = new AsyncGetProviderBooks(mActivity, data);
            agpb.delegate = this;
            agpb.execute();
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException->SetBookList : " + e);
        }
    }

    /**
     * Event listener that handles thee deletion of a selected book in the list view.
     * @param view The view of the activity that fired this event.
     */
    public void DeleteProviderBook(View view) {

        Log.d("-- event -- ", "Inside event");

        JSONObject listBooksJSON = GetBookListData();
        try {

            List<Book> bookList = (List<Book>) listBooksJSON.get("list");

            if(GetSelectedItemPosition() != -1) {

                Book selectedBook = bookList.get(GetSelectedItemPosition());
                JSONObject bookData = new JSONObject();
                bookData.put("username", GetUserData().get("username"));
                bookData.put("title", selectedBook.GetTitle());
                bookData.put("authors", selectedBook.GetAuthors());
                bookData.put("sector", selectedBook.GetSector());
                this.refreshFlag = true;

                this.DisplayConfirmationDialog(this, bookData);
            }
            else {
                MessageCenter msgcenter = new MessageCenter(this);
                msgcenter.DisplayErrorDialog("Deletion Error", "You must select one item in order " +
                        "to proceed to deletion!");
            }
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException->DeleteProviderBook : " + e);
        }

    }

    /**
     * Method that displays a confirmation massage of `yes` or `no` to the user.
     * @param actObj Activity object.
     * @param data The json object with all the users info.
     */
    public void DisplayConfirmationDialog(final Activity actObj, final JSONObject data) throws JSONException {

        Log.d("-- event -- ", "Inside display");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirmation Message");

        // Setting Dialog Message
        alertDialog.setMessage("Do you really want to delete `" + data.get("title") + "` from " +
                "your list ?");

        alertDialog.setCancelable(false);
        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CallAsyncForDelete(actObj, data);
            }
        });

        // On pressing Settings button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Showing Alert Message
        alertDialog.show();

    }


    /**
     * Method that adds an additional number to the amount of books.
     * @param view The view of the activity.
     */
    public void BookAmountAddition(View view) {
        TextView amountTv = (TextView) findViewById(R.id.book_amount);
        this.bookAmount += 1;
        amountTv.setText(String.valueOf(this.bookAmount));
    }

    /**
     * Method that subtracts an additional number to the amount of books.
     * @param view The view of the activity.
     */
    public void BookAmountSubtraction(View view) {
        TextView amountTv = (TextView) findViewById(R.id.book_amount);
        this.bookAmount -= 1;

        if(this.bookAmount <= 0)
            this.bookAmount = 0;

        amountTv.setText(String.valueOf(this.bookAmount));
    }

    /**
     * Method that calls the appropriate methods to set the book list of a provider and then
     * save the whole data in a static variable to later be used to the list view.
     * @param output The response of the server containing all the providers books.
     */
    private void ProcessProviderBookList(JSONObject output) {

        BookController bc = new BookController();
        JSONObject result = bc.BookListData(output);
        bookList = result;

        if(this.refreshFlag)
            this.dpb.RefreshProviderBookList(this);
    }

    /**
     * Event that is called whenever the submit button on the add book form is pressed.
     * @param view The view of the activity that called the event.
     */
    public void AddBookEvent(View view) {
        this.ibf.AddNewBookProcess(this, GetUserData());
    }

    /**
     * Method that returns the providers information to all the available tabs (its static).
     * @return Returns the providers account information.
     */
    public static JSONObject GetUserData() {
        return userData;
    }

    /**
     * Method that sets the providers account info.
     * @param newUserData The new account info of the provider.
     */
    public static void SetUserData(JSONObject newUserData) {
        userData = newUserData;
    }

    /**
     * Method that return the private static json booklist.
     * @return Return the private booklist json.
     */
    public static JSONObject GetBookListData() {
        return bookList;
    }

    /**
     * Method that return the private static integer item position
     * @return Return the private item position.
     */
    public static int GetSelectedItemPosition() {
        return selectedItemPos;
    }

    /**
     * Method that return the private static item position.
     * @return Return the private item position.
     */
    public static void SetSelectedItemPosition(int pos) {
        selectedItemPos = pos;
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        tabsviewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        tabsviewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void ProcessFinish(JSONObject output, Activity mActivity) {
        this.ProcessProviderBookList(output);
    }

}
