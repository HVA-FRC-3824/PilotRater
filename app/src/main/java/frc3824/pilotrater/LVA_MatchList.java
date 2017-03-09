package frc3824.pilotrater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author frc3824
 * Created: 3/8/17
 */

public class LVA_MatchList extends ArrayAdapter<Integer> {

    private ArrayList<Integer> mMatchNumbers;
    private Context mContext;

    public LVA_MatchList(Context context, ArrayList<Integer> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        mMatchNumbers = objects;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        final int match_number = mMatchNumbers.get(position);


        TextView txt1 = (TextView) convertView.findViewById(android.R.id.text1);
        txt1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txt1.setText(String.format("Match %d", match_number));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PilotRater.class);
                intent.putExtra(Constants.MATCH_NUMBER, match_number);
                mContext.startActivity(intent);
            }
        });


        return convertView;
    }
}