package codebrains.edufind.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import codebrains.edufind.Initializers.Book;
import codebrains.edufind.R;
import android.view.View.OnClickListener;

import static codebrains.edufind.Activities.ProviderActivity.SetSelectedItemPosition;

/**
 * Custom adapter class that processes all the inserted book registries of the user and then
 * displays them on the list view element.
 */
public class ProviderCollectionAdapter extends ArrayAdapter<Book> {

    private final Context context;
    private final List<Book> valuesArr;
    private boolean checkFlag;
    private View previousView;

    private int selectedPosition;

    public ProviderCollectionAdapter(Context context, List<Book> values, boolean chFlag) {
        super(context, R.layout.provider_collection_list, values);
        this.context = context;
        this.valuesArr = values;
        this.checkFlag = chFlag;

        this.selectedPosition = -1;
        SetSelectedItemPosition(selectedPosition);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView;

        if(this.checkFlag) {
            rowView = inflater.inflate(R.layout.provider_collection_list, parent, false);

            TextView titleTv = (TextView) rowView.findViewById(R.id.textView28);
            TextView sectorTv = (TextView) rowView.findViewById(R.id.textView29);
            TextView authorsTv = (TextView) rowView.findViewById(R.id.textView30);

            titleTv.setText(this.valuesArr.get(position).GetTitle());
            sectorTv.setText(this.valuesArr.get(position).GetSector());

            String authors = this.valuesArr.get(position).GetAuthors();
            if(authors.length() >= 27) {
                authors = authors.substring(0, 25) + "...";
            }
            authorsTv.setText(authors);

            rowView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.setBackgroundColor(context.getResources().getColor(R.color.yellowish));

                    if(position != selectedPosition && selectedPosition != -1)
                        previousView.setBackgroundColor(Color.TRANSPARENT);

                    if(position == selectedPosition) {
                        previousView.setBackgroundColor(Color.TRANSPARENT);
                        selectedPosition = -1;
                    }
                    else {
                        selectedPosition = position;
                        previousView = v;
                    }

                    SetSelectedItemPosition(selectedPosition);
                }
            });
        }
        else {
            rowView = inflater.inflate(R.layout.provider_empty_collection_list, parent, false);
            TextView emptyTv = (TextView) rowView.findViewById(R.id.textView31);
            emptyTv.setText(this.valuesArr.get(position).GetTitle());
        }

        return rowView;
    }


}
