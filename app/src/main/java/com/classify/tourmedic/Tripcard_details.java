package com.classify.tourmedic;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tripcard_details extends AppCompatActivity {

    String trip_no,no,dest,startdate,enddate;
    DatabaseReference mDatabaseCreateTrip;
    private RecyclerView recyclerView;
    ArrayList<fragment_Class> al2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripcard_details);
        trip_no = getIntent().getExtras().getString("trip_no");
        no = getIntent().getExtras().getString("no");
        mDatabaseCreateTrip = FirebaseDatabase.getInstance().getReference().child("CreateTrip");
        mDatabaseCreateTrip.keepSynced(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_trip_details);
        setUpRecyclerView(recyclerView);
        mDatabaseCreateTrip.child(no).child("Trips").child("Trip"+trip_no).child("places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    startdate = (String) snapshot.child("startdate").getValue();
                    enddate = (String) snapshot.child("enddate").getValue();
                    dest = (String) snapshot.child("destination").getValue();
                    fragment_Class frag = snapshot.getValue(fragment_Class.class);
                    frag.setDestination(dest);
                    frag.setStartdate(startdate);
                    frag.setEnddate(enddate);
                    al2.add(frag);
                }
                recyclerView.setAdapter(new Tripcard_details.adapter(recyclerView.getContext(), al2));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setUpRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new adapter(rv.getContext(), al2));
        Log.d("Firebase-data", "user adapter");
    }

    private class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
        ArrayList<fragment_Class> al2 = new ArrayList<>();
        Context context;


        public adapter(Context c, ArrayList<fragment_Class> al) {
            context = c;
            al2 = al;

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tripname;
            private TextView date;
            private CardView tripcard;

            // private RelativeLayout comment;

            Context context;

            public ViewHolder(View itemView) {
                super(itemView);
                tripname = (TextView) itemView.findViewById(R.id.mstv10);
                date = (TextView) itemView.findViewById(R.id.date0);
                tripcard = (CardView) itemView.findViewById(R.id.cardview_trip_details);

//                comment = (RelativeLayout)itemView.findViewById(R.id.cmnt);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_cardview_details, parent, false);
            return new Tripcard_details.adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tripname.setText(al2.get(position).getDestination());
            holder.date.setText(al2.get(position).getStartdate() +" - " +al2.get(position).getEnddate());

        }


        @Override
        public int getItemCount() {
            return al2.size();
        }

    }

}
