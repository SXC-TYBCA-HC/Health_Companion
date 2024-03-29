package com.project.healthcompanion.DietPlansClasses;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project.healthcompanion.R;

import java.util.ArrayList;

public class DietPlansListAdapter extends ArrayAdapter<String > {

    ArrayList<String> list;
    Context context;

    public DietPlansListAdapter(Context context, ArrayList<String>items) {
        super(context, R.layout.dietplanslist_row, items);
        this.context = context;
        list = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.dietplanslist_row, null);

            TextView number = convertView.findViewById(R.id.number);
            number.setText(position+1 + ".");

            TextView name = convertView.findViewById(R.id.name);
            name.setText(list.get(position));
            String DPnam = name.getText().toString();
            /*name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DietPlans.ListClick(DPnam);
                }
            });*/

            ImageView remove = convertView.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DietPlans.removeItem(position, DPnam);
                    Log.d("ListCheck", "Deleted item number" + position + "Deleted item name:" + name.getText().toString());
                }
            });
        }
        return convertView;
    }
}