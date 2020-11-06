package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
Button logoutBtn , resendCode;
TextView verifyMsg;
FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        logoutBtn= findViewById (R.id.logoutBtn);
        resendCode=findViewById (R.id.resendCode);
        verifyMsg = findViewById (R.id.verifyMsg);
        fAuth=FirebaseAuth.getInstance ();


        final FirebaseUser user =fAuth .getCurrentUser ();

        if (!user.isEmailVerified ()){
            resendCode.setVisibility (View.VISIBLE);
            verifyMsg.setVisibility (View.VISIBLE);

            resendCode.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(final View v) {
                    user.sendEmailVerification ().addOnSuccessListener (new OnSuccessListener<Void> ( ) {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText (v.getContext (),"Verification Email Has Been Sent ", Toast.LENGTH_LONG).show ();


                        }


                    }).addOnFailureListener (new OnFailureListener ( ) {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            final String tag = "tag";
                            Log.d("tag", "onFailure:Email not sent"+ e.getMessage () );


                        }
                    });
                }
            });
        }



        logoutBtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance ().signOut ();
                startActivity (new Intent (getApplicationContext (),Login.class));
                finish ();
            }
        });
    }

}