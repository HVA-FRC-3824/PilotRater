package frc3824.pilotrater;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author frc3824
 *         Created: 3/9/17
 */

public class LVA_PilotRating extends ArrayAdapter<String> {

    private ArrayList<String> mOptions;
    private Context mContext;

    public LVA_PilotRating(@NonNull Context context, @NonNull ArrayList<String> objects) {
        super(context, android.R.layout.simple_spinner_dropdown_item, objects);
        mOptions = objects;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_pilot_rating, null);
        }
        TextView txt = (TextView)convertView;

        txt.setText(mOptions.get(position));
        txt.setTextColor(Color.WHITE);
        txt.setBackground(null);

        return convertView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_pilot_rating, null);
        }

        TextView txt = (TextView)convertView;

        txt.setText(mOptions.get(position));
        txt.setTextColor(Color.BLACK);
        txt.setBackgroundColor(Color.WHITE);

        return convertView;
    }
}
