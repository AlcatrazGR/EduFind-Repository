package codebrains.edufind.Interfaces;

import android.app.Activity;

import org.json.JSONObject;


/**
 * Interface that is used to gain the result of an async task.
 */
public interface  IAsyncResponse {

    void ProcessFinish(JSONObject output, Activity mActivity);
}
