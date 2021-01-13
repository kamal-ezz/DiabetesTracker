package com.p.diabetz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {

    protected static ArrayAdapter<String> adapter;
    protected static ArrayList<String[]> contactsInfo = new ArrayList<String[]>(); //contient des infos de contient
    protected ArrayList<String> contactNames = new ArrayList<>(); //contient seulement les noms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Contacts");

        //lancer l'activité de l'ajout d'un contact
       FloatingActionButton fab = findViewById(R.id.fabContacts);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AjouterContacts.class);
                startActivity(intent);
            }
        });

        //recevoir la liste des contacts qui sont stockée dans un sharedPreferences
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.p.diabetz", Context.MODE_PRIVATE);

        try {
            contactsInfo = (ArrayList<String[]>) ObjectSerializer.deserialize(sharedPreferences.getString("c", ObjectSerializer.serialize(new ArrayList<String[]>())));
        }catch (Exception e){
            e.printStackTrace();
        }


        final ListView lv = (ListView) findViewById(R.id.ContactsListView);//liste des contacts
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactNames);
        lv.setAdapter(adapter);

        if(contactsInfo != null) {

            for (String[] arrStr : contactsInfo) {
                contactNames.add(arrStr[0]); //ajouter les noms au contactNames
            }

        }


        //supprimer un contact
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;

                new AlertDialog.Builder(Contacts.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Vous êtes sure?")
                        .setMessage("Est-ce vous voulez supprimer ce contact?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                for (String[] arrStr : contactsInfo){
                                    if(itemToDelete == Integer.parseInt(arrStr[2]))
                                        contactsInfo.remove(contactsInfo.indexOf(arrStr));
                                }
                                contactNames.remove(itemToDelete);
                                adapter.notifyDataSetChanged();

                                try {
                                    sharedPreferences.edit().putString("c",ObjectSerializer.serialize(Contacts.contactsInfo)).apply();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

                return true;
            }
        });


    }



}





