package com.p.diabetz;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * A simple {@link Fragment} subclass.
 */
public class Donnees extends Fragment {

    //pour les bases de données
    private GlucoseDataSource glucoseDataSource;
    private TensionDataSource tensionDataSource;
    private PoidsDataSource weightDataSource;
    private A1CDataSource a1CDataSource;


    public Donnees() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.donnees, container, false);

        final GraphView graph = (GraphView) view.findViewById(R.id.graph1); //le graphe

        final ArrayList<String> spinnerList = new ArrayList<>();


        spinnerList.add("Glucose");
        spinnerList.add("Tension");
        spinnerList.add("Poids");
        spinnerList.add("A1C");

        glucoseDataSource = new GlucoseDataSource(getActivity());
        tensionDataSource = new TensionDataSource(getActivity());
        weightDataSource = new PoidsDataSource(getActivity());
        a1CDataSource = new A1CDataSource(getActivity());

        //ils apparaissent quand le tension est séléctioné
        final TextView tvs = (TextView) view.findViewById(R.id.graphTsis);
        final TextView tvd = (TextView) view.findViewById(R.id.graphTdias);

        Spinner spinner = (Spinner) view.findViewById(R.id.DataSpinner);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(listAdapter);


        //les graphes correspandants à chaque élement de spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                List<DataToStore> values;


                switch (position) {
                    case 0:
                        glucoseDataSource.open();
                        values = glucoseDataSource.getAllData();
                        graph.removeAllSeries();
                        plot(graph, values, series, 4);
                        tvs.setVisibility(View.INVISIBLE);
                        tvd.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        tensionDataSource.open();
                        values = tensionDataSource.getAllData();
                        graph.removeAllSeries();
                        plot(graph, values, series, 5);
                        tvs.setVisibility(View.VISIBLE);
                        tvd.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        weightDataSource.open();
                        values = weightDataSource.getAllData();
                        graph.removeAllSeries();
                        plot(graph, values, series, 3);
                        tvs.setVisibility(View.INVISIBLE);
                        tvd.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        a1CDataSource.open();
                        values = a1CDataSource.getAllData();
                        graph.removeAllSeries();
                        plot(graph, values, series, 3);
                        tvs.setVisibility(View.INVISIBLE);
                        tvd.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        values = null;
                        tvs.setVisibility(View.INVISIBLE);
                        tvd.setVisibility(View.INVISIBLE);
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        return view;
    }

    //pour dessiner les graphes
    private void plot(GraphView graph, final List<DataToStore> values, LineGraphSeries<DataPoint> series, int n) {

        double y, x, z;
        x = 0;


        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>();

        if (values != null) {

            for (int i = 0; i < values.size(); i++) {
                x = x + 1;
                String str = values.get(i).getMesures()[n];
                String[] arr = str.split(" ");
                y = Double.parseDouble(arr[0]);
                series.appendData(new DataPoint(x, y), true, 100);

                if (n == 5) {
                    z = Double.parseDouble(values.get(i).getMesures()[4]);
                    series2.appendData(new DataPoint(x, z), true, 100);
                }

            }


            series2.setColor(Color.RED);

            graph.addSeries(series);
            graph.addSeries(series2);


            String[] dates = new String[values.size()];

            for (int j = 0; j < values.size(); j++) {
                String date = values.get(j).getMesures()[0];
                dates[j] = Month(date) + " " + date.substring(8, 10);
            }

            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            if (dates.length >= 2) {
                staticLabelsFormatter.setHorizontalLabels(dates);
                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            } else if(dates.length > 0){
                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            String date = values.get(0).getMesures()[0];
                            return Month(date) + " " + date.substring(8, 10);
                        }
                        return super.formatLabel(value, isValueX);

                    }
                });
            }
            graph.getGridLabelRenderer().setNumHorizontalLabels(values.size());

        }


    }

        public void onResume(){
            glucoseDataSource.open();
            weightDataSource.open();
            tensionDataSource.open();
            a1CDataSource.open();
            super.onResume();
        }


        //afichier le mois
    private String Month(String str){
        String getMonth = str.substring(5,7);
        String month = "";
        if(getMonth.equals("01"))
            month = "Janvier";
        else if(getMonth.equals("02"))
            month = "Février";
        else if(getMonth.equals("03"))
            month = "Mars";
        else if(getMonth.equals("04"))
            month = "Avril";
        else if(getMonth.equals("05"))
            month = "Mai";
        else if(getMonth.equals("06"))
            month = "Juin";
        else if(getMonth.equals("07"))
            month = "Juillet";
        else if(getMonth.equals("08"))
            month = "Aout";
        else if(getMonth.equals("09"))
            month = "Septembre";
        else if(getMonth.equals("10"))
            month = "Octobre";
        else if(getMonth.equals("11"))
            month = "Novembre";
        else if(getMonth.equals("12"))
            month = "Décembre";

        return month;
    }

}