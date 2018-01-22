package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class PartyActivity extends AppCompatActivity {
    public static final String partyName = "partyName";
    public static final String partyID = "id";

    public static class PartyViewHolder extends RecyclerView.ViewHolder {
        TextView partyNameTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;
        TextView addressTextView;
        TextView organizerTextView;
        TextView organizerRatingTextView;
        CircleImageView partyImageView;

        public PartyViewHolder(View v) {
            super(v);
            partyNameTextView = (TextView) itemView.findViewById(R.id.partyNameTextView);
            startTimeTextView = (TextView) itemView.findViewById(R.id.startTimeTextView);
            endTimeTextView   = (TextView) itemView.findViewById(R.id.endTimeTextView);
            addressTextView   = (TextView) itemView.findViewById(R.id.addressTextView);
            organizerTextView = (TextView) itemView.findViewById(R.id.organizerTextView);
            organizerRatingTextView = (TextView) itemView.findViewById(R.id.organizerRatingTextView);
            partyImageView = (CircleImageView) itemView.findViewById(R.id.partyImageView);
        }
    }

    public static final String PARTIES_CHILD = "parties";
    private RecyclerView mPartyRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Party, PartyActivity.PartyViewHolder> mFirebaseAdapter;
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        mPartyRecyclerView = (RecyclerView) findViewById(R.id.partyRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        SnapshotParser<Party> parser = new SnapshotParser<Party>() {
            @Override
            public Party parseSnapshot(DataSnapshot dataSnapshot) {
                Party Party = dataSnapshot.getValue(Party.class);
                if (Party != null) {
                    Party.setId(dataSnapshot.getKey());
                }
                return Party;
            }
        };

        DatabaseReference partiesRef = mFirebaseDatabaseReference.child(PARTIES_CHILD);

        FirebaseRecyclerOptions<Party> options =
                new FirebaseRecyclerOptions.Builder<Party>()
                        .setQuery(partiesRef, parser)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Party, PartyViewHolder>(options) {

            @Override
            public PartyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PartyViewHolder(inflater.inflate(R.layout.item_party, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final PartyViewHolder viewHolder,
                                            int position,
                                            Party party) {

                if (party.getName() != null) {
                    viewHolder.partyNameTextView.setText(party.getName());
                    viewHolder.partyNameTextView.setVisibility(TextView.VISIBLE);
                }

                if (party.getStartTime() != null) {
                    viewHolder.startTimeTextView.setText(party.getStartTime());
                    viewHolder.startTimeTextView.setVisibility(TextView.VISIBLE);
                }
                if (party.getEndTime()!= null) {
                    viewHolder.endTimeTextView.setText(party.getEndTime());
                    viewHolder.endTimeTextView.setVisibility(TextView.VISIBLE);
                }
                if (party.getAddress() != null) {
                    viewHolder.addressTextView.setText(party.getAddress());
                    viewHolder.addressTextView.setVisibility(TextView.VISIBLE);
                }
                if (party.getOrganizerName() != null) {
                    viewHolder.organizerTextView.setText(party.getOrganizerName());
                    viewHolder.organizerTextView.setVisibility(TextView.VISIBLE);
                }
                if (party.getOrganizerRating() != null) {
                    viewHolder.organizerRatingTextView.setText(party.getOrganizerRating());
                    viewHolder.organizerRatingTextView.setVisibility(TextView.VISIBLE);
                }
                viewHolder.organizerTextView.setText(party.getOrganizerName());
                if (party.getImageUrl() == null) {
                    viewHolder.partyImageView.setImageDrawable(ContextCompat.getDrawable(PartyActivity.this,
                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(PartyActivity.this)
                            .load(party.getImageUrl())
                            .into(viewHolder.partyImageView);
                }

            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int PartyCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added party.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (PartyCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mPartyRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mPartyRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mPartyRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPartyRecyclerView.setAdapter(mFirebaseAdapter);
        mPartyRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Party partyRef = mFirebaseAdapter.getItem(position);
                        Intent intent = new Intent(PartyActivity.this, PartyActionActivity.class);
                        intent.putExtra(partyID, ""+partyRef.getId());
                        intent.putExtra(partyName, partyRef.getName());
                        startActivity(intent);

                    }
                })
        );
    }
    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }
}
