package codebrains.edufind.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import codebrains.edufind.R;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_fragment, container, false);
        return view;
    }

    public void LoginToSystem(Activity mActivity) throws JSONException {

        EditText usernameEdt = (EditText) mActivity.findViewById(R.id.usernameEdt);
        EditText passwordEdt = (EditText) mActivity.findViewById(R.id.passwordEdt);

        JSONObject loginJSON = new JSONObject();
        loginJSON.put("username", usernameEdt.getText().toString().trim());
        loginJSON.put("password", passwordEdt.getText().toString().trim());



    }

}
