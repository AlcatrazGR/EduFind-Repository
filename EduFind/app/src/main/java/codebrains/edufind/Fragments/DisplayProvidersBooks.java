package codebrains.edufind.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import codebrains.edufind.Adapters.ProviderCollectionAdapter;
import codebrains.edufind.AsyncTasks.AsyncGetProviderBooks;
import codebrains.edufind.Initializers.Book;
import codebrains.edufind.R;
import static codebrains.edufind.Activities.ProviderActivity.GetBookListData;

public class DisplayProvidersBooks extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_display_providers_books, container, false);

        JSONObject listBooksJSON = GetBookListData();
        ListView listView = (ListView) view.findViewById(R.id.listView);

        try {
            List<Book> bookList = (List<Book>) listBooksJSON.get("list");
            ProviderCollectionAdapter pca =  new ProviderCollectionAdapter(view.getContext(), bookList,
                    (Boolean) listBooksJSON.get("status"));
            listView.setAdapter(pca);
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : " + e);
        }

        return view;
    }

    /**
     * Method that refreshes the book collection list of the provider and displaying the new
     * data to it.
     * @param mActivity The activity that called this method.
     */
    public void RefreshProviderBookList(Activity mActivity) {

        JSONObject listBooksJSON = GetBookListData();
        ListView listView = (ListView) mActivity.findViewById(R.id.listView);

        try {
            List<Book> bookList = (List<Book>) listBooksJSON.get("list");
            ProviderCollectionAdapter pca = new ProviderCollectionAdapter(mActivity, bookList,
                    (Boolean) listBooksJSON.get("status"));
            listView.setAdapter(pca);
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : " + e);
        }

    }

}
