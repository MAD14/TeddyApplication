package it.polito.teddyapplication;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Federico on 20/04/2017.
 */

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText nameView;
    private EditText surnameView;
    private EditText emailView;
    private EditText passView;
    private EditText userView;
    private String name,surname,username,email,password;
    private static final String TAG = RegistrationActivity.class.getName();
    private boolean find;
    private boolean bo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Intent intent = getIntent();
        String emailRequested = intent.getStringExtra("emailRequested");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        nameView = (EditText) findViewById(R.id.name);
        surnameView = (EditText) findViewById(R.id.surname);
        userView = (EditText) findViewById(R.id.username);
        emailView = (EditText) findViewById(R.id.email);
        emailView.setText(emailRequested);
        passView = (EditText) findViewById(R.id.password);


        final Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = nameView.getText().toString();
                surname = surnameView.getText().toString();

                username = userView.getText().toString();

                email = emailView.getText().toString();
                password = passView.getText().toString();

                Button newUser = (Button) findViewById(R.id.email_register_button);

                newUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createAccount();

                    }
                });
            }
        });
    }



    public void createAccount() {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");

                            if(checkUserName()) {
                                DatabaseReference ref = myRef.child(username);
                                ref.child("Name").setValue(name);
                                ref.child("Surname").setValue(surname);
                                ref.child("Email").setValue(email);
                                ref.child("Password").setValue(password);
                                Toast.makeText(RegistrationActivity.this, "Added " + name + " " + surname,
                                        Toast.LENGTH_SHORT).show();
                                mainActivityCall();
                            }
                            else{
                                Log.w(TAG, "createUserWithEmail:failure->username already in use", task.getException());
                                Toast.makeText(RegistrationActivity.this, "Authentication failed: username already in use! Please select another one",
                                        Toast.LENGTH_SHORT).show();


                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }


                });
    }

    public boolean checkUserName() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        find=false;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (username==data.getKey()) {
                        find=true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Failed to read value.", error.toException());
                Toast.makeText(RegistrationActivity.this, "Failed to read value from DB!.",
                        Toast.LENGTH_SHORT).show();


            }

        });
        return find;
    }



    public void mainActivityCall(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }



}

