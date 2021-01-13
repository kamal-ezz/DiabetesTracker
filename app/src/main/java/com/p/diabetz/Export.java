package com.p.diabetz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Export extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Exporter");

    }


    public void glucose(View view){
        Intent intent = new Intent(getApplicationContext(), BilanGlucosePdf.class);
        startActivity(intent);
    }

    public void medicament(View view){
        Intent intent = new Intent(getApplicationContext(), BilanMedicamentsPdf.class);
        startActivity(intent);
    }

    public void tension(View view){
        Intent intent = new Intent(getApplicationContext(), BilanTensionPdf.class);
        startActivity(intent);
    }

    public void poid(View view){
        Intent intent = new Intent(getApplicationContext(), BilanPoidsPdf.class);
        startActivity(intent);
    }

    public void a1c(View view){
        Intent intent = new Intent(getApplicationContext(), BilanA1CPdf.class);
        startActivity(intent);
    }
}
