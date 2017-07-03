package com.example.shubham.googlesignindemo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;//Firebase Authentication State
    private FirebaseAuth.AuthStateListener authStateListener;

    Button button;
    EditText editText_email,editText_pass;
    Activity activity=this;

    String emailID,password;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(Button)findViewById(R.id.button);
        editText_email=(EditText)findViewById(R.id.email);
        editText_pass=(EditText)findViewById(R.id.password);





        firebaseAuth=FirebaseAuth.getInstance();
        /*
            Every time a new user logs in or logs out I.E. the authentication state is changed
            this method will be called.
         */
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user=firebaseAuth.getCurrentUser();
                if(user!=null)
                    Log.d("NewState","onAuthStateChanged: signed_In"+ user.getDisplayName());
                else
                    Log.d("NewState","onAuthStateChanged: signed_Out");

            }
        };


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailID=editText_email.getText().toString();
                password=editText_pass.getText().toString();
                if ((!emailID.equals("")) && (!password.equals("")) && Patterns.EMAIL_ADDRESS.matcher(emailID).matches()) {

//                    Toast.makeText(activity, "Right UID/Password", Toast.LENGTH_SHORT).show();
//                   firebaseAuth.createUserWithEmailAndPassword(emailID,password)
//                           .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
//                               @Override
//                               public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                   Log.d("Success", "createUserWithEmail:onComplete:" + task.isSuccessful());
//
//                                   // If sign in fails, display a message to the user. If sign in succeeds
//                                   // the auth state listener will be notified and logic to handle the
//                                   // signed in user can be handled in the listener.
//                                   if (!task.isSuccessful()) {
//                                       Toast.makeText(activity, "auth failed",
//                                               Toast.LENGTH_SHORT).show();
//                                   }
//                               }
//                           });
                 firebaseAuth.signInWithEmailAndPassword(emailID,password)
                         .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {


                                 user=firebaseAuth.getCurrentUser();

                                 Log.d("Success", "signInWithEmail:onComplete:" + task.isSuccessful());



                                 // If sign in fails, display a message to the user. If sign in succeeds
                                 // the auth state listener will be notified and logic to handle the
                                 // signed in user can be handled in the listener.
                                 if (!task.isSuccessful()) {
                                     Log.d("Fail", "signInWithEmail:failed", task.getException());
                                     Toast.makeText(activity, "FAILED",
                                             Toast.LENGTH_SHORT).show();
                                 }
                             }
                         }) ;
                }
                else
                    Toast.makeText(activity, "Wrong UID/Password", Toast.LENGTH_SHORT).show();
            }

        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //Starting the listener object
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Removing the listener object
        if (authStateListener != null)
            firebaseAuth.removeAuthStateListener(authStateListener);

    }
}
