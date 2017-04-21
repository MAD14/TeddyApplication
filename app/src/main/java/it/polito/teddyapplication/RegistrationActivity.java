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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        nameView = (EditText) findViewById(R.id.name);
        surnameView = (EditText) findViewById(R.id.surname);
        userView = (EditText) findViewById(R.id.username);
        emailView = (EditText) findViewById(R.id.email);
        passView = (EditText) findViewById(R.id.password);


        final Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = nameView.getText().toString();
                surname = surnameView.getText().toString();

                username = userView.getText().toString();

                // TODO Controllo validità username

                email = emailView.getText().toString();
                password = passView.getText().toString();

                final Button newUser = (Button) findViewById(R.id.email_sign_in_button);

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

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("users");

                                DatabaseReference ref = myRef.child(username);
                                ref.child("Name").setValue(name);
                                ref.child("Surname").setValue(surname);
                                ref.child("Email").setValue(email);
                                ref.child("Password").setValue(password);

                                if (task.isSuccessful()) {
                                    mainActivityCall();

                                }

                            }


                        });
            }


    public void mainActivityCall(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }



}

