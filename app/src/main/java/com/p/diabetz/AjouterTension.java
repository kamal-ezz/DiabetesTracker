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

public class AjouterTension extends AppCompatActivity {




    private EditText ed1;
    private EditText ed2;
    private EditText ed3;
    private EditText ed4;
    private EditText ed5;

    private Spinner spinner;

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
        setContentView(R.layout.ajouter_tension);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Tension");


        ed1 = (EditText) findViewById(R.id.prDate);
        ed2 = (EditText) findViewById(R.id.prTime);
        ed3 = (EditText) findViewById(R.id.prNotes);
        ed4 = (EditText) findViewById(R.id.systolicPr);
        ed5 = (EditText) findViewById(R.id.diastolicPr);

        spinner = findViewById(R.id.prSpinner);

        ArrayList<String> array = new ArrayList<String>();
        array.add("Droit");
        array.add("Gauche");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button btn = (Button) findViewById(R.id.prAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ed4.getText().toString().isEmpty() && ed5.getText().toString().isEmpty()){
                    new AlertDialog.Builder(AjouterTension.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Attention")
                            .setMessage("Il faut préciser la pression")
                            .setPositiveButton("Ok", null)
                            .show();
                }else {

                    Tension.add(ed1, ed2, ed3, ed4, ed5, spinner);

                    dataToStore = Tension.tensionDataSource.createData(Tension.measurments);

                    Tension.prInfoArrayList.add(dataToStore.getMesures());

                    Tension.PressureActivityAdapter adapter = Tension.adapter;

                    adapter.notifyDataSetChanged();

                    onBackPressed();
                }

            }
        });



        Date now = new Date();

        ed1.setText(updateDate(now));

        ed2.setText(updateTime(now));

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

                new DatePickerDialog(AjouterTension.this, date, currentYear , currentMonth, currentDay).show();
            }
        });


        ed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentHour = myCalendar.get(Calendar.HOUR_OF_DAY);
                currentMinutes = myCalendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AjouterTension.this, new TimePickerDialog.OnTimeSetListener() {
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
