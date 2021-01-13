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

public class PoidsDataSource {

    // Champs de la base de données
    private SQLiteDatabase database;
    private PoidsSQLHelper dbHelper;
    private String[] allColumns = {PoidsSQLHelper.COLUMN_ID, PoidsSQLHelper.COLUMN_DATE, PoidsSQLHelper.COLUMN_HOUR,
            PoidsSQLHelper.COLUMN_NOTE, PoidsSQLHelper.COLUMN_VALUE};

    Cursor cursor;

    public PoidsDataSource(Context context) {

        dbHelper = new PoidsSQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public DataToStore createData(String[] mesures) {

        ContentValues values = new ContentValues();

        values.put(PoidsSQLHelper.COLUMN_DATE, mesures[0]);
        values.put(PoidsSQLHelper.COLUMN_HOUR, mesures[1]);
        values.put(PoidsSQLHelper.COLUMN_NOTE, mesures[2]);
        values.put(PoidsSQLHelper.COLUMN_VALUE, mesures[3]);

        long insertId = database.insert(PoidsSQLHelper.TABLE_POIDS, null, values);
        cursor = database.query(PoidsSQLHelper.TABLE_POIDS,
                allColumns, PoidsSQLHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        DataToStore newDataToStore = cursorToData(cursor);
        cursor.close();
        return newDataToStore;
    }

    public void deleteData(String[] mesures) {

        //Log.i("Info", "date et heure " + mesures[1] + " " + mesures[2] );
        database.delete(PoidsSQLHelper.TABLE_POIDS, PoidsSQLHelper.COLUMN_DATE + " = " + "'" + mesures[0]
                + "'" + " AND " + PoidsSQLHelper.COLUMN_HOUR + " = " + "'" + mesures[1] + "'", null);
    }


    public List<DataToStore> getAllData() {

        List<DataToStore> data = new ArrayList<DataToStore>();

        Cursor cursor = database.query(PoidsSQLHelper.TABLE_POIDS,
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

    private DataToStore cursorToData(Cursor cursor) {

        DataToStore dataToStore = new DataToStore();

        String[] tab = {cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4)};

        dataToStore.setMesures(tab);

        return dataToStore;
    }


    public class PoidsSQLHelper extends SQLiteOpenHelper {

        public static final String TABLE_POIDS = "poids";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_VALUE = "valeur";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_HOUR = "heure";
        public static final String COLUMN_NOTE = "note";

        private static final String DATABASE_NAME = "poids.db";
        private static final int DATABASE_VERSION = 1;

        // Commande sql pour la création de la base de données

        private static final String DATABASE_CREATE = "create table "
                + TABLE_POIDS + "(" + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_VALUE + " text not null, " + COLUMN_DATE + " text not null, " + COLUMN_HOUR + " text not null, "
                + COLUMN_NOTE + " text not null);";

        public PoidsSQLHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {

            database.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(PoidsSQLHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POIDS);
            onCreate(db);
        }


    }
}
