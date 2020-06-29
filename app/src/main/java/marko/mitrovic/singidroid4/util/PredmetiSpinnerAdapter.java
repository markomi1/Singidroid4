package marko.mitrovic.singidroid4.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import marko.mitrovic.singidroid4.R;

public class PredmetiSpinnerAdapter extends BaseAdapter implements SpinnerAdapter{


    JsonArray items;
    Context context;

    public PredmetiSpinnerAdapter(Context context, JsonArray items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.spinner_custom_layout, null);
        TextView textView = view.findViewById(R.id.main);
        JsonObject obj = items.get(position).getAsJsonObject();
        textView.setText(obj.get("title").getAsString());
        textView.setTag(obj.get("id").getAsString());
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view;
        view = View.inflate(context, R.layout.spinner_my_items, null);
        final TextView textView = view.findViewById(R.id.dropdown);
        JsonObject obj = items.get(position).getAsJsonObject();
        textView.setText(obj.get("title").getAsString());
        textView.setTag(obj.get("id").getAsString());

        return view;
    }
}
