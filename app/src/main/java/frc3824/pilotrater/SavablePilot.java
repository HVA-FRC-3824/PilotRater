package frc3824.pilotrater;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author frc3824
 * Created: 3/8/17
 */

public class SavablePilot extends RelativeLayout implements View.OnLongClickListener, View.OnClickListener {

    private int mTeamNumber;

    private TextView mTeamNumberTV;
    private Spinner mRating;
    private TextView mDrops;
    private int mDropsCount;
    private TextView mLifts;
    private int mLiftsCount;

    public SavablePilot(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.savable_pilot, this);

        mTeamNumberTV = ((TextView) findViewById(R.id.team_number));
        mRating = ((Spinner)findViewById(R.id.rating));
        LVA_PilotRating aa = new LVA_PilotRating(context, new ArrayList<>(Arrays.asList(Constants.RATING_OPTIONS)));
        mRating.setAdapter(aa);
        mDrops = ((TextView)findViewById(R.id.drops));
        mDrops.setOnClickListener(this);
        mDrops.setOnLongClickListener(this);
        mDrops.setText("0");
        findViewById(R.id.drops_label).setOnClickListener(this);
        findViewById(R.id.drops_label).setOnLongClickListener(this);

        mLifts = ((TextView)findViewById(R.id.lifts));
        mLifts.setOnClickListener(this);
        mLifts.setOnLongClickListener(this);
        mLifts.setText("0");
        findViewById(R.id.lifts_label).setOnClickListener(this);
        findViewById(R.id.lifts_label).setOnLongClickListener(this);

    }

    void setTeamNumber(int team_number) {
        mTeamNumber = team_number;
        mTeamNumberTV.setText(String.format("%d:", team_number));
    }

    public String getErrors(){
        return "";
    }

    public void set(MatchTeamPilotData mtpd){
        mRating.setSelection(Arrays.asList(Constants.RATING_OPTIONS).indexOf(mtpd));
        mDropsCount = mtpd.drops;
        mDrops.setText(String.valueOf(mDropsCount));
        mLiftsCount = mtpd.lifts;
        mLifts.setText(String.valueOf(mLiftsCount));
    }

    public MatchTeamPilotData get(){
        MatchTeamPilotData mtpd = new MatchTeamPilotData();
        mtpd.team_number = mTeamNumber;
        mtpd.rating = Constants.RATING_OPTIONS[mRating.getSelectedItemPosition()];
        mtpd.drops = mDropsCount;
        mtpd.lifts = mLiftsCount;
        return mtpd;
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId())
        {
            case R.id.drops:
            case R.id.drops_label:
                if(mDropsCount > 0){
                    mDropsCount--;
                    mDrops.setText(String.valueOf(mDropsCount));
                }
                break;
            case R.id.lifts:
            case R.id.lifts_label:
                if(mLiftsCount > 0) {
                    mLiftsCount--;
                    mLifts.setText(String.valueOf(mLiftsCount));
                }
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.drops:
            case R.id.drops_label:
                mDropsCount++;
                mDrops.setText(String.valueOf(mDropsCount));
                break;
            case R.id.lifts:
            case R.id.lifts_label:
                mLiftsCount++;
                mLifts.setText(String.valueOf(mLiftsCount));
                break;
        }
    }
}
