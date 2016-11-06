package codebrains.edufind.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import codebrains.edufind.R;
import codebrains.edufind.Utils.MessageCenter;

public class SortingActivity extends AppCompatActivity {

    private RadioButton rd1;
    private RadioButton rd2;
    private RadioButton rd3;
    private RadioButton rd4;

    private Spinner spinner;
    private EditText areaSortEdt;
    private EditText publisherSortEdt;
    private EditText titleSortEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);

        this.rd1 = (RadioButton) findViewById(R.id.radioButton);
        this.rd2 = (RadioButton) findViewById(R.id.radioButton2);
        this.rd3 = (RadioButton) findViewById(R.id.radioButton3);
        this.rd4 = (RadioButton) findViewById(R.id.radioButton4);

        this.spinner = (Spinner) findViewById(R.id.spinner3);
        this.areaSortEdt = (EditText) findViewById(R.id.editText9);
        this.publisherSortEdt = (EditText) findViewById(R.id.editText10);
        this.titleSortEdt = (EditText) findViewById(R.id.editText11);

        //By default the enable one is the sorting by area.
        this.areaSortEdt.setEnabled(true);
        this.publisherSortEdt.setEnabled(false);
        this.titleSortEdt.setEnabled(false);
        this.spinner.setEnabled(false);

    }

    /**
     * Event listener that is fired whenever a radio button is checked and it enables or disable
     * all the elements that are not connected with the selected radio button (sorting method).
     * @param view The view of the activity that fired this event.
     */
    public void EnableDisableFields(View view) {

        if(rd1.isChecked()) {
            this.areaSortEdt.setEnabled(true);
            this.publisherSortEdt.setEnabled(false);
            this.titleSortEdt.setEnabled(false);
            this.spinner.setEnabled(false);
        }
        else if (rd2.isChecked()) {
            this.areaSortEdt.setEnabled(false);
            this.publisherSortEdt.setEnabled(false);
            this.titleSortEdt.setEnabled(false);
            this.spinner.setEnabled(true);
        }
        else if (rd3.isChecked()) {
            this.areaSortEdt.setEnabled(false);
            this.publisherSortEdt.setEnabled(true);
            this.titleSortEdt.setEnabled(false);
            this.spinner.setEnabled(false);
        }
        else if (rd4.isChecked()) {
            this.areaSortEdt.setEnabled(false);
            this.publisherSortEdt.setEnabled(false);
            this.titleSortEdt.setEnabled(true);
            this.spinner.setEnabled(false);
        }

    }

    /**
     * Event listener onClick which is fired whenever the `Submit` button is pressed and it handles
     * the process of setting the sorting data and returning them to the main activity.
     * @param view The view of the activity that fired this event.
     */
    public void SortingProcess(View view){

        MessageCenter msgCenter = new MessageCenter(this);

        if(rd1.isChecked()) {

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
