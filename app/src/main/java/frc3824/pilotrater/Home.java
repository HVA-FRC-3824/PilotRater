package frc3824.pilotrater;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    private String mEventKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ((TextView)findViewById(R.id.version)).setText(String.format("Version: %s", Constants.VERSION));


        ((EditText)findViewById(R.id.event)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mEventKey = s.toString();
                Database.getInstance(mEventKey);
            }
        });

        findViewById(R.id.match_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MatchList.class);
                startActivity(intent);
            }
        });
        // Setup the database or reload it (to make the schedule and button list work)
        if(mEventKey != "") {
            Database.getInstance(mEventKey);
        }
        else
        {
            Database.getInstance();
        }
    }
}
