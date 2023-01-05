package com.education.ontimeattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
            greeting= "Good Morning,";
        }else if(timeOfDay >= 12 && timeOfDay < 17){
            homeLayout.setBackgroundResource(R.drawable.afternoonbg);
            greeting= "Good Afternoon,";
        }else if(timeOfDay >= 17){
            homeLayout.setBackgroundResource(R.drawable.eveningbg);
            greeting= "Good Evening,";
        }else {
            homeLayout.setBackgroundResource(R.drawable.nightbg);
            greeting= "Soja haramjaade,";
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
    }
}