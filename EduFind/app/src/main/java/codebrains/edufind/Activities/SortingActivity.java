package codebrains.edufind.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import codebrains.edufind.R;
import codebrains.edufind.Utils.MessageCenter;

public class SortingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
    }

    /**
     * Event listener onClick which is fired whenever the `Submit` button is pressed and it handles
     * the process of setting the sorting data and returning them to the main activity.
     * @param view The view of the activity that fired this event.
     */
    public void SortingProcess(View view){

        MessageCenter msgCenter = new MessageCenter(this);

        RadioButton rd1 = (RadioButton) findViewById(R.id.radioButton);
        RadioButton rd2 = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton rd3 = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton rd4 = (RadioButton) findViewById(R.id.radioButton4);

        if(rd1.isChecked()) {

            EditText areaSortEdt = (EditText) findViewById(R.id.editText9);
            String areaToSort = areaSortEdt.getText().toString().trim();

            if(this.EmptySortingFieldCheck(areaToSort)) {
                String code = "0";
                Intent output = new Intent();
                output.putExtra("sortingMethod", code);
                output.putExtra("sortingValue", areaToSort);
                output.putExtra("sortingKey", "city");
                setResult(RESULT_OK, output);
                finish();
            }
            else {
                msgCenter.DisplayErrorDialog("Sorting Error", "The `city` sorting field is empty! " +
                        "Please fill the necessary field and then resubmit ...");
            }
        }
        else if (rd2.isChecked()) {

            Spinner spinner = (Spinner) findViewById(R.id.spinner3);
            String sectorToSort = spinner.getSelectedItem().toString();

            String code = "1";
            Intent output = new Intent();
            output.putExtra("sortingMethod", code);
            output.putExtra("sortingValue", sectorToSort);
            output.putExtra("sortingKey", "sector");
            setResult(RESULT_OK, output);
            finish();

        }
        else if (rd3.isChecked()) {

            EditText publisherSortEdt = (EditText) findViewById(R.id.editText10);
            String publisherToSort = publisherSortEdt.getText().toString().trim();

            if(this.EmptySortingFieldCheck(publisherToSort)) {
                String code = "2";
                Intent output = new Intent();
                output.putExtra("sortingMethod", code);
                output.putExtra("sortingValue", publisherToSort);
                output.putExtra("sortingKey", "publisher");
                setResult(RESULT_OK, output);
                finish();
            }
            else {
                msgCenter.DisplayErrorDialog("Sorting Error", "The `publisher` sorting field is empty! " +
                        "Please fill the necessary field and then resubmit ...");
            }

        }
        else {

            EditText titleSortEdt = (EditText) findViewById(R.id.editText11);
            String titleToSort = titleSortEdt.getText().toString().trim();

            if(this.EmptySortingFieldCheck(titleToSort)) {
                String code = "3";
                Intent output = new Intent();
                output.putExtra("sortingMethod", code);
                output.putExtra("sortingValue", titleToSort);
                output.putExtra("sortingKey", "title");
                setResult(RESULT_OK, output);
                finish();
            }
            else {
                msgCenter.DisplayErrorDialog("Sorting Error", "The `title` sorting field is empty! " +
                        "Please fill the necessary field and then resubmit ...");
            }

        }

    }

    /**
     * Method that checks if the sorting value entered is empty.
     * @param value The sorting value entered by the user.
     * @return Returns a boolean flag representing the status of the check.
     */
    private boolean EmptySortingFieldCheck(String value) {

        if(value == "" || value == null)
            return false;

        return true;
    }



}
