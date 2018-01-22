package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PartyActionActivity extends AppCompatActivity {

    TextView partyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_action);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String partyId = intent.getStringExtra(PartyActivity.partyID);
        String partyName = intent.getStringExtra(PartyActivity.partyName);

        // Capture the layout's TextView and set the string as its text
        partyTextView = (TextView) findViewById(R.id.partyNameTextView2);
        partyTextView.setText(partyName);
    }
}
