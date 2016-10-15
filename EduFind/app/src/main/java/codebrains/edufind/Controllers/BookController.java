package codebrains.edufind.Controllers;

import android.app.Activity;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import codebrains.edufind.Initializers.Book;
import codebrains.edufind.Models.BookModel;

/**
 * Controller class that handles the calling of all the necessary methods in order to finish
 * the book addition process. This class follows the MVC architecture idea.
 */
public class BookController {

    private JSONObject newBookData;

    //Constructor
    public BookController() {
        this.newBookData = new JSONObject();
    }

    /**
     * Control method for the book addition process.
     * @param mActivity The activity that called the event.
     * @return Returns a boolean that represents the status of the process.
     */
    public boolean BookAdditionProcess(Activity mActivity, JSONObject userdata) {

        BookModel bm = new BookModel();
        this.newBookData = bm.GetAddNewBookFormData(mActivity, userdata);

        if(this.newBookData == null)
            return false;

        return true;
    }

    /**
     * Method that handles the setting of all the data that will later be displayed to the book list
     * view.
     * @param bookData A json object result from the response of the server.
     * @return Returns a json object which contains the info to be displayed.
     */
    public JSONObject BookListData(JSONObject bookData) {

        try {
            int status = Integer.parseInt(bookData.get("status").toString());
            BookModel bm = new BookModel();
            List<Book> booksList = null;
            boolean flag = false;

            switch (status) {

                //Case the response contains books.
                case 1:
                    JSONArray booksArray = (JSONArray) bookData.get("books");
                    booksList = bm.SetProviderBookList(booksArray);
                    flag = true;
                break;

                //Case the response doesn't contain any books.
                case 2:
                    booksList = bm.SetEmptyProviderBookList(bookData.get("message").toString());
                    flag = false;
                break;

            }

            if(booksList != null) {
                JSONObject result = new JSONObject();
                result.put("status", flag);
                result.put("list", booksList);

                return result;
            }

        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException -> BookListData : " + e);
            return null;
        }

        return null;
    }

    /**
     * Method that returns the initialized book json.
     * @return Returns the private book json object.
     */
    public JSONObject GetNewBookJson() {
        return this.newBookData;
    }

}
