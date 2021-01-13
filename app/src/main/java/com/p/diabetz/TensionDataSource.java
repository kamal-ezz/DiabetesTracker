package com.p.diabetz;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TensionDataSource {

    // Champs de la base de données
    private SQLiteDatabase database;
    private PressureSQLHelper dbHelper;
    private String[] allColumns = { PressureSQLHelper.COLUMN_ID, PressureSQLHelper.COLUMN_DATE, PressureSQLHelper.COLUMN_HOUR,
            PressureSQLHelper.COLUMN_UNKNOWN, PressureSQLHelper.COLUMN_NOTE, PressureSQLHelper.COLUMN_SIS, PressureSQLHelper.COLUMN_DIAS};

    Cursor cursor;

    public TensionDataSource(Context context) {

        dbHelper = new PressureSQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public DataToStore createData (String[] mesures){

        ContentValues values = new ContentValues();

        values.put(PressureSQLHelper.COLUMN_DATE, mesures[0]);
        values.put(PressureSQLHelper.COLUMN_HOUR, mesures[1]);
        values.put(PressureSQLHelper.COLUMN_UNKNOWN, mesures[2]);
        values.put(PressureSQLHelper.COLUMN_NOTE, mesures[3]);
        values.put(PressureSQLHelper.COLUMN_SIS, mesures[4]);
        values.put(PressureSQLHelper.COLUMN_DIAS, mesures[5]);

        long insertId = database.insert(PressureSQLHelper.TABLE_PRESSURE, null, values);
        cursor = database.query(PressureSQLHelper.TABLE_PRESSURE,
                allColumns, PressureSQLHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        DataToStore newDataToStore = cursorToData(cursor);
        cursor.close();
        return newDataToStore;
    }

    public void deleteData(String[] mesures){

        //Log.i("Info", "date et heure " + mesures[1] + " " + mesures[2] );
        database.delete(PressureSQLHelper.TABLE_PRESSURE, PressureSQLHelper.COLUMN_DATE + " = " + "'" + mesures[0]
                + "'" + " AND " + PressureSQLHelper.COLUMN_HOUR  + " = " + "'" + mesures[1] + "'" , null);
    }

    public List<DataToStore> getAllData(){

        List<DataToStore> data = new ArrayList<DataToStore>();

        Cursor cursor = database.query(PressureSQLHelper.TABLE_PRESSURE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DataToStore dataToStore = cursorToData(cursor);
            data.add(dataToStore);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return data;
    }

    private DataToStore cursorToData(Cursor cursor){

        DataToStore dataToStore = new DataToStore();

        String[] tab = {cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6)};

        dataToStore.setMesures(tab);

        return dataToStore;
    }

    public class PressureSQLHelper extends SQLiteOpenHelper {

        public static final String TABLE_PRESSURE = "pression";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_SIS = "sisToLicPre";
        public static final String COLUMN_DIAS = "diasToLicPre";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_HOUR = "heure";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_UNKNOWN= "unknown";

        private static final String DATABASE_NAME = "pression.db";
        private static final int DATABASE_VERSION = 1;

        // Commande sql pour la création de la base de données

        private static final String DATABASE_CREATE = "create table "
                + TABLE_PRESSURE + "("  + COLUMN_ID  + " integer primary key autoincrement, "
                + COLUMN_SIS+ " text not null, " + COLUMN_DIAS + " text not null, " + COLUMN_DATE  + " text not null, " +
                COLUMN_HOUR + " text not null, " + COLUMN_NOTE   + " text not null, " + COLUMN_UNKNOWN + " text not null); ";


        public PressureSQLHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {

            database.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(PressureSQLHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " +  TABLE_PRESSURE);
            onCreate(db);
        }

    }


}
