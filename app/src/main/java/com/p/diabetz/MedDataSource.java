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

public class MedDataSource {

    // Champs de la base de données
    private SQLiteDatabase database;
    private MedSQLHelper dbHelper;
    private String[] allColumns = {  MedSQLHelper.COLUMN_ID,  MedSQLHelper.COLUMN_DATE, MedSQLHelper.COLUMN_HOUR,
            MedSQLHelper.COLUMN_MED, MedSQLHelper.COLUMN_NOTE, MedSQLHelper.COLUMN_DOSAGE};

    Cursor cursor;

    public MedDataSource(Context context) {

        dbHelper = new MedSQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public DataToStore createData (String[] mesures){

        ContentValues values = new ContentValues();

        values.put(MedSQLHelper.COLUMN_DATE, mesures[0]);
        values.put(MedSQLHelper.COLUMN_HOUR, mesures[1]);
        values.put(MedSQLHelper.COLUMN_MED, mesures[2]);
        values.put(MedSQLHelper.COLUMN_NOTE, mesures[3]);
        values.put(MedSQLHelper.COLUMN_DOSAGE, mesures[4]);

        long insertId = database.insert(MedSQLHelper.TABLE_MEDICAMETS, null, values);
        cursor = database.query(MedSQLHelper.TABLE_MEDICAMETS,
                allColumns, MedSQLHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        DataToStore newDataToStore = cursorToData(cursor);
        cursor.close();
        return newDataToStore;
    }

    public void deleteData(String[] mesures){

        //Log.i("Info", "date et heure " + mesures[1] + " " + mesures[2] );
        database.delete(MedSQLHelper.TABLE_MEDICAMETS, MedSQLHelper.COLUMN_DATE + " = " + "'" + mesures[0]
                + "'" + " AND " + MedSQLHelper.COLUMN_HOUR  + " = " + "'" + mesures[1] + "'" , null);
    }

    public List<DataToStore> getAllData(){

        List<DataToStore> data = new ArrayList<DataToStore>();

        Cursor cursor = database.query(MedSQLHelper.TABLE_MEDICAMETS,
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
                cursor.getString(4), cursor.getString(5)};

        dataToStore.setMesures(tab);

        return dataToStore;
    }

    public class MedSQLHelper extends SQLiteOpenHelper {

        public static final String TABLE_MEDICAMETS = "medicaments";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_MED = "medicament";
        public static final String COLUMN_DOSAGE = "dosage";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_HOUR = "heure";
        public static final String COLUMN_NOTE = "note";

        private static final String DATABASE_NAME = "medicaments.db";
        private static final int DATABASE_VERSION = 1;

        // Commande sql pour la création de la base de données

        private static final String DATABASE_CREATE = "create table "
                + TABLE_MEDICAMETS + "("  + COLUMN_ID  + " integer primary key autoincrement, "
                + COLUMN_MED+ " text not null, " + COLUMN_DOSAGE  + " text not null, " + COLUMN_DATE  + " text not null, " + COLUMN_HOUR + " text not null, "
                + COLUMN_NOTE   + " text not null); " ;

        public MedSQLHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {

            database.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MedSQLHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " +   TABLE_MEDICAMETS);
            onCreate(db);
        }

    }
}