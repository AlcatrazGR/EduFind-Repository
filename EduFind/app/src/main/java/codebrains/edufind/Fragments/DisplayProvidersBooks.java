package codebrains.edufind.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import codebrains.edufind.Adapters.ProviderCollectionAdapter;
import codebrains.edufind.Initializers.Book;
import codebrains.edufind.R;

public class DisplayProvidersBooks extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_display_providers_books, container, false);

        String[] titles = {"title1", "title2", "title3", "title4"};
        String[] sectors = {"sector1", "sector2", "sector3", "sector4"};
        String[] authors = {"authors1", "authors2", "authors3", "authors4"};
        List<Book> listBooks = new ArrayList<Book>();

        for(int i=0; i <= 3; i++) {
            Book bookObj = new Book("", titles[i], authors[i], "", sectors[i], 0);
            listBooks.add(bookObj);
        }

        ListView lv = (ListView) view.findViewById(R.id.listView);
        lv.setAdapter(new ProviderCollectionAdapter(view.getContext(), listBooks, true));


        return view;
    }



}
