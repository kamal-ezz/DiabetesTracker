package com.p.diabetz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class A1C extends AppCompatActivity {

    protected static A1CDataSource a1CDataSource;

    protected static ArrayList<String[]> a1cInfoArrayList = new ArrayList<String[]>();
    protected static String[] measurments;
    protected static A1CActivityAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1c);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("A1C");

        measurments = new String[4];

        a1CDataSource = new A1CDataSource(this);
        a1CDataSource.open();

        List<DataToStore> values = a1CDataSource.getAllData();

        if (values != null) {

            a1cInfoArrayList.clear();

            for (int i = 0; i < values.size(); i++) {

                a1cInfoArrayList.add(values.get(i).getMesures());

            }
        } else {

            a1cInfoArrayList = new ArrayList<String[]>();
        }

        ListView lv = (ListView) findViewById(R.id.a1cListView);
        adapter = new A1CActivityAdapter(a1cInfoArrayList, getApplicationContext());
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final int itemToDelete = position;

                new AlertDialog.Builder(A1C.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Suppression de la mesure !!")
                        .setMessage(" Voulez vous vraiment supprimer cette mesure ?!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                a1CDataSource.deleteData(A1C.a1cInfoArrayList.get(itemToDelete));

                                A1C.a1cInfoArrayList.remove(itemToDelete);
                                A1C.adapter.notifyDataSetChanged();

                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
                return true;
            }
        });


        FloatingActionButton fab = findViewById(R.id.a1cFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AjouterA1C.class);
                startActivity(intent);
            }
        });
    }




    public static void add(EditText ed1, EditText ed2, EditText ed3, EditText ed4){

        measurments[0] = ed1.getText().toString();
        measurments[1] = ed2.getText().toString();
        measurments[2] = ed3.getText().toString();
        measurments[3] = ed4.getText().toString();


    }

    protected  void onResume(){

        a1CDataSource.open();
        super.onResume();
    }

    class A1CActivityAdapter extends BaseAdapter {
        private ArrayList<String[]> arrayList;
        private LayoutInflater inflter;
        private Context context;

        public A1CActivityAdapter(ArrayList<String[]> arrayList, Context context) {

            this.arrayList = arrayList;
            this.context = context;
            inflter = (LayoutInflater.from(context));
        }


        @Override
        public int getCount() {

            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            view = inflter.inflate(R.layout.row, null);
            TextView tv = (TextView) view.findViewById(R.id.date);
            TextView tv2 = (TextView) view.findViewById(R.id.time);
            TextView tv3 = (TextView) view.findViewById(R.id.notesField); //Note entrée
            TextView tv4 = (TextView) view.findViewById(R.id.amount);
            TextView tv5 = (TextView) view.findViewById(R.id.label); //A1C
            TextView tv6 = (TextView) view.findViewById(R.id.how);
            tv5.setText("A1C");
            tv6.setVisibility(view.INVISIBLE);
            tv.setText((arrayList.get(position))[0]);
            tv2.setText((arrayList.get(position))[1]);
            tv3.setText((arrayList.get(position))[2]);
            tv4.setText((arrayList.get(position))[3]);

            return view;
        }
    }




}
