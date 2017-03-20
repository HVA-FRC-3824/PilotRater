package frc3824.pilotrater;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author frc3824
 * Created: 3/8/17
 */

public class Database {

    private final static String TAG = "Database";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRootRef;

    //region Database References
    private DatabaseReference mEventRef;
    private DatabaseReference mScheduleRef;
    private DatabaseReference mMatchPilotRef;
    //endregion

    private String mEventKey;

    //region Maps
    private static Set<String> mEvents;
    private Map<Integer, Match> mSchedule;
    private Map<Integer, MatchPilotData> mPilotMap;
    //endregion

    private static Database mSingleton;

    public static Database getInstance(String eventKey) {
        if (mSingleton == null) {
            mSingleton = new Database();
        }

        mSingleton.setEventKey(eventKey);
        return mSingleton;
    }

    public static Database getInstance() {
        if (mSingleton == null) {
            mSingleton = new Database();
        }

        return mSingleton;
    }

    private Database() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
        mRootRef = mFirebaseDatabase.getReference();

        mEvents = new HashSet<>();

        //Root reference's children are the events
        mRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildAdded: " + dataSnapshot.getKey());
                mEvents.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildChanged: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "onChildRemoved: " + dataSnapshot.getKey());
                mEvents.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "onCancelled");
            }
        });
    }

    public static Set<String> getEvents() {
        return mEvents;
    }

    private void setEventKey(String eventKey) {
        if (eventKey.isEmpty() || mEventKey == eventKey)
            return;

        mEventRef = mRootRef.child(eventKey);

        //region Setup references and maps
        //region Schedule
        mScheduleRef = mEventRef.child("schedule");
        mSchedule = new HashMap<>();
        mScheduleRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "schedule.onChildAdded: " + dataSnapshot.getKey());
                mSchedule.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(Match.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "schedule.onChildChanged: " + dataSnapshot.getKey());
                mSchedule.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(Match.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "schedule.onChildRemoved: " + dataSnapshot.getKey());
                mSchedule.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "schedule.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "schedule.onCancelled");
            }
        });
        //endregion

        //region Match Pilot
        mMatchPilotRef = mEventRef.child("pilot").child("match");
        mPilotMap = new HashMap<>();
        mMatchPilotRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pilot.onChildAdded: " + dataSnapshot.getKey());
                mPilotMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(MatchPilotData.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pilot.onChildChanged: " + dataSnapshot.getKey());
                mPilotMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(MatchPilotData.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "pilot.onChildRemoved: " + dataSnapshot.getKey());
                mPilotMap.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pilot.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "pilot.onCancelled");
            }
        });
        //endregion
        //endregion

    }

    //region Schedule Data
    public Match getMatch(int match_number)
    {
        return mSchedule.get(match_number);
    }

    public int getNumberOfMatches()
    {
        return mSchedule.size();
    }
    //endregion

    //region Pilot Data
    public MatchPilotData getMatchPilotData(int match_number) {
        return mPilotMap.get(match_number);
    }

    public void setMatchPilotData(MatchPilotData mpd) {
        mpd.last_modified = System.currentTimeMillis();
        mMatchPilotRef.child(String.valueOf(mpd.match_number)).setValue(mpd);
    }
    //endregion
}