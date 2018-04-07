package com.dc.project.nitcmarket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class nextscreen extends AppCompatActivity implements View.OnClickListener{

    private Button logout;
    private Button sell;
    private Button buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nextscreen);
        logout = findViewById(R.id.sign_out);
        buy = findViewById(R.id.buy);
        sell = findViewById(R.id.sell);

    }

    @Override
    public void onStart(){
        super.onStart();
        logout.setOnClickListener(nextscreen.this);
        buy.setOnClickListener(nextscreen.this);
        sell.setOnClickListener(nextscreen.this);
    }

    public void onClick(View v){
        if (v.getId() == R.id.sign_out){
            AuthUI.getInstance()
                    .signOut(nextscreen.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            Toast.makeText(nextscreen.this, "Logged out", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(nextscreen.this, MainActivity.class));
                            finish();
                        }
                    });
        } else if(v.getId() == R.id.sell){
            startActivity(new Intent(nextscreen.this, sellscreen.class));
            finish();
        } else if(v.getId() == R.id.buy){
            startActivity(new Intent(nextscreen.this, buyscreen.class));
            finish();
        }
    }

}
