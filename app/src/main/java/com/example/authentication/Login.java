package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Login extends AppCompatActivity {
    EditText mEmail,mpassword;
    Button mLoginBtn;
    TextView mCreateBtn , forgotTextLink;
    ProgressBar ProgressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);

        mEmail = findViewById (R.id. Email);
        mpassword = findViewById (R.id.Password);
        mAuth = FirebaseAuth.getInstance ();
        ProgressBar  = findViewById (R.id.progressBar2);
        mLoginBtn =findViewById (R.id.loginBtn);
        mCreateBtn = findViewById (R.id.creatText);
        forgotTextLink = findViewById (R.id.ForgotPassword);


        mLoginBtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText ( ).toString ( ).trim ( );
                String Password = mpassword.getText ( ).toString ( ).trim ( );

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError ("Email is requered.");
                    return;

                }
                if (TextUtils.isEmpty(Password)) {
                    mpassword.setError("password not entered.");
                    return;

                }

                if (Password.length ( ) < 6) {
                    mpassword.setError ("Password Must Contain More Than 5 Characters");
                    return;
                }

                ProgressBar.setVisibility(View.VISIBLE);
                //authenticate the user
                mAuth.signInWithEmailAndPassword (email,Password).addOnCompleteListener (new OnCompleteListener<AuthResult> ( ) {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful ( )) {
                            ProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this," Logined in Successfully",Toast.LENGTH_LONG).show();
                            startActivity (new Intent (getApplicationContext (),MainActivity.class));


                            //code for not going back to login when pressed back button
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        }
                        else{

                            Toast.makeText (Login.this, "Error !" + task.getException().getMessage (),Toast.LENGTH_LONG).show ();
                            ProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
        mCreateBtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (getApplicationContext (),Register.class));
            }
        });

        forgotTextLink.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {


                final EditText resetMail = new EditText (v.getContext ());
                AlertDialog.Builder passwordRestDialog = new AlertDialog.Builder (v.getContext ());
                passwordRestDialog.setTitle ("Reset password ? ");
                passwordRestDialog.setMessage (" Enter Your Email To Receive Reset Link");
                passwordRestDialog.setView (resetMail);

                passwordRestDialog.setPositiveButton ("yes", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText ().toString ();
                        mAuth.sendPasswordResetEmail (mail).addOnSuccessListener (new OnSuccessListener<Void> ( ) {
                            @Override
                            public void onSuccess(Void aVoid) {
                              Toast.makeText (Login.this,"Reset Link Sent To Your Email ", Toast.LENGTH_SHORT).show ();
                            }
                        }).addOnFailureListener (new OnFailureListener ( ) {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText (Login.this,"Error ! Reset Link Is Not Sent " + e.getMessage (), Toast.LENGTH_SHORT).show ();

                            }
                        });

                    }
                });
                passwordRestDialog.setNegativeButton (" no", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close t5he dialog
                    }
                });
                passwordRestDialog.create ().show ();

            }
        });


    }
}