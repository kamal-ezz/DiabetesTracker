package com.p.diabetz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import static com.p.diabetz.Glucose.add;


public class AjouterGlucose extends AppCompatActivity {


    //les composants de la fenêtre
    private EditText ed1;
    private EditText ed2;
    private EditText ed3;
    private EditText ed4;

    private Spinner spinner;

    //les variables qui manipule la date et l'heure
    final private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private TimePickerDialog timePickerDialog;
    private int currentHour;
    private int currentMinutes;
    private String amPm;

    static DataToStore dataToStore = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_glucose);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Taux de sucre");


        ed1 = (EditText) findViewById(R.id.dateEditText);
        ed2 = (EditText) findViewById(R.id.hourEditText);
        ed3 = (EditText) findViewById(R.id.notesEditText);
        ed4 = (EditText) findViewById(R.id.sugarConcentrationEditText);

        spinner = findViewById(R.id.glucoseSpinner);

        // les évenements au moment de mésure
        ArrayList<String> array = new ArrayList<String>(); 
        array.add("Avant petit-déjeuner");
        array.add("Après petit-déjeuner");
        array.add("Avant déjeuner");
        array.add("Après déjeuner");
        array.add("Avant dinner");
        array.add("Après dinner");
        array.add("Jeune");

        // ajouter les élements de l'array au spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //les actions éffectués quand le button ajouter est pressé
        Button btn = (Button) findViewById(R.id.GlucoseButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ed4.getText().toString().isEmpty()){
                    new AlertDialog.Builder(AjouterGlucose.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Attention")
                            .setMessage("Il faut préciser la concentration")
                            .setPositiveButton("Ok", null)
                            .show();
                }else {
                    add(ed1, ed2, ed3, ed4, spinner);

                    dataToStore = Glucose.glucoseDataSource.createData(Glucose.measurments);

                    Glucose.glucoseInfoArrayList.add(dataToStore.getMesures());

                    Glucose.GlucoseActivityAdapter adapter = Glucose.adapter;

                    adapter.notifyDataSetChanged();

                    onBackPressed();

                }
            }
        });

        Date now = new Date();

        ed1.setText(updateDate(now));//définir la date

        ed2.setText(updateTime(now));//définir l'heure

        //affichier le sélécteur de la date
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                ed1.setText(updateDate(myCalendar.getTime()));
            }
        };

        ed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentYear = myCalendar.get(Calendar.YEAR);
                currentMonth = myCalendar.get(Calendar.MONTH);
                currentDay = myCalendar.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(AjouterGlucose.this, date, currentYear , currentMonth, currentDay).show();
            }
        });

        //affichier le sélécteur de l'heure
        ed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentHour = myCalendar.get(Calendar.HOUR_OF_DAY);
                currentMinutes = myCalendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AjouterGlucose.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        if(i >= 12){

                            amPm = "PM";
                        }else{

                            amPm = "AM";
                        }

                        ed2.setText(String.format("%02d:%02d ", i, i1) + amPm);
                    }
                }, currentHour, currentMinutes, false);
                timePickerDialog.show();
            }
        });

    }


    private String updateDate(Date date) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = dateFormatter.format(date);
        return formattedDate;
    }

    private String updateTime(Date date) {

        DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT);
        String formattedTime = timeFormatter.format(date);
        return formattedTime;
    }

}

