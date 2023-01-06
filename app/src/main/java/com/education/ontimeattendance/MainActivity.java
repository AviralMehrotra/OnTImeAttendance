package com.education.ontimeattendance;

import static com.education.ontimeattendance.R.color.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.education.ontimeattendance.R.color;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button showProfile;
    private Button showQR;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showProfile= (Button)findViewById(R.id.showProfile);
        showProfile.setOnClickListener(this);

        showQR= (Button)findViewById(R.id.showQR);
        showQR.setOnClickListener(this);



        mAuth= FirebaseAuth.getInstance();

        user= FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView textFullname = (TextView) findViewById(R.id.fullName);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.fullname;

                    textFullname.setText(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        });


        Calendar cal = Calendar.getInstance();
        int timeOfDay = cal.get(Calendar.HOUR_OF_DAY);

        RelativeLayout homeLayout = findViewById(R.id.homeLayout);
        TextView greetingMsg = (TextView) findViewById(R.id.greetings);

        String greeting= null;

        if(timeOfDay >= 6 && timeOfDay < 12){
            homeLayout.setBackgroundResource(R.drawable.morningbg);
            textFullname.setTextColor(getResources().getColor(white));
            greetingMsg.setTextColor(getResources().getColor(fadedWhite));
            greeting= "Good Morning,";
        }else if(timeOfDay >= 12 && timeOfDay < 17){
            homeLayout.setBackgroundResource(R.drawable.afternoonbg);
            textFullname.setTextColor(getResources().getColor(paleYellow));
            greetingMsg.setTextColor(getResources().getColor(mudYellow));
            greeting= "Good Afternoon,";
        }else if(timeOfDay >= 17){
            homeLayout.setBackgroundResource(R.drawable.eveningbg);
            textFullname.setTextColor(getResources().getColor(whitePurple));
            greetingMsg.setTextColor(getResources().getColor(purple_100));
            greeting= "Good Evening,";
        }else {
            homeLayout.setBackgroundResource(R.drawable.nightbg);
            greeting= "Good Night,";
            textFullname.setTextColor(getResources().getColor(nightName));
            greetingMsg.setTextColor(getResources().getColor(nightGreeting));
        }
        greetingMsg.setText(greeting);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.showProfile:
                startActivity(new Intent(this, profile.class));
                break;
        }
        switch (view.getId()){
            case R.id.showQR:
                startActivity(new Intent(this, showQRcode.class));
                break;
        }
    }

    
}