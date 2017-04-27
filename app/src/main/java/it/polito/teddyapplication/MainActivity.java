package it.polito.teddyapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView list;
    FloatingActionButton fab;
    //
    private String[] groups = {"Group1","Group2","Group3","Group4","Group5","Group6","Group7"};
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
                    //TODO: show the personal information
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

        fab.show();

    }


    public BaseAdapter getAdapterGroups(){
        //TODO: integrazione con firebase
        BaseAdapter bag = new BaseAdapter() {
            @Override
            public int getCount() {return groups.length;}
            @Override
            public Object getItem(int i) {return null;}
            @Override
            public long getItemId(int i) {return 0;}
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getLayoutInflater().inflate(R.layout.group_item,parent,false);

                TextView tv = (TextView) convertView.findViewById(R.id.group_name);
                tv.setText(groups[position]);
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
        //TODO: integrazione con firebase
        //TODO: decidere cosa mettere
        BaseAdapter bag = new BaseAdapter() {
            @Override
            public int getCount() {return 1;}
            @Override
            public Object getItem(int i) {return null;}
            @Override
            public long getItemId(int i) {return 0;}
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getLayoutInflater().inflate(R.layout.personal_section,parent,false);

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
