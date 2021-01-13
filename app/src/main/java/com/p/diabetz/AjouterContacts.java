package com.p.diabetz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AjouterContacts extends AppCompatActivity {


    private EditText ed1;
    private EditText ed2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_contacts);



        setTitle("Contacts");

        ed1 = (EditText) findViewById(R.id.nameContact);
        ed2 = (EditText) findViewById(R.id.telContact);


        Button btn = (Button) findViewById(R.id.addContactsButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Contacts.class);

                String[] info = new String[]{ed1.getText().toString(), ed2.getText().toString(),String.valueOf(Contacts.contactsInfo.size())};

                if(!(ed1.getText().toString().isEmpty()) && !(ed2.getText().toString().isEmpty())) {
                    Contacts.contactsInfo.add(info);
                    Contacts.adapter.notifyDataSetChanged();

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.p.diabetz", Context.MODE_PRIVATE);
                    try {
                        sharedPreferences.edit().putString("c",ObjectSerializer.serialize(Contacts.contactsInfo)).apply();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    startActivity(intent);

                }else{
                    new AlertDialog.Builder(AjouterContacts.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Attention")
                            .setMessage("Le nom ou le n° de tél est manquant")
                            .setPositiveButton("Ok", null)
                            .show();
                }



            }



        });



    }
}
