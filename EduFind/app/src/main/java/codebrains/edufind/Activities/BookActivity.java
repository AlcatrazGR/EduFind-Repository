package codebrains.edufind.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import codebrains.edufind.AsyncTasks.AsyncUpdateBook;
import codebrains.edufind.R;

public class BookActivity extends AppCompatActivity {

    private JSONObject oldData; //<== exei kai to username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent intent = getIntent();
        try {
            this.oldData = new JSONObject(intent.getStringExtra("data"));
            this.SetUserInterfaceData();
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : " + e);
        }
    }

    /**
     * Method that displays all the selected book data to the user interface.
     */
    private void SetUserInterfaceData() throws JSONException {

        TextView titleTv = (TextView) findViewById(R.id.textView33);
        TextView amountTv = (TextView) findViewById(R.id.textView38);
        EditText authorsEdt = (EditText) findViewById(R.id.editText7);
        EditText publisherEdt = (EditText) findViewById(R.id.editText8);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);

        titleTv.setText(this.oldData.get("title").toString());
        amountTv.setText(this.oldData.get("amount").toString());
        authorsEdt.setText(this.oldData.get("authors").toString());
        publisherEdt.setText(this.oldData.get("publisher").toString());

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(this.oldData.get("sector"))) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    /**
     * Method that adds an additional number to the amount of books.
     * @param view The view of the activity.
     */
    public void BookAmountAddition(View view) {
        TextView amountTv = (TextView) findViewById(R.id.textView38);
        int amount = Integer.parseInt(amountTv.getText().toString());
        amount += 1;
        amountTv.setText(String.valueOf(amount));
    }

    /**
     * Method that subtracts an additional number to the amount of books.
     * @param view The view of the activity.
     */
    public void BookAmountSubtraction(View view) {
        TextView amountTv = (TextView) findViewById(R.id.textView38);
        int amount = Integer.parseInt(amountTv.getText().toString());
        amount -= 1;

        if(amount <= 0)
            amount = 0;

        amountTv.setText(String.valueOf(amount));
    }

    /**
     * Event listener for the update of book information.
     * @param view The view of the activity that called the listener.
     */
    public void UpdateBookInfoProcess(View view) {

        JSONObject newData = new JSONObject();

        TextView titleTv = (TextView) findViewById(R.id.textView33);
        TextView amountTv = (TextView) findViewById(R.id.textView38);
        EditText authorsEdt = (EditText) findViewById(R.id.editText7);
        EditText publisherEdt = (EditText) findViewById(R.id.editText8);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);

        try {
            newData.put("title", titleTv.getText().toString().trim());
            newData.put("authors", authorsEdt.getText().toString().trim());
            newData.put("publisher", publisherEdt.getText().toString().trim());
            newData.put("sector", spinner.getSelectedItem().toString());
            newData.put("amount", amountTv.getText().toString());
            newData.put("username", this.oldData.get("username").toString());

            this.DisplayConfirmationDialog(this, this.oldData, newData);
            this.oldData = newData;

        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException->UpdateBookInfoProcess : " + e);
        }
    }

    /**
     * Method that displays a confirmation massage of `yes` or `no` to the user.
     * @param actObj Activity object.

     */
    public void DisplayConfirmationDialog(final Activity actObj, final JSONObject oldData,
            final JSONObject newData) throws JSONException {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirmation Message");

        // Setting Dialog Message
        alertDialog.setMessage("Do you really want to update `" + oldData.get("title") + "`");
        alertDialog.setCancelable(false);

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AsyncUpdateBook aub = new AsyncUpdateBook(actObj, oldData, newData);
                aub.execute();
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
     * Event listener for the information button, which opens on the web browser the web page
     * of the application.
     * @param view The view of the activity that called this event.
     */
    public void OpenInformationWebPage(View view) {
        Uri uri = Uri.parse("http://edufind.hol.es");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }





}
