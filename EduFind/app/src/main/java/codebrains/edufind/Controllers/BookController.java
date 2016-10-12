package codebrains.edufind.Controllers;

import android.app.Activity;

import org.json.JSONObject;

import codebrains.edufind.Models.BookModel;

/**
 * Controller class that handles the calling of all the necessary methods in order to finish
 * the book addition process. This class follows the MVC architecture idea.
 */
public class BookController {

    //Constructor
    public BookController() {

    }

    /**
     * Control method for the book addition process.
     * @param mActivity The activity that called the event.
     * @return Returns a boolean that represents the status of the process.
     */
    public boolean BookAdditionProcess(Activity mActivity, JSONObject userdata) {

        BookModel bm = new BookModel();
        JSONObject newBookData = bm.GetAddNewBookFormData(mActivity, userdata);

        if(newBookData == null)
            return false;

        return true;
    }

}
