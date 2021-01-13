package com.p.diabetz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import static com.p.diabetz.A1C.add;


public class AjouterA1C extends AppCompatActivity {


    private EditText ed1;
    private EditText ed2;
    private EditText ed3;
    private EditText ed4;

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
        setContentView(R.layout.ajouter_a1c);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("A1C");



        ed1 = (EditText) findViewById(R.id.a1cDate);
        ed2 = (EditText) findViewById(R.id.a1cTime);
        ed3 = (EditText) findViewById(R.id.a1cNotes);
        ed4 = (EditText) findViewById(R.id.a1cAvgConcentration);



        Button btn = (Button) findViewById(R.id.a1cAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ed4.getText().toString().isEmpty()){
                    new AlertDialog.Builder(AjouterA1C.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Attention")
                            .setMessage("Il faut préciser la concentration")
                            .setPositiveButton("Ok", null)
                            .show();
                }else {

                    add(ed1, ed2, ed3, ed4);

                    dataToStore = A1C.a1CDataSource.createData(A1C.measurments);

                    A1C.a1cInfoArrayList.add(dataToStore.getMesures());

                    A1C.A1CActivityAdapter adapter = A1C.adapter;

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

                new DatePickerDialog(AjouterA1C.this, date, currentYear , currentMonth, currentDay).show();
            }
        });


        ed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentHour = myCalendar.get(Calendar.HOUR_OF_DAY);
                currentMinutes = myCalendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AjouterA1C.this, new TimePickerDialog.OnTimeSetListener() {
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
