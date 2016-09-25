package codebrains.edufind.Controllers;

import android.app.Activity;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import codebrains.edufind.Models.CreateAccount;
import codebrains.edufind.R;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Created by Vasilhs on 9/25/2016.
 */
public class CreateAccountController {

    public CreateAccountController(){

    }

    public boolean CreateAccount(Activity mActivity, JSONObject geoInfo) {

        boolean check = false;
        CreateAccount ca = new CreateAccount();

        if(ca.CheckGeoLocationInfoJSON(mActivity, geoInfo)) {

        }


            JSONObject accountJSON = geoInfo;

            EditText usernameEdt = (EditText) mActivity.findViewById(R.id.editText);
            EditText passwordEdt = (EditText) mActivity.findViewById(R.id.editText2);
            EditText rePassedt = (EditText) mActivity.findViewById(R.id.editText3);
            EditText emailEdt = (EditText) mActivity.findViewById(R.id.editText4);
            EditText numberEdt = (EditText) mActivity.findViewById(R.id.editText5);
            EditText providerEdt = (EditText) mActivity.findViewById(R.id.editText6);

            try {
                accountJSON.put("username", usernameEdt.getText().toString());
                accountJSON.put("password", passwordEdt.getText().toString());
                accountJSON.put("repass", rePassedt.getText().toString());
                accountJSON.put("email", emailEdt.getText().toString());
                accountJSON.put("number", numberEdt.getText().toString());
                accountJSON.put("provider", providerEdt.getText().toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }






    }

}
