package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Register extends AppCompatActivity {
            EditText mFullName,mEmail,mUsn,mPassword;
            Button mRegisterBtn;
            TextView mloginBtn;
            FirebaseAuth mAuth;
            android.widget.ProgressBar ProgressBar;

             private DatabaseReference mDatabase;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate (savedInstanceState);
                setContentView (R.layout.activity_register);

                mFullName = findViewById (R.id.fullName);
                mEmail = findViewById (R.id.email);
                mPassword = findViewById (R.id.password);
                mUsn = findViewById (R.id.usn);
                mRegisterBtn = findViewById (R.id.registerBtn);
                mloginBtn = findViewById (R.id.loginBtn);
                ProgressBar = findViewById (R.id.progressBar);
                mAuth = FirebaseAuth.getInstance ( );

                if (mAuth.getCurrentUser () != null){
                    startActivity (new Intent (getApplicationContext (),MainActivity.class));
                    finish ();
            }


                mRegisterBtn.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View v) {
                        String email = mEmail.getText ( ).toString ( ).trim ( );
                        String password = mPassword.getText ( ).toString ( ).trim ( );

                        if (TextUtils.isEmpty(email)) {
                            mEmail.setError ("Email is requered.");
                            return;

                        }
                        if (TextUtils.isEmpty(password)) {
                            mPassword.setError("password not entered.");
                            return;

                        }

                        if (password.length ( ) < 6) {
                            mPassword.setError ("Password Must Contain More Than 5 Characters");
                            return;
                        }

                        ProgressBar.setVisibility(View.VISIBLE);
                        //register the user in firebase//

                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult> ( ) {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful ()){

                                    FirebaseUser current_user =FirebaseAuth.getInstance ().getCurrentUser ();
                                    String uid =current_user.getUid ();

                                    mDatabase=FirebaseDatabase.getInstance ().getReference ().child ("users").child (uid);

                                    String stringmFullName =mFullName.getText ().toString ();
                                   HashMap<String,String> userMap= new HashMap<> ();
                                   userMap.put ("name", stringmFullName); // changed to string
                                    userMap.put ("status","Hi,there i am using the App. ");
                                    userMap.put ("image","default");
                                    userMap.put ("thumb_image","default");
                                    mDatabase.setValue (userMap);

                                    //send verification link
                                    FirebaseUser fuser = mAuth.getCurrentUser ();
                                    fuser.sendEmailVerification ().addOnSuccessListener (new OnSuccessListener<Void> ( ) {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText (Register.this,"Verification Email Has Been Sent ", Toast.LENGTH_LONG).show ();

                                            //code for not going back to login when pressed back button
                                            Intent intent = new Intent(Register.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);



                                        }


                                    }).addOnFailureListener (new OnFailureListener ( ) {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            final String TAG = "";
                                            Log.d(TAG, "onFailure:Email not sent"+ e.getMessage () );


                                        }
                                    });





                                    Toast.makeText(Register.this,"Successfully Created",Toast.LENGTH_LONG).show();
                                    startActivity (new Intent (getApplicationContext (),MainActivity.class));
                                }

                                else {
                                    Toast.makeText (Register.this, "Error !" + task.getException().getMessage (),Toast.LENGTH_LONG).show ();

                                }
                            }
                        });

                    }

                });
                mloginBtn.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View v) {
                        startActivity (new Intent (getApplicationContext (),Login.class));
                    }
                });
            }
        }


