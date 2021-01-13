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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Tension extends AppCompatActivity {

    protected static TensionDataSource tensionDataSource;

    protected static ArrayList<String[]> prInfoArrayList;
    protected static String[] measurments;
    protected static PressureActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tension);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Tension");

        prInfoArrayList = new ArrayList<String[]>();
        measurments = new String[6];

        tensionDataSource = new TensionDataSource(this);
        tensionDataSource.open();

        List<DataToStore> values = tensionDataSource.getAllData();

        if(values != null){

            prInfoArrayList.clear();

            for(int i = 0; i< values.size(); i++){

                prInfoArrayList.add(values.get(i).getMesures());
            }
        } else{

            prInfoArrayList  = new ArrayList<String[]>();
        }

        ListView lv = (ListView) findViewById(R.id.prListView);
        adapter = new PressureActivityAdapter(prInfoArrayList,getApplicationContext());
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final int itemToDelete = position;

                new AlertDialog.Builder(Tension.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Suppression de la mesure !!")
                        .setMessage(" Voulez vous vraiment supprimer cette mesure ?!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                tensionDataSource.deleteData(Tension.prInfoArrayList.get(itemToDelete));

                                Tension.prInfoArrayList.remove(itemToDelete);
                                Tension.adapter.notifyDataSetChanged();

                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
                return true;
            }
        });


        FloatingActionButton fab = findViewById(R.id.prFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AjouterTension.class);
                startActivity(intent);
            }
        });
    }


    public static void add(EditText ed1, EditText ed2, EditText ed3, EditText ed4, EditText ed5, Spinner spinner){
        measurments[0] = ed1.getText().toString();
        measurments[1] = ed2.getText().toString();
        measurments[2] = spinner.getSelectedItem().toString();
        measurments[3] = ed3.getText().toString();
        measurments[4] = ed4.getText().toString();
        measurments[5] = ed5.getText().toString();

    }

    protected  void onResume(){

        tensionDataSource.open();
        super.onResume();
    }

    public class PressureActivityAdapter extends BaseAdapter {
        private ArrayList<String[]> arrayList;
        private LayoutInflater inflter;
        private Context context;

        public PressureActivityAdapter(ArrayList<String[]> arrayList, Context context) {
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
            TextView tv3 = (TextView) view.findViewById(R.id.how);
            TextView tv4 = (TextView) view.findViewById(R.id.notesField);
            TextView tv5 = (TextView) view.findViewById(R.id.amount);
            TextView tv6 = (TextView) view.findViewById(R.id.label);
            tv6.setText("Bras");;
            TextView tv7 = (TextView) view.findViewById(R.id.amount2);
            tv7.setVisibility(View.VISIBLE);
            tv.setText((arrayList.get(position))[0]);
            tv2.setText((arrayList.get(position))[1]);
            tv3.setText((arrayList.get(position))[2]);
            tv4.setText((arrayList.get(position))[3]);
            tv5.setText((arrayList.get(position))[4]);
            tv7.setText((arrayList.get(position))[5]);

            return view;
        }
    }

}
