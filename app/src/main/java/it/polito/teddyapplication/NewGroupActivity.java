package it.polito.teddyapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import it.polito.teddyapplication.R;

public class NewGroupActivity extends AppCompatActivity {

    Button CreateGroup;
    EditText EditName;
    EditText EditDescription;
    private String GroupName;
    private String GroupDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CreateGroup = (Button) findViewById(R.id.group_create_button);
        EditName = (EditText) findViewById(R.id.group_name);
        EditDescription = (EditText) findViewById(R.id.group_description);

        CreateGroup.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        GroupName = EditName.getText().toString();
                        if (EditIsAlphanumeric(GroupName)) {
                            GroupDescription = EditDescription.getText().toString();
                            Intent intent = new Intent(NewGroupActivity.this, ChoiceGroupMembersActivity.class);
                            intent.putExtra("groupname", GroupName);
                            intent.putExtra("groupdescription", GroupDescription);
                            startActivity(intent);
                        } else {
                            GroupName = "";
                            Toast.makeText(NewGroupActivity.this, "Group Name is not valid.\nMust contains numbers or letters",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }


                });
    }

    private boolean EditIsAlphanumeric(String ToControl) {
        //TODO: Replace this with your own logic
        return ToControl.replaceAll("\\s+","").matches("[a-zA-Z0-9]+");
    }




}
