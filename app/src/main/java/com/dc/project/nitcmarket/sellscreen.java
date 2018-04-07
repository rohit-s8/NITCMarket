package com.dc.project.nitcmarket;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.ui.phone.CountryListSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class sellscreen extends AppCompatActivity implements View.OnClickListener{

    private Button save_data;
    private EditText description;
    private EditText price;
    private static int num_items=0;
    private DatabaseReference ref;
    private static final String TAG = "selling activity";
    private ValueEventListener vl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellscreen);
        save_data = findViewById(R.id.save_button);
        description = findViewById(R.id.description_text);
        price = findViewById(R.id.price_data);
        ref = FirebaseDatabase.getInstance().getReference();

        vl = ref.child("nextid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    ref.child("nextid").setValue(0);
                } else {
                    num_items = Integer.parseInt(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "database error");
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        save_data.setOnClickListener(this);
    }

    public void onClick(View v){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String desc = description.getText().toString();
        String userid = user.getUid();
        String value = price.getText().toString();
//        double value=0;
//        try {
//            value = Double.parseDouble(price.getText().toString());
//        }catch(NumberFormatException e){
//            Toast.makeText(sellscreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }

        String num = Integer.toString(num_items+1);
        ref.child("nextid").setValue(num_items+1);
        ref.child("items").child(num).child("state").setValue(0);
        ref.child("items").child(num).child("user").setValue(userid);
        ref.child("items").child(num).child("description").setValue(desc);
        ref.child("items").child(num).child("price").setValue(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(sellscreen.this, "Saved", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(sellscreen.this,nextscreen.class));
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(sellscreen.this,nextscreen.class));
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ref.removeEventListener(vl);
    }
}
