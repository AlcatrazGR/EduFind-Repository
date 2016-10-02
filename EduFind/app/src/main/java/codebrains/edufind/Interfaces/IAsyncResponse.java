package codebrains.edufind.Interfaces;

import org.json.JSONObject;


/**
 * Interface that is used to gain the result of an async task.
 */
public interface  IAsyncResponse {

    void processFinish(JSONObject output);
}
