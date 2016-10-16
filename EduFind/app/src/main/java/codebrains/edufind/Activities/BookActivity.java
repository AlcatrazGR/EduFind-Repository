package codebrains.edufind.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.R;

public class BookActivity extends AppCompatActivity {

    private JSONObject oldData;

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





}
