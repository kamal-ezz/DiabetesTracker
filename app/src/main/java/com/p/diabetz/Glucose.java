package com.p.diabetz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Glucose extends AppCompatActivity {

    protected static GlucoseDataSource glucoseDataSource; //c'est ou se trouve la base de données 

    protected static ArrayList<String[]> glucoseInfoArrayList; //il va contenir les infos que l'utilisateur rempli quand'il ajoute un nouveau mésure
    protected static String[] measurments; //il va contenir les infos temporairement avant qu'il seront ajouté au glucoseInforArrayList
    protected static GlucoseActivityAdapter adapter;//il gére la list de mésures

    protected static String mesureUnit; //continet l'unité de mésure

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glucose);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //pour afficher le boutton retour sur l'actionBar

        setTitle("Taux de sucre"); //le titre

        measurments = new String[5];

        glucoseDataSource = new GlucoseDataSource(this);
        glucoseDataSource.open(); //ouvrir la base de données

        glucoseInfoArrayList = new ArrayList<String[]>();
        List<DataToStore> values = glucoseDataSource.getAllData(); //il va stocker les valeurs qui se trouvent dans la base de données

        //values.clear();
        if(values != null){

            glucoseInfoArrayList.clear();

            for(int i = 0; i< values.size(); i++){

                glucoseInfoArrayList.add(values.get(i).getMesures()); //ajouter des valeurs
            }
        } else{

            glucoseInfoArrayList  = new ArrayList<String[]>();
        }

        ListView lv = (ListView) findViewById(R.id.GlucoseListView);// la liste de mésures
        adapter = new GlucoseActivityAdapter(glucoseInfoArrayList,getApplicationContext());
        lv.setAdapter(adapter);


        // recevoir l'unité de mésure depuis les paramètres
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mesureUnit = prefs.getString("mesure_unit","g/l");


        //supprimer une mésure    
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final int itemToDelete = position;

                new AlertDialog.Builder(Glucose.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Suppression de la mesure !!")
                        .setMessage(" Voulez vous vraiment supprimer cette mesure ?!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Log.i("Info", Glucose.glucoseInfoArrayList.get(itemToDelete).toString());
                                glucoseDataSource.deleteData(Glucose.glucoseInfoArrayList.get(itemToDelete));

                                Glucose.glucoseInfoArrayList.remove(itemToDelete);
                                Glucose.adapter.notifyDataSetChanged();

                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
                return true;
            }
        });

        //lancer l'activité de l'ajout de mésure quand le button est clické
        FloatingActionButton fab = findViewById(R.id.glucoseFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AjouterGlucose.class);
                startActivity(intent);
            }
        });
    }

    //ajouter un mésure
    public static void add(EditText ed1, EditText ed2, EditText ed3, EditText ed4, Spinner spinner){


        measurments[0] = ed1.getText().toString();
        measurments[1] = ed2.getText().toString();
        measurments[2] = spinner.getSelectedItem().toString();
        measurments[3] = ed3.getText().toString();
        measurments[4] = ed4.getText().toString() + " " + mesureUnit;
    }

    protected  void onResume(){

        glucoseDataSource.open();
        super.onResume();
    }

    public class GlucoseActivityAdapter extends BaseAdapter {

        private ArrayList<String[]> arrayList;
        private LayoutInflater inflter;
        private Context context;

        public GlucoseActivityAdapter(ArrayList<String[]> arrayList, Context context) {
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
            tv.setText((arrayList.get(position))[0]);
            tv2.setText((arrayList.get(position))[1]);
            tv3.setText((arrayList.get(position))[2]);
            tv4.setText((arrayList.get(position))[3]);
            tv5.setText((arrayList.get(position))[4]);

            return view;
        }}

}