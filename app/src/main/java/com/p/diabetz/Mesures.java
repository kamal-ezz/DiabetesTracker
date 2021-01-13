package com.p.diabetz;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class Mesures extends Fragment {



    //les icons affichés dans la liste
    private int[] icons = {
            R.drawable.blood,
            R.drawable.medical,
            R.drawable.preasure,
            R.drawable.gym,
            R.drawable.hemoglobin
    };


    private ArrayList<String> larray = new ArrayList<>();



    //un constructeur vide nécessaire
    public Mesures() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.mesures, container, false);

        // obtenir les mesures à suivre
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean g = prefs.getBoolean("switch_preference_1",true);
        boolean m = prefs.getBoolean("switch_preference_2",true);
        boolean t = prefs.getBoolean("switch_preference_3",true);
        boolean p = prefs.getBoolean("switch_preference_4",true);
        boolean a = prefs.getBoolean("switch_preference_5",true);

        larray.add("Glucose");
        larray.add("Médicaments");
        larray.add("Tension");
        larray.add("Poids");
        larray.add("A1C");


        if(!g){
            larray.remove(0);
           }
        else if(!m){
            larray.remove(1);
        }

        else if(!t){
            larray.remove(2);
        }

        else if(!p){
            larray.remove(3);
        }

        else if(!a){
            larray.remove(4);
        }


        ListView lv = (ListView) view.findViewById(R.id.listviewMesure);

        MeasureListAdapter adapt = new MeasureListAdapter(icons, larray, getActivity());

        lv.setAdapter(adapt);


        //lier chaque élement du liste à son propre activité
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView temp = (TextView) view.findViewById(R.id.rowMeasureTextView);

                String pos = temp.getText().toString();


                if (pos.equals("Glucose")) {
                    Intent intent = new Intent(getActivity(), Glucose.class);
                    startActivity(intent);
                } else if (pos.equals("Médicaments")) {
                    Intent intent = new Intent(getActivity(), Medicaments.class);
                    startActivity(intent);
                } else if (pos.equals("Tension")) {
                    Intent intent = new Intent(getActivity(), Tension.class);
                    startActivity(intent);
                } else if (pos.equals("Poids")) {
                    Intent intent = new Intent(getActivity(), Poids.class);
                    startActivity(intent);
                } else if (pos.equals("A1C")) {
                    Intent intent = new Intent(getActivity(), A1C.class);
                    startActivity(intent);
                }
            }
        });


        return view;

    }

    public class MeasureListAdapter extends BaseAdapter {


        private int[] icons;
        private ArrayList<String> measures;
        private LayoutInflater inflter;
        private Context context;
        private ImageView icon;
        private TextView measure;


        public MeasureListAdapter(int[] icons, ArrayList<String> measures, Context context){
            this.icons = icons;
            this.measures = measures;
            this.context = context;
            inflter = LayoutInflater.from(context);
        }




        @Override
        public int getCount() {
            return measures.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.row_measure, null);
            measure = view.findViewById(R.id.rowMeasureTextView);
            icon = view.findViewById(R.id.imageView);
            measure.setText(measures.get(i));
            icon.setImageResource(icons[i]);

            return view;
        }
    }

}