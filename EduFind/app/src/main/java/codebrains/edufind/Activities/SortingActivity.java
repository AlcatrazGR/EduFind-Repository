package codebrains.edufind.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import codebrains.edufind.R;

public class SortingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);

        RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.relativeLayout2);
        RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.relativeLayout3);
        RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.relativeLayout4);
        RelativeLayout rl4 = (RelativeLayout) findViewById(R.id.relativeLayout5);

        rl2.setEnabled(false);
        rl3.setEnabled(false);
        rl4.setEnabled(false);

    }


}
