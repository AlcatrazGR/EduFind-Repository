package codebrains.edufind.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import codebrains.edufind.Initializers.Book;
import codebrains.edufind.R;
import android.view.View.OnClickListener;

/**
 * Custom adapter class that processes all the inserted book registries of the user and then
 * displays them on the list view element.
 */
public class ProviderCollectionAdapter extends ArrayAdapter<Book> {

    private final Context context;
    private final List<Book> valuesArr;

    public ProviderCollectionAdapter(Context context, List<Book> values) {
        super(context, R.layout.provider_collection_list, values);
        this.context = context;
        this.valuesArr = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.provider_collection_list, parent, false);

        TextView titleTv = (TextView) rowView.findViewById(R.id.textView28);
        TextView sectorTv = (TextView) rowView.findViewById(R.id.textView29);
        TextView authorsTv = (TextView) rowView.findViewById(R.id.textView30);

        titleTv.setText(this.valuesArr.get(position).GetTitle());
        sectorTv.setText(this.valuesArr.get(position).GetSector());
        authorsTv.setText(this.valuesArr.get(position).GetAuthors());

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("----Clicked on : ---", valuesArr.get(position).GetTitle());
            }
        });

        return rowView;
    }

}
