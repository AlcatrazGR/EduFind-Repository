package codebrains.edufind.Models;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import codebrains.edufind.R;

/**
 * Model class that contains necessary methods for the book management part. this class follows
 * the MVC architecture idea.
 */
public class BookModel {

    //Constructor
    public BookModel() {

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

}
