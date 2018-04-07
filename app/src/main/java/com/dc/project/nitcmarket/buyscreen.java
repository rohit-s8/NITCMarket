package com.dc.project.nitcmarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class buyscreen extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mAuth;
    private ValueEventListener l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyscreen);
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        l = ref.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Item_data> myDataset = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    int id = Integer.parseInt(snapshot.getKey());
                    Item_data I = snapshot.getValue(Item_data.class);
                    if(I.user.equals(mAuth.getCurrentUser().getUid()))
                        continue;
                    I.setId(id);
                    myDataset.add(I);
                }
                if(myDataset.size() == 0){
                    Toast.makeText(buyscreen.this, "No items to buy", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(buyscreen.this, nextscreen.class));
                    finish();
                } else {

                    mRecyclerView = findViewById(R.id.cardList);
                    mLayoutManager = new LinearLayoutManager(buyscreen.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new MyAdapter(myDataset);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(buyscreen.this, nextscreen.class));
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.removeEventListener(l);
    }
}
