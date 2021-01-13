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

public class A1CDataSource{

    // Champs de la base de données
    private SQLiteDatabase database;
    private A1CSQLiteHelper dbHelper;
    private String[] allColumns = { A1CSQLiteHelper.COLUMN_ID, A1CSQLiteHelper.COLUMN_DATE, A1CSQLiteHelper.COLUMN_HOUR,
            A1CSQLiteHelper.COLUMN_NOTE, A1CSQLiteHelper.COLUMN_CONCENTRATION};

    Cursor cursor;

    public A1CDataSource(Context context) {

        dbHelper = new A1CSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public DataToStore createData (String[] mesures){

        ContentValues values = new ContentValues();


        values.put(A1CSQLiteHelper.COLUMN_DATE, mesures[0]);
        values.put(A1CSQLiteHelper.COLUMN_HOUR, mesures[1]);
        values.put(A1CSQLiteHelper.COLUMN_NOTE, mesures[2]);
        values.put(A1CSQLiteHelper.COLUMN_CONCENTRATION, mesures[3]);

        long insertId = database.insert(A1CSQLiteHelper.TABLE_MESURESA1C, null, values);
        cursor = database.query(A1CSQLiteHelper.TABLE_MESURESA1C,
                allColumns, A1CSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        DataToStore newDataToStore = cursorToData(cursor);
        cursor.close();
        return newDataToStore;
    }

    public void deleteData(String[] mesures){

        //Log.i("Info", "date et heure " + mesures[1] + " " + mesures[2] );
        database.delete(A1CSQLiteHelper.TABLE_MESURESA1C, A1CSQLiteHelper.COLUMN_DATE + " = " + "'" + mesures[0]
                + "'" + " AND " + A1CSQLiteHelper.COLUMN_HOUR  + " = " + "'" + mesures[1] + "'" , null);
    }

    public List<DataToStore> getAllData(){

        List<DataToStore> data = new ArrayList<DataToStore>();

        Cursor cursor = database.query(A1CSQLiteHelper.TABLE_MESURESA1C,
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
                cursor.getString(4)};

        dataToStore.setMesures(tab);

        return dataToStore;
    }


    public class A1CSQLiteHelper extends SQLiteOpenHelper {

        public static final String TABLE_MESURESA1C = "mesuresGlucose";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_CONCENTRATION = "concentration";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_HOUR = "heure";
        public static final String COLUMN_NOTE = "note";

        private static final String DATABASE_NAME = "mesuresA1C.db";
        private static final int DATABASE_VERSION = 1;

        // Commande sql pour la création de la base de données

        private static final String DATABASE_CREATE = "create table "
                + TABLE_MESURESA1C + "("  + COLUMN_ID  + " integer primary key autoincrement, "
                + COLUMN_CONCENTRATION + " text not null, " + COLUMN_DATE  + " text not null, " + COLUMN_HOUR + " text not null, "
                + COLUMN_NOTE   + " text not null); " ;

        public A1CSQLiteHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase database) {

            database.execSQL(DATABASE_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(A1CSQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " +  TABLE_MESURESA1C);
            onCreate(db);
        }
    }


}
