package frc3824.pilotrater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * @author frc3824
 * Created: 3/8/17
 */

public class MatchList extends Activity {

    private Database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        mDatabase = Database.getInstance();

        int numberOfMatches = mDatabase.getNumberOfMatches();
        ArrayList<Integer> match_numbers = new ArrayList<>();

        ListView listView = (ListView)findViewById(R.id.match_list);

        for(int i = 1; i <= numberOfMatches; i++) {
            match_numbers.add(i);
        }

        LVA_MatchList lva = new LVA_MatchList(this, match_numbers);
        listView.setAdapter(lva);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Home.class));
    }
}