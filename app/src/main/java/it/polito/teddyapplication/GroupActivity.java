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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    private ListView list;
    private List<ExpenseItem> expenses;
    private List<DebitsItem> debits;

    private int indexExp=0;
    private String groupname;


    // these arrays of strings are used to populate the List "expenses" and "debits" that are then shown in the app
    // they will be then substituted by the data coming from the database
    private String[] expenses_base = {"Expense1", "Expense2", "Expense3", "Expense4", "Expense5"};
    private String[] value_expenses_base = {"13.50€", "1.50€", "21.00€", "8.30€", "15.00€"};
    private String[] names_base = {"Michela", "Matteo", "Simona", "Greta", "Agnese","Stefania","Elena","Martina","Laura","Monica","Letizia"};
    private String description = "Lorem ipsum dolor sit amet, ...";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_expenses_section:
                    list = (ListView) findViewById(R.id.lv_expenses);
                    BaseAdapter bae = getAdapterExpenses();
                    list.setAdapter(bae);
                    return true;
                case R.id.navigation_summary_section:
                    list = (ListView) findViewById(R.id.lv_expenses);
                    BaseAdapter bad = getAdapterDebit();
                    list.setAdapter(bad);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent myIntent = getIntent();
        groupname = myIntent.getStringExtra("groupname");

        expenses = new ArrayList<>();
        debits = new ArrayList<>();

        for (int i=0;i<5;i++){
            DebitsItem debit_tmp = new DebitsItem(names_base[i],value_expenses_base[i]);
            debits.add(debit_tmp);
            expenses.add(new ExpenseItem(expenses_base[i],value_expenses_base[i],description,names_base[i]));
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_group);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        list = (ListView) findViewById(R.id.lv_expenses);
        BaseAdapter bae = getAdapterExpenses();
        list.setAdapter(bae);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("groups/" + groupname + "/items");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ExpenseCreation.class);
                intent.putExtra("groupname", groupname);
                //startActivityForResult(intent, 1);
                startActivity(intent);
            }});
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    expenses.add(indexExp,
                                    new ExpenseItem(data.child("Name").getValue().toString(),
                                            data.child("Price").getValue().toString(),
                                            data.child("Description").getValue().toString(),
                                            data.child("Author").getValue().toString()));
                    indexExp++;
                }

                list.invalidate();

                //questa assegnazione è da mettere di nuovo?? TODO
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//                fab.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(view.getContext(), ExpenseCreation.class);
//                        intent.putExtra("groupname",groupname);
//                        startActivityForResult(intent, 1);
//                    }
//                });
                fab.show();
            }

            public void onCancelled(DatabaseError error) {
                Log.w("Failed to read value.", error.toException());

            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String str_name = data.getStringExtra("name");
                String str_import = data.getStringExtra("import")+"€";
                String str_description = data.getStringExtra("description");
                String str_author = data.getStringExtra("author");
                ExpenseItem expense_tmp = new ExpenseItem(str_name,str_import,str_description,str_author);
                expense_tmp.setAuthor(str_author);
                expenses.add(expense_tmp);
            }
        }
    }


    protected BaseAdapter getAdapterExpenses() {
        BaseAdapter bae = new BaseAdapter() {
            @Override
            public int getCount() {
                return expenses.size();
            }

            @Override
            public Object getItem(int position) {
                return expenses.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getLayoutInflater().inflate(R.layout.expense_item, parent, false);
                TextView tv = (TextView) convertView.findViewById(R.id.expense_name);
                tv.setText(expenses.get(position).getName());
                tv = (TextView) convertView.findViewById(R.id.expense_import);
                tv.setText(expenses.get(position).getValue());
                tv = (TextView) convertView.findViewById(R.id.expense_description);
                tv.setText(expenses.get(position).getDescription());

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.show();
                return convertView;
            }
        };
        return bae;
    }

    protected BaseAdapter getAdapterDebit() {
        BaseAdapter bad = new BaseAdapter() {
            @Override
            public int getCount() {
                return debits.size();
            }

            @Override
            public Object getItem(int position) {
                return debits.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getLayoutInflater().inflate(R.layout.personal_summary_item, parent, false);

                TextView tv = (TextView) convertView.findViewById(R.id.summary_name);
                tv.setText(expenses.get(position).getAuthor());
                tv = (TextView) convertView.findViewById(R.id.summary_import);
                tv.setText("-"+expenses.get(position).getValue());
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.hide();
                return convertView;
            }
        };
        return bad;
    }



}
