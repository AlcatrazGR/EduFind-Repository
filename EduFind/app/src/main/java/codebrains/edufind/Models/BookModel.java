package codebrains.edufind.Models;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import codebrains.edufind.Initializers.Book;
import codebrains.edufind.R;
import codebrains.edufind.Utils.SystemControl;

/**
 * Model class that contains necessary methods for the book management part. this class follows
 * the MVC architecture idea.
 */
public class BookModel {

    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    //Constructor
    public BookModel() {
        this.listDataHeader = new ArrayList<String>();
        this.listDataChild = new HashMap<String, List<String>>();
    }

    /**
     * Method that retrieves all the data for the insertion of a new book.
     * @param mActivity The avtivity that called this method.
     * @return Returns a json array that holds all the data.
     */
    public JSONObject GetAddNewBookFormData(Activity mActivity, JSONObject userdata) {

        JSONObject newBookData = new JSONObject();

        EditText titleEdt = (EditText) mActivity.findViewById(R.id.book_title);
        EditText authorsEdt = (EditText) mActivity.findViewById(R.id.book_authors);
        EditText publisherEdt = (EditText) mActivity.findViewById(R.id.book_publisher);
        TextView amountTv = (TextView) mActivity.findViewById(R.id.book_amount);

        Spinner spinner = (Spinner) mActivity.findViewById(R.id.spinner);

        try {
            newBookData.put("username", userdata.get("username").toString());
            newBookData.put("title", titleEdt.getText().toString().trim());
            newBookData.put("authors", authorsEdt.getText().toString().trim());
            newBookData.put("publisher", publisherEdt.getText().toString().trim());
            newBookData.put("sector", spinner.getSelectedItem().toString());
            newBookData.put("amount", Integer.parseInt(amountTv.getText().toString().trim()));
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "getStringExtra : " + e);
            return null;
        }

        return newBookData;
    }

    /**
     * Method that processes the response from the server and initializes a list of books in
     * order to be displayed to the listview.
     * @param bookArray The array of all the provider's books.
     * @return Returns a list of initialized books to be displayed to the listview
     * @throws JSONException Throws a json exception if an error occurres while processing json data.
     */
    public List<Book> SetProviderBookList(JSONArray bookArray) throws JSONException {

        List<Book> bookList = new ArrayList<Book>();

        for(int i = 0; i < bookArray.length(); i++) {

            JSONObject bookJSON = (JSONObject) bookArray.get(i);
            Book book = new Book("", bookJSON.get("title").toString(), bookJSON.get("authors").toString(),
                    bookJSON.get("publisher").toString(), bookJSON.get("sector").toString(),
                    Integer.parseInt(bookJSON.get("amount").toString()));

            bookList.add(book);
        }

        return bookList;
    }

    /**
     * Method that sets an empty array list to be displayed to the list view.
     * @return Returns a list with one row.
     */
    public List<Book> SetEmptyProviderBookList(String message) {

        List<Book> bookList = new ArrayList<Book>();
        Book book = new Book("", message, "", "", "", 0);
        bookList.add(book);

        return bookList;
    }

    /**
     * Method that sets the expandable list view of the student for none empty response.
     * @param data The response data of the server.
     * @param mActivity The activity that called this method.
     * @return Returns a boolean stating the result of the process.
     * @throws JSONException exception that is fired whenever an error occurs while handling json data.
     */
    public boolean SetNoneEmptyExpandableListContent(JSONObject data, Activity mActivity) throws JSONException {

        SystemControl sc = new SystemControl(mActivity);

        JSONArray usersArray = (JSONArray) data.get("users");
        for(int i = 0; i < usersArray.length(); i++) {

            JSONObject user = usersArray.getJSONObject(i);

            JSONArray booksArray = (JSONArray) user.get("books");
            for(int j = 0; j < booksArray.length(); j++) {

                JSONObject book = booksArray.getJSONObject(j);
                List<String> item = new ArrayList<String>();
                item.add("Publisher: "+sc.ConvertUTF8EncodedStringToReadable(book.get("publisher").toString()));
                item.add("Authors: "+sc.ConvertUTF8EncodedStringToReadable(book.get("authors").toString()));
                item.add("Sector: "+sc.ConvertUTF8EncodedStringToReadable(book.get("sector").toString()));
                item.add("Provider: "+sc.ConvertUTF8EncodedStringToReadable(user.get("name").toString()));
                item.add("Address: "+sc.ConvertUTF8EncodedStringToReadable(user.get("address").toString()));
                item.add("Amount: " + book.get("amount").toString());

                this.listDataHeader.add(sc.ConvertUTF8EncodedStringToReadable( book.get("title").toString() ));
                this.listDataChild.put(sc.ConvertUTF8EncodedStringToReadable(book.get("title").toString()), item);
            }
        }

        if(this.listDataChild == null || this.listDataChild == null)
            return false;

        return true;
    }

    /**
     * Method that sets an empty expandable list.
     * @param data The response of the server. Contains status=2 and a message stating that there was no results.
     * @return Returns a boolean stating the status of the process.
     * @throws JSONException Exception that is fired whenever an error occurs while handling json data.
     */
    public boolean SetEmptyExpandableListContent(JSONObject data) throws JSONException {

        List<String> item = new ArrayList<String>();
        item.add(data.get("message").toString());

        this.listDataHeader.add("Empty List ...");
        this.listDataChild.put("Empty List ...", item);

        if(this.listDataChild == null || this.listDataChild == null)
            return false;

        return true;
    }

    /**
     * Returns the private list data header.
     * @return Returns the list of header for the expandable list.
     */
    public List<String> GetListDataHeader() {
        return this.listDataHeader;
    }

    /**
     * Method that returns the list data child for the expandable list.
     * @return Returns the child data of the expandable list.
     */
    public HashMap<String, List<String>> GetListDataChild() {
        return this.listDataChild;
    }


}
