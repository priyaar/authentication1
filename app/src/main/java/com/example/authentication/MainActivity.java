package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

Button logoutBtn , resendCode;
TextView verifyMsg;
FirebaseAuth mAuth;
private ViewPager mViewPager;
private sectionsPagerAdapter mSectionsPageerAdapter;

private TabLayout mTabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        resendCode=findViewById (R.id.resendCode);
        verifyMsg = findViewById (R.id.verifyMsg);
        mAuth=FirebaseAuth.getInstance ();




          //Tabs
        mViewPager=(ViewPager)findViewById (R.id.main_tabPager);
        mSectionsPageerAdapter=new sectionsPagerAdapter (getSupportFragmentManager ());
        mViewPager.setAdapter (mSectionsPageerAdapter);

        mTabLayout=(TabLayout)findViewById (R.id.main_tabs);
        mTabLayout.setupWithViewPager (mViewPager);





        final FirebaseUser user =mAuth .getCurrentUser ();

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

    }

    @Override
    public void onStart() {
        super.onStart ( );
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser ( );
        if (currentUser == null) {

        }
    }



    @Override
    public boolean  onCreateOptionsMenu(Menu menu)  {
        super.onCreateOptionsMenu (menu);

        getMenuInflater ().inflate (R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected (item);
        if (item.getItemId ()== R.id.main_logout_btn){
            FirebaseAuth.getInstance ().signOut ();
            startActivity (new Intent (getApplicationContext (),Login.class));
            finish ();
//            SendToStart ();
        }

        return true;
    }

}