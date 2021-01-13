package com.p.diabetz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BilanTensionPdf extends AppCompatActivity {

    File file;

    static ArrayList<String[]> infoToStore = new ArrayList<String[]>();

    static TensionDataSource tensionDataSourcePdf;

    String[] informationArray = new String[]{"Date", "Heure", "SisToLicPre", "Note", "DiasToLicPre", "Unknown"};

    Bitmap bmp, scaledBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilan_tension_pdf);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.tensionpdf);
        scaledBitmap = Bitmap.createScaledBitmap(bmp, 100, 70, false);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        tensionDataSourcePdf = new TensionDataSource(this);
        tensionDataSourcePdf.open();

        infoToStore = new ArrayList<String[]>();

        List<DataToStore> values = tensionDataSourcePdf.getAllData();

        if (values != null) {

            for (int i = 0; i < values.size(); i++) {

                infoToStore.add(values.get(i).getMesures());
            }
        } else {

            infoToStore = new ArrayList<String[]>();
        }

        createPdf(infoToStore);

        File file = new File(Environment.getExternalStorageDirectory() + dir + "/Tension.pdf");

        if (file.exists()) {

            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Install a pdf reader", Toast.LENGTH_LONG).show();
            }
        } else {

            Toast.makeText(getApplicationContext(), "File path is not correct", Toast.LENGTH_LONG).show();
        }
    }

    String dir = "/Download";

    public void createPdf(ArrayList<String[]> allData) {

        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        ArrayList<PdfDocument.PageInfo> pagesInfo = new ArrayList<>();
        ArrayList<PdfDocument.Page> pages = new ArrayList<>();


        int k = 7, l = 0;
        int max;

        if (allData.size() % 7 == 0) {
            max = (int) (allData.size() / 7);
        } else {
            max = (int) (allData.size() / 7) + 1;
        }

        for (int j = 0; j < max; j++) {

            if (k >= allData.size()) {
                k = allData.size();
            } else {
            }

            pagesInfo.add(new PdfDocument.PageInfo.Builder(300, 450, j + 1).create());
            pages.add(myPdfDocument.startPage(pagesInfo.get(j)));

            Canvas canvas = pages.get(j).getCanvas();

            myPaint.setTextAlign(Paint.Align.LEFT);
            myPaint.setTextSize(11.0f);
            myPaint.setColor(Color.BLACK);
            canvas.drawText("Rapport: Tension", 115, 50, myPaint);

            canvas.drawBitmap(scaledBitmap, 3, 15, myPaint);

            int startXPosition = 10;
            int endXPosition = pagesInfo.get(j).getPageWidth() - 10;
            int startYPosition = 110;
            int startSecondTextX = 90;

            float verticalStartY = 105;
            float verticalEndY = 145;

            for (int i = l; i < k; i++) {

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(6.0f);
                myPaint.setColor(Color.BLACK);

                for (int p = 0; p < 5; p++) {

                    canvas.drawText(informationArray[p], startXPosition, startYPosition, myPaint);
                    canvas.drawText(allData.get(i)[p], startSecondTextX, startYPosition, myPaint);
                    canvas.drawLine(startXPosition, startYPosition + 2, endXPosition, startYPosition + 2, myPaint);

                    startYPosition += 8;
                }

                canvas.drawLine(80, verticalStartY, 80, verticalEndY, myPaint);
                startYPosition += 5;
                verticalStartY += 45;
                verticalEndY += 45;

            }

            myPdfDocument.finishPage(pages.get(j));

            l = k;
            k = k * 2;

        }

        file = new File(Environment.getExternalStorageDirectory() + dir, "/Tension.pdf");

        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        myPdfDocument.close();

    }
}
