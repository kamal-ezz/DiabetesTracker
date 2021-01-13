package com.p.diabetz;

import android.content.Intent;
import android.os.Bundle;


import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {



    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // obtenir la référence de la barre d'outils
        setSupportActionBar(toolbar); // Définition / remplacement de la barre d'outils comme ActionBar

        // obtenir la référence de ViewPager et TabLayout
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // Créer un nouvel onglet nommé "Accueil"
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Accueil"); // définir le texte du premier onglet
        firstTab.setIcon(R.drawable.buildings); // définir une icône pour le premier onglet
        tabLayout.addTab(firstTab); // ajouter l'onglet dans le TabLayout
       
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Mésures"); 
        secondTab.setIcon(R.drawable.tools); 
        tabLayout.addTab(secondTab); 
        
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("Données"); 
        thirdTab.setIcon(R.drawable.computing);
        tabLayout.addTab(thirdTab);

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        // lorsqu'on clique sur un onglet il va afficher les données qu'il contient
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // L'événement addOnPageChangeListener modifie l'onglet sur la diapositive
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));



    }


    //lier le menu avec le fichier ressource correspandante  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    //les actions qui sont éxecutés dés qu'on clique sur un élement de menu
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.about :
                Intent intent = new Intent(getApplicationContext(), Apropos.class);
                startActivity(intent);
                break;
            case R.id.share:
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"Diabetz: une application génial pour suivre votre glycémie");
                intent.setType("text/plain");
                Intent.createChooser(intent,"Partager via");
                startActivity(intent);
                break;
            case R.id.contacts:
                intent = new Intent(getApplicationContext(),Contacts.class);
                startActivity(intent);
                break;
            case R.id.settings:
                intent = new Intent(getApplicationContext(), Parametres.class);
                startActivity(intent);
                break;

            case R.id.export:
                //Log.i("Info", "Export");
                intent = new Intent(getApplicationContext(), Export.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}