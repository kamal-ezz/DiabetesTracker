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

public class GlucoseDataSource {

    // Champs de la base de données
    private SQLiteDatabase database;
    private GlucoseSQLiteHelper dbHelper;
    private String[] allColumns = { GlucoseSQLiteHelper.COLUMN_ID, GlucoseSQLiteHelper.COLUMN_DATE, GlucoseSQLiteHelper.COLUMN_HOUR,
            GlucoseSQLiteHelper.COLUMN_PERIOD, GlucoseSQLiteHelper.COLUMN_NOTE, GlucoseSQLiteHelper.COLUMN_VALUE};

    Cursor cursor;

    public GlucoseDataSource(Context context) {

        dbHelper = new GlucoseSQLiteHelper(context);
    }

    //obtenir les permissions d'écriture
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    //remplissage de la table dans la base de données
    public DataToStore createData (String[] mesures){

        ContentValues values = new ContentValues();

        values.put(GlucoseSQLiteHelper.COLUMN_DATE, mesures[0]);
        values.put(GlucoseSQLiteHelper.COLUMN_HOUR, mesures[1]);
        values.put(GlucoseSQLiteHelper.COLUMN_PERIOD, mesures[2]);
        values.put(GlucoseSQLiteHelper.COLUMN_NOTE, mesures[3]);
        values.put(GlucoseSQLiteHelper.COLUMN_VALUE, mesures[4]);

        long insertId = database.insert(GlucoseSQLiteHelper.TABLE_MESURESGLUCOSE, null, values);
        cursor = database.query(GlucoseSQLiteHelper.TABLE_MESURESGLUCOSE,
                allColumns, GlucoseSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        DataToStore newDataToStore = cursorToData(cursor);
        cursor.close();
        return newDataToStore;
    }
    //suppression du données
    public void deleteData(String[] mesures){

        
        database.delete(GlucoseSQLiteHelper.TABLE_MESURESGLUCOSE, GlucoseSQLiteHelper.COLUMN_DATE + " = " + "'" + mesures[0]
                + "'" + " AND " + GlucoseSQLiteHelper.COLUMN_HOUR  + " = " + "'" + mesures[1] + "'" , null);
    }

    //obtenir les infos de la base de données
    public List<DataToStore> getAllData(){

        List<DataToStore> data = new ArrayList<DataToStore>();

        Cursor cursor = database.query(GlucoseSQLiteHelper.TABLE_MESURESGLUCOSE,
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


public class GlucoseSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_MESURESGLUCOSE = "mesuresGlucose";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VALUE = "valeur";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_HOUR = "heure";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_PERIOD = "periode";

    private static final String DATABASE_NAME = "mesuresGlucose.db";
    private static final int DATABASE_VERSION = 1;


    // Commande sql pour la création de la base de données

    private static final String DATABASE_CREATE = "create table "
            + TABLE_MESURESGLUCOSE + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_VALUE + " text not null, " + COLUMN_DATE + " text not null, " + COLUMN_HOUR + " text not null, "
            + COLUMN_NOTE + " text not null, " + COLUMN_PERIOD + " text not null);";


    public GlucoseSQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(GlucoseSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESURESGLUCOSE);
        onCreate(db);
    }
}

}