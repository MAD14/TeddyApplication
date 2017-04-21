package it.polito.teddyapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_group_section:
                    mTextMessage.setText(R.string.group_section);
                    return true;
                case R.id.navigation_personal_section:
                    mTextMessage.setText(R.string.personal_section);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        mTextMessage = (TextView) findViewById(R.id.message);
        mTextMessage.setText(email);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // questo fab dovrà avere due funzioni diverse a seconda di dove si troverà:
        // nella sezione dei gruppi aggiungerà un gruppo
        // nella sezione personale dovrà aggiungere una spesa (opp lo togliamo e aggiungiamo un altro tipo di bottone per la spesa!)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NewGroupActivity.class);
                startActivity(intent);
            }
        });
    }

}
