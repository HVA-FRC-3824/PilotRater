package frc3824.pilotrater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author frc3824
 * Created: 3/8/17
 */

public class PilotRater extends Activity {

    private static String TAG = "PilotRater";

    private int mMatchNumber = -1;
    private Database mDatabase;

    private String mScoutName;

    private AlertDialog mLogisticsDialog;
    private View mLogisticsView;
    private AutoCompleteTextView mScoutNameTextView;
    private View mLogisticsScoutNameBackground;
    private TextView mLogisticsIncorrect;

    private ArrayList<SavablePilot> mTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilot_rater);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        mMatchNumber = extras.getInt(Constants.MATCH_NUMBER);

        mDatabase = Database.getInstance();

        findViewById(android.R.id.content).setKeepScreenOn(true);

        Match match = mDatabase.getMatch(mMatchNumber);

        mTeams = new ArrayList<>();
        mTeams.add((SavablePilot) findViewById(R.id.blue1));
        mTeams.add((SavablePilot) findViewById(R.id.blue2));
        mTeams.add((SavablePilot) findViewById(R.id.blue3));
        mTeams.add((SavablePilot) findViewById(R.id.red1));
        mTeams.add((SavablePilot) findViewById(R.id.red2));
        mTeams.add((SavablePilot) findViewById(R.id.red3));
        for(int i = 0; i < 6; i++)
        {
            mTeams.get(i).setTeamNumber(match.team_numbers.get(i));
            if(i < 3){
                mTeams.get(i).setBackgroundColor(Color.BLUE);
            } else {
                mTeams.get(i).setBackgroundColor(Color.RED);
            }
        }

        MatchPilotData mpd = mDatabase.getMatchPilotData(mMatchNumber);
        if(mpd != null){
            load(mpd);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        mLogisticsView = LayoutInflater.from(this).inflate(R.layout.dialog_match_logistics, null);
        ((TextView)mLogisticsView.findViewById(R.id.match_number)).setText(String.format("Match Number: %d", mMatchNumber));

        mScoutNameTextView = (AutoCompleteTextView)mLogisticsView.findViewById(R.id.scout_name);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Constants.SUPER_SCOUTS_LIST);
        mScoutNameTextView.setAdapter(aa);
        builder.setView(mLogisticsView);

        mLogisticsIncorrect = (TextView)mLogisticsView.findViewById(R.id.incorrect);
        mLogisticsScoutNameBackground = mLogisticsView.findViewById(R.id.scout_name_background);

        builder.setPositiveButton("Ok", null);
        builder.setCancelable(false);
        mLogisticsDialog = builder.create();
        mLogisticsDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        mScoutName = mScoutNameTextView.getText().toString();
                        if(mScoutName.equals("")){
                            mLogisticsScoutNameBackground.setBackgroundColor(Color.RED);
                            mLogisticsIncorrect.setVisibility(View.VISIBLE);
                        } else {
                            mLogisticsDialog.dismiss();
                        }
                    }
                });
            }
        });
        if(mScoutName == null || mScoutName.isEmpty()) {
            mLogisticsDialog.show();
        }
    }

    /**
     * Creates the overflow menu for the toolbar. Removes previous match or next match options if
     * they do not exist.
     *
     * @param menu The menu that is filled with the overflow menu.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.match_overflow, menu);
        if (mMatchNumber == 1) {
            menu.removeItem(R.id.previous_match);
        }
        if (mMatchNumber == mDatabase.getNumberOfMatches()) {
            menu.removeItem(R.id.next_match);
        }

        return true;
    }

    /**
     * Override to show icons on the overflow menu
     * http://stackoverflow.com/questions/18374183/how-to-show-icons-in-overflow-menu-in-actionbar
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Log.e(TAG, "onMenuOpened", e);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * Implements the actions for the overflow menu
     *
     * @param item Menu item that is selected from the overflow menu
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                home_press();
                break;
            case R.id.match_list:
                back_press();
                break;
            case R.id.save:
                save_press();
                break;
            case R.id.previous_match:
                previous_press();
                break;
            case R.id.next_match:
                next_press();
                break;
            case R.id.scout_name:
                mLogisticsDialog.show();
                break;
            default:
                assert false;
        }
        return true;
    }


    /**
     * The action that happens when the home button is pressed. Brings up dialog with options to save
     * and takes user to the home screen.
     */
    private void home_press() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PilotRater.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "save pressed");
                // Collect values from all the custom elements
                String error = save();

                if (error.equals("")) {
                    Log.d(TAG, "Saving values");

                    // Go to the next match
                    Intent intent = new Intent(PilotRater.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(PilotRater.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();

                }
            }
        });

        // Cancel Option
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dialogbox goes away
            }
        });

        // Continue w/o Saving Option
        builder.setNegativeButton("Continue w/o Saving", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Cont W/O Saving");
                // Go to home
                Intent intent = new Intent(PilotRater.this, Home.class);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * The action that happens when the back button is pressed. Brings up dialog with options to save
     * and takes user to the match list.
     */
    private void back_press() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PilotRater.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "save pressed");
                // Collect values from all the custom elements
                String error = save();

                if (error.equals("")) {
                    Log.d(TAG, "Saving values");

                    Intent intent = new Intent(PilotRater.this, MatchList.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(PilotRater.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Cancel Option
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dialogbox goes away
            }
        });

        // Continue w/o Saving Option
        builder.setNegativeButton("Continue w/o Saving", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Cont W/O Saving");
                Intent intent = new Intent(PilotRater.this, MatchList.class);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * Saves the current data
     */
    private void save_press() {
        // Collect values from all the custom elements

        String error = save();

        if (error.equals("")) {
            Log.d(TAG, "Saving values");
        } else {
            Toast.makeText(PilotRater.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * The action that happens when the previous match button is pressed. Brings up dialog with options to save
     * and takes user to match scout the previous match.
     */
    private void previous_press() {
        Log.d(TAG, "previous match pressed");
        AlertDialog.Builder builder = new AlertDialog.Builder(PilotRater.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Collect values from all the custom elements
                String error = save();

                if (error.equals("")) {
                    Log.d(TAG, "Saving values");

                    // Go to the next match
                    Intent intent = new Intent(PilotRater.this, PilotRater.class);
                    intent.putExtra(Constants.MATCH_NUMBER, mMatchNumber - 1);
                    startActivity(intent);
                } else {
                    Toast.makeText(PilotRater.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Cancel Option
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dialogbox goes away
            }
        });

        // Continue w/o Saving Option
        builder.setNegativeButton("Continue w/o Saving", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Cont W/O Saving");
                // Go to the previous match
                Intent intent = new Intent(PilotRater.this, PilotRater.class);
                intent.putExtra(Constants.MATCH_NUMBER, mMatchNumber - 1);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * The action that happens when the next match button is pressed. Brings up dialog with options to save
     * and takes user to match scout the next match.
     */
    private void next_press() {
        Log.d(TAG, "next match pressed");

        AlertDialog.Builder builder = new AlertDialog.Builder(PilotRater.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d(TAG, "Save pressed");
                // Collect values from all the custom elements

                String error = save();

                if (error.equals("")) {
                    // Go to the next match
                    Intent intent = new Intent(PilotRater.this, PilotRater.class);
                    intent.putExtra(Constants.MATCH_NUMBER, mMatchNumber + 1);
                    startActivity(intent);
                } else {
                    Log.e(TAG, error);
                    Toast.makeText(PilotRater.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Cancel Option
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dialogbox goes away
            }
        });

        // Continue w/o Saving Option
        builder.setNegativeButton("Continue w/o Saving", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Cont W/O Saving");
                // Go to the next match
                Intent intent = new Intent(PilotRater.this, PilotRater.class);
                intent.putExtra(Constants.MATCH_NUMBER, mMatchNumber + 1);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        back_press();
    }

    public void load(MatchPilotData mpd){
        for(int i = 0; i < 6; i++){
            mTeams.get(i).set(mpd.teams.get(i));
        }
    }

    public String save() {
        MatchPilotData mpd = new MatchPilotData();
        mpd.match_number = mMatchNumber;
        for(int i = 0; i < 6; i++){
            mpd.teams.add(mTeams.get(i).get());
        }
        mDatabase.setMatchPilotData(mpd);
        return "";
    }
}
