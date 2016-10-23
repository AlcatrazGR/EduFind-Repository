package codebrains.edufind.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import org.json.JSONObject;

import codebrains.edufind.Adapters.AdminExpandableListAdapter;
import codebrains.edufind.Adapters.StudentExpandableListAdapter;
import codebrains.edufind.Controllers.BookController;
import codebrains.edufind.R;
import codebrains.edufind.Utils.MessageCenter;

import static codebrains.edufind.Activities.StudentActivity.GetSortedBookList;


public class BookSearchFragment extends Fragment {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_book_search_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Log.d("---das-ds--", "fab clicked");
            }
        });



        return view;
    }


    /**
     * Method that handles the setting of the expandable list data.
     * @param mActivity The activity that called this method.
     */
    public void SetExpandableListContent(Activity mActivity) {

        JSONObject bookData = GetSortedBookList();

        BookController bc = new BookController();
        this.expListView = (ExpandableListView) mActivity.findViewById(R.id.listView);

        if(bc.SetStudentExpandableListContent(bookData, mActivity)) {
            this.listAdapter = new StudentExpandableListAdapter(mActivity, bc.GetListDataHeader(),
                    bc.GetListDataChild(), true);
            this.expListView.setAdapter(this.listAdapter);
        }
        else {
            this.listAdapter = new AdminExpandableListAdapter(mActivity, bc.GetListDataHeader(),
                    bc.GetListDataChild(), false);
            this.expListView.setAdapter(this.listAdapter);
        }


    }



}
