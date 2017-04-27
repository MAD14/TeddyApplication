package it.polito.teddyapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polito.teddyapplication.R;

public class InviteUsersToGroup extends AppCompatActivity {

    private ArrayList<String> emailsToBeSent;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_users_to_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emailsToBeSent = new ArrayList<>();

        list = (ListView) findViewById(R.id.lv_invitation);
        list.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {return emailsToBeSent.size();}
            @Override
            public String getItem(int position) {return emailsToBeSent.get(position);}
            @Override
            public long getItemId(int position) {return 0;}
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getLayoutInflater().inflate(R.layout.contact_item, parent, false);
                TextView tv = (TextView) findViewById(R.id.tv_email_contact);
                tv.setText(emailsToBeSent.get(position));
                return convertView;
            }
        });


        findViewById(R.id.completed_operation_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: scrittura sul db
                // TODO: invio mails (ci penso io :) )

                Toast.makeText(InviteUsersToGroup.this, "OK"+ emailsToBeSent.get(0).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void onClickCompletedAction(View view) {
        EditText et = (EditText) findViewById(R.id.edit_text_invite);
        String emailInserted = et.getText().toString();
        if (isEmailValid(emailInserted)) {
            //inserisci nella lista e fai aggiornare la vista della lista
            emailsToBeSent.add(emailInserted);
            list.invalidate();
            list.requestLayout();
        } else{
            // messaggio di errore
            Toast.makeText(InviteUsersToGroup.this, "Email not valid.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private boolean isEmailValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

}
