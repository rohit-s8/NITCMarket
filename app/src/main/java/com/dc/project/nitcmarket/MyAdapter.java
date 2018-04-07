package com.dc.project.nitcmarket;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

/**
 * Created by rohit on 6/4/18.
 */



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Item_data> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        protected TextView vDescription;
        protected TextView vPrice;
        protected TextView vUser;
        protected TextView vBuy;
        public ViewHolder(View v) {
            super(v);
            vDescription = v.findViewById(R.id.description);
            vPrice = v.findViewById(R.id.priceview);
            vUser = v.findViewById(R.id.username);
            vBuy = v.findViewById(R.id.clickbuy);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Item_data> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Item_data item = mDataset.get(position);
        item.getstate();
        String id = String.format(Locale.ENGLISH,"%d",item.id);
        holder.vDescription.setText("item: "+item.description);
        holder.vPrice.setText("price: "+item.price);
        holder.vBuy.setClickable(true);
        holder.vBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isSellable()){
                    sell_item(item);
                    item.stop_listening();
                } else{
                    Toast.makeText(v.getContext(), "Item not available", Toast.LENGTH_LONG).show();
                }
            }
        });

        final ViewHolder temp = holder;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(item.user).child("Username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temp.vUser.setText("seller: "+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void sell_item(Item_data sell_item){
        sell_item.set_sold();
        sell_item.remove_item();
    }
}

