package it.polito.teddyapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class InviteUsersToGroup extends AppCompatActivity{

    private ArrayList<String> emailsToBeSent;
    private String[] listAddress = {""};
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
            public Object getItem(int position) {return emailsToBeSent.get(position);}
            @Override
            public long getItemId(int position) {return 0;}
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getLayoutInflater().inflate(R.layout.contact_item, parent, false);
                TextView tv = (TextView) findViewById(R.id.tv_email_contact);
//                tv.setText(emailsToBeSent.get(position));
                return convertView;
            }
        });
    }

    public void onClickInvite(View view) throws Exception{
        EditText et = (EditText) findViewById(R.id.edit_text_invite);
        String emailInserted = et.getText().toString();
        if (isEmailValid(emailInserted)) {
            listAddress[0] = emailInserted;
            emailsToBeSent.add(emailInserted);
            list.invalidate();
            list.requestLayout();
            Toast.makeText(InviteUsersToGroup.this, "Email sent",
                    Toast.LENGTH_SHORT).show();
            final InviteMail inviteMail = new InviteMail();

            // Possibility1 (P1)
            /*new AsyncTask<Void, Void, Void>() {
                @Override
                public Void doInBackground(Void... arg) {
                    try {
                        inviteMail.set_to(listAddress);
                        inviteMail.send();
                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                    }
                    return null;
                }
            }.execute(); */
            // end P1
            // Possibility2 (P2)
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        Log.e("SendMail","set_to" + listAddress[0]);

                        inviteMail.set_to(listAddress);
                        inviteMail.send();
                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                    }
                }
            };

            Thread t = new Thread(r);
            t.start();
            // end P2
        } else{
            Toast.makeText(InviteUsersToGroup.this, "Email not valid.",
                    Toast.LENGTH_SHORT).show();
        }

    }

//    public void onClickCompletedAction(View view) {
//        // TODO: scrittura sul db
//        // TODO: invio mails (ci penso io :) )
//
//        Toast.makeText(InviteUsersToGroup.this, "OK"+ emailsToBeSent.get(0),
//                Toast.LENGTH_SHORT).show();
//    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private boolean isEmailValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

}
