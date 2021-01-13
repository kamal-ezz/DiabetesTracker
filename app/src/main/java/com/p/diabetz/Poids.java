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

public class Poids extends AppCompatActivity {

    protected static PoidsDataSource poidsDataSource;

    protected static ArrayList<String[]> weightInfoArrayList;
    protected static String[] measurments;
    protected static WeightActivityAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poids);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Poids");

        weightInfoArrayList = new ArrayList<String[]>();

        measurments = new String[5];

        poidsDataSource = new PoidsDataSource(this);
        poidsDataSource.open();

        List<DataToStore> values =  poidsDataSource.getAllData();

        if(values != null){


            weightInfoArrayList.clear();

            for(int i = 0; i< values.size(); i++){

                weightInfoArrayList.add(values.get(i).getMesures());

            }
        } else{

            weightInfoArrayList  = new ArrayList<String[]>();
        }

        ListView lv = (ListView) findViewById(R.id.weightListView);
        adapter = new WeightActivityAdapter(weightInfoArrayList,getApplicationContext());
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final int itemToDelete = position;

                new AlertDialog.Builder(Poids.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Suppression de la mesure !!")
                        .setMessage(" Voulez vous vraiment supprimer cette mesure ?!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                poidsDataSource.deleteData(Poids.weightInfoArrayList.get(itemToDelete));

                                Poids.weightInfoArrayList.remove(itemToDelete);
                                Poids.adapter.notifyDataSetChanged();

                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
                return true;
            }
        });


        FloatingActionButton fab = findViewById(R.id.weightFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AjouterPoids.class);
                startActivity(intent);
            }
        });
    }


    public static void add(EditText ed1, EditText ed2, EditText ed3, EditText ed4){
        measurments[0] = ed1.getText().toString();
        measurments[1] = ed2.getText().toString();
        measurments[2] = ed3.getText().toString();
        measurments[3] = ed4.getText().toString() + " " + "Kg";

    }

    protected void onResume(){

        poidsDataSource.open();
        super.onResume();
    }

    public class WeightActivityAdapter extends BaseAdapter {

        private ArrayList<String[]> arrayList;
        private LayoutInflater inflter;
        private Context context;

        public WeightActivityAdapter(ArrayList<String[]> arrayList, Context context) {
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
            TextView tv4 = (TextView) view.findViewById(R.id.notesField);
            TextView tv5 = (TextView) view.findViewById(R.id.amount);
            TextView tv6 = (TextView) view.findViewById(R.id.how);
            tv6.setVisibility(View.INVISIBLE);
            TextView tv7 = (TextView) view.findViewById(R.id.label);
            tv7.setText("Poids");
            tv.setText((arrayList.get(position))[0]);
            tv2.setText((arrayList.get(position))[1]);
            tv4.setText((arrayList.get(position))[2]);
            tv5.setText((arrayList.get(position))[3]);

            return view;
        }
    }
}
