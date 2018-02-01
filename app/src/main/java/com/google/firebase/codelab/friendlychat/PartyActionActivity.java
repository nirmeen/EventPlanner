package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PartyActionActivity extends AppCompatActivity {

    TextView partyTextView;
    ListView attendListView;
    CheckBox attending;
    CheckBox checkin;
    Object attendees;
    ArrayAdapter<String> attendeesListAdapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference mFirebaseDatabaseReference;
    public static final String PARTIES_CHILD = "parties";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_action);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //firebase storage initialization
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String partyId = intent.getStringExtra(PartyActivity.partyID);
        String partyName = intent.getStringExtra(PartyActivity.partyName);

        DatabaseReference partiesRef = mFirebaseDatabaseReference.child(PARTIES_CHILD);


        SnapshotParser<Party> parser = new SnapshotParser<Party>() {
            @Override
            public Party parseSnapshot(DataSnapshot dataSnapshot) {
                Party Party = dataSnapshot.getValue(Party.class);
                if (Party != null) {

                    Party.setId(dataSnapshot.getKey());
                    if(partyId == Party.getId()){
                        attendees = Party.getAttendees();
                    }
                }
                return Party;
            }
        };

        FirebaseRecyclerOptions<Party> options =
                new FirebaseRecyclerOptions.Builder<Party>()
                        .setQuery(partiesRef, parser)
                        .build();
        Query partyQuery = partiesRef.orderByKey().equalTo(partyId);

        Log.e("ref party ","ddd "+partiesRef.child(partyId).child("attendees").child("UserUID"));
        if(attendees!= null){
            Log.e("ref party ","ddd ggg "+attendees.toString());
        }




        // Capture the layout's TextView and set the string as its text
        partyTextView = (TextView) findViewById(R.id.partyNameTextView2);
        partyTextView.setText(partyName);


        attendListView = (ListView) findViewById(R.id.attendListView);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        attendListView.setEmptyView(emptyText);




    }
}
