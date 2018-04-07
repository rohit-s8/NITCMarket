package com.dc.project.nitcmarket;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by rohit on 6/4/18.
 */

public class Item_data {

    public String description;
    public String price;
    public int state;
    public String user;
    public int id;
    private ValueEventListener l;
    private DatabaseReference ref;

    public Item_data(){
        ref = FirebaseDatabase.getInstance().getReference();
    }

//    public Item_data(String s, String d, String p, String u){
//        state = Integer.parseInt(s);
//        description = d;
//        price = Double.parseDouble(p);
//        user = u;
//    }

    public void setId(int id){
        this.id = id;
    }

    public void getstate(){
        String item_id = Integer.toString(this.id);
        l = ref.child("items").child(item_id).child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Item_data.this.state = Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean isSellable(){
        return (this.state==0);
    }

    public void set_sold(){
        this.state = 1;
        ref.child("items").child(Integer.toString(this.id)).child("state").setValue(1);
    }

    public void remove_item(){
        ref.child("items").child(Integer.toString(this.id)).setValue(null);
    }

    public void stop_listening(){
        ref.removeEventListener(l);
    }
}
