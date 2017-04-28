package it.polito.teddyapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView list;
    FloatingActionButton fab;
    private String email;
    private String username;
    private List<String> groups;
    private List<String> descriptions;
    private List<DebitsItem> personal;
    private int indexGroup=0;
    private int indexPersonal=0;
    //
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_group_section:
                    //shows the groups list and link their tap to the group activity
                    list = (ListView) findViewById(R.id.list_view_main_activity);
                    list.setAdapter(getAdapterGroups());
                    fab.show();
                    return true;
                case R.id.navigation_personal_section:
                    //TODO: show the personal information which one?
                    list = (ListView) findViewById(R.id.list_view_main_activity);
                    list.setAdapter(getAdapterPersonal());
                    fab.hide();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent myIntent = getIntent();
        email=myIntent.getStringExtra("email");

        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();

        groups = new ArrayList<>();
        descriptions= new ArrayList<>();
        personal=new ArrayList<>();
        username=getUserName();



        // questo fab dovrà avere due funzioni diverse a seconda di dove si troverà:
        // nella sezione dei gruppi aggiungerà un gruppo
        // nella sezione personale dovrà aggiungere una spesa (opp lo togliamo e aggiungiamo un altro tipo di bottone per la spesa!)
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),NewGroupActivity.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/"+username+"/groups");
        Toast.makeText(getApplicationContext(), myRef.toString(), Toast.LENGTH_SHORT).show();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!groups.contains(data.getKey())) {
                        Toast.makeText(getApplicationContext(),data.getKey(), Toast.LENGTH_SHORT).show();
                        groups.add(indexGroup, data.getKey());
                        descriptions.add(indexGroup, data.child("Description").getValue().toString());
                        indexGroup++;
                    }
                }
                //shows the groups list and link their tap to the group activity
                list = (ListView) findViewById(R.id.list_view_main_activity);
                list.setAdapter(getAdapterGroups());
                list.setClickable(true);

        /*list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id){
                Intent intent =new Intent(getApplicationContext(),GroupActivity.class);
                startActivity(intent);}

            });*/

            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Failed to read value.", error.toException());
            }
        });


        DatabaseReference myRef2 = database.getReference("users/"+username+"/personal");
        Toast.makeText(getApplicationContext(),myRef2.toString(), Toast.LENGTH_SHORT).show();
        if(myRef2==null){
            personal.add(new DebitsItem("No item inserted yet","0"));
        }else {

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                            personal.add(indexPersonal, new DebitsItem(data.getKey(),data.child("Price").toString()));
                            indexPersonal++;

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("Failed to read value.", error.toException());
                }
            });
        }

        fab.show();
    }

    public String getUserName(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), data.child("Email").toString(), Toast.LENGTH_SHORT).show();
                    if (data.child("Email").getValue().toString().equals(email)) {
                        username = data.getKey();
                        Toast.makeText(getApplicationContext(), username, Toast.LENGTH_SHORT).show();
                    } else {
                        //problems
                    }

                }
            }

            public void onCancelled(DatabaseError error) {
                Log.w("Failed to read value.", error.toException());

            }
        });

        return username;
    }


    public BaseAdapter getAdapterGroups(){

        BaseAdapter bag = new BaseAdapter() {
            @Override
            public int getCount() {return groups.size();}
            @Override
            public Object getItem(int i) {return groups.get(i);}
            @Override
            public long getItemId(int i) {return 0;}
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getLayoutInflater().inflate(R.layout.group_item,parent,false);

                TextView tv = (TextView) convertView.findViewById(R.id.group_name);
                tv.setText(groups.get(position));
                tv = (TextView) convertView.findViewById(R.id.group_description);
                tv.setText(descriptions.get(position));
                tv = (TextView) convertView.findViewById(R.id.group_summary1);
                tv.setText("+" + String.valueOf(position));
                tv = (TextView) convertView.findViewById(R.id.group_summary2);
                tv.setText("-"+ String.valueOf(position));
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                return convertView;
            }
        };
        return bag;
    }

    public BaseAdapter getAdapterPersonal(){
        BaseAdapter bag = new BaseAdapter() {
            @Override
            public int getCount() {return personal.size();}
            @Override
            public Object getItem(int i) {return personal.get(i);}
            @Override
            public long getItemId(int i) {return 0;}
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getLayoutInflater().inflate(R.layout.personal_section,parent,false);
                    // TODO da inserire la proper view degli elementi expense
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                return convertView;
            }
        };
        return bag;
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(view.getContext(),GroupActivity.class);
        startActivity(intent);
    }
    //TODO: logout temporaneo!!!!
    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(view.getContext(),LoginActivity.class);
        startActivity(intent);
    }
}
