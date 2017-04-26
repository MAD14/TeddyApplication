package it.polito.teddyapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChoiceGroupMembersActivity extends AppCompatActivity {

    Button inviteUser;
    EditText invitedUserEmail;
    private String currentInviteEmail;
    private List<String> invites = new ArrayList<String>();
    private List<String> invites_check = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_group_members);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inviteUser = (Button)findViewById(R.id.invite_button);
        invitedUserEmail   = (EditText)findViewById(R.id.email_invitation);

        inviteUser.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view) {
                        currentInviteEmail = invitedUserEmail.getText().toString();
                        if (isEmailValid(currentInviteEmail)) {
                            invites.add(currentInviteEmail);

                            //onInviteClicked();

                            TextView tvLeft = (TextView) findViewById(R.id.invite_sent);
                            tvLeft.setText("");
                            for (int j = 0; j < invites.size(); j++) {
                                tvLeft.append(invites.get(j) + "\n");
                                invites_check.add("Invitato");
                            }
                            TextView tvRight = (TextView) findViewById(R.id.invite_sent_note);
                            tvRight.setText("");
                            for (int j = 0; j < invites.size(); j++) {
                                tvRight.append(invites_check.get(j) + "\n");
                            }
                        } else {
                            Toast.makeText(ChoiceGroupMembersActivity.this, "Email is not valid",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private boolean isEmailValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }


}