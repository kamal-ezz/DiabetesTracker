package com.p.diabetz;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static com.p.diabetz.Contacts.contactsInfo;


public class Accueil extends Fragment {


        private long START_TIME_IN_MILLIS;

        private TextView countDownTextView;

        private ImageView startImageView;
        private ImageView pauseImageView;
        private ImageView resetImageView;

        private CountDownTimer countDownTimer;

        private boolean timerRunning;

        private long timeLeftInMillis;


        private double UpperLimitWithFasting = 1.26;
        private double LowerLimitWithFasting = 0.8;
        private double UpperLimitWithoutFasting = 1.4;
        private double LowerLimitWithoutFasting = 1;


    //un constructeur vide nécessaire
    public Accueil(){

    }



    //Permet de lancer le compte à rebours lorsqu'on clique sur le "START" et de l'annuler lorsqu'on clique sur "PAUSE"
    private void startPause(View view) {

        ImageView counter = (ImageView) view;

        if ((counter.getTag()).equals("startRappel") || (counter.getTag()).equals("pauseRappel")) {

            if (timerRunning) {

                pauseTimer();

            } else {

                startTimer();
            }
        }

        
    }

    //permet de reprendre l'etat intial lorsqu'on clique sur "RESET"
    private void reset(View view) {

        ImageView counter = (ImageView) view;

        if ((counter.getTag()).equals("resetRappel")) {

            resetTimer();
        }

    
    }


    //permet de convetir le temps affiché dans le chrno en milisecondes.
    private long convertToMilis(String str){
        if (str.equals("01:00:00"))
            return 3600000;
        else if (str.equals("01:30:00"))
            return 5400000;
        else if(str.equals("02:00:00"))
            return 7200000;
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.accueil, container, false);

        GlucoseDataSource glucoseDataSource = new GlucoseDataSource(getActivity());

        glucoseDataSource.open();

        List<DataToStore> values = glucoseDataSource.getAllData();
        

        // le text qui apparu dans l'acceuil
        TextView indicateur = (TextView) view.findViewById(R.id.etatGlycemieTextView);

        // pour obtenir la date d'aujourd'hui
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();

        String today = formatter.format(date);

        // obtenir les paramètres
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String rappelcountdown = prefs.getString("rappel_chrono", "01:00:00");
        String mesureUnit = prefs.getString("mesure_unit", "g/l");

        //ce sont les valeurs limites pour une glycémie normale avec les deux cas jeune et pas jeune    
        if (mesureUnit.equals("g/l")) {
            UpperLimitWithFasting = 1.26;
            LowerLimitWithFasting = 0.8;
            UpperLimitWithoutFasting = 1.4;
            LowerLimitWithoutFasting = 1;

        } else if (mesureUnit.equals("mmol/l")) {
            UpperLimitWithFasting = 6.67;
            LowerLimitWithFasting = 4.44;
            UpperLimitWithoutFasting = 7;
            LowerLimitWithoutFasting = 5.55;

        } else if (mesureUnit.equals("mg/dl")) {
            UpperLimitWithFasting = 126;
            LowerLimitWithFasting = 80;
            UpperLimitWithoutFasting = 140;
            LowerLimitWithoutFasting = 10;

        }

        //l'emoji qui apparut
        ImageView smileEmoji = (ImageView) view.findViewById(R.id.smileEmojiImageView);
        ImageView sadEmoji = (ImageView) view.findViewById(R.id.sadEmojiImageView);


        boolean isFast = false; 
        double latestmesure = 0;
        
        //obtenir le dernier mésure
        if (values != null) {

            for (int i = 0; i < values.size(); i++) {

                String datee = values.get(i).getMesures()[0];
                if (datee.equals(today)) {


                    String when = values.get(i).getMesures()[2];

                    if (when.equals("Avant petit-déjeuner") || when.equals("Jeune"))
                        isFast = true;
                    else
                        isFast = false;
                }


            }


            if (values.size() > 0) {
                String l = values.get(values.size() - 1).getMesures()[4];
                String[] arr = l.split(" ");
                latestmesure = Double.parseDouble(arr[0]);
            }

        }


        //juger la glycémie d'aprés le dernier mésure
        if(!isFast && latestmesure == 0){
        indicateur.setText("Pas encore de mésures aujourd'hui");
        smileEmoji.setVisibility(View.INVISIBLE);
        sadEmoji.setVisibility(View.INVISIBLE);
        } else if (isFast && latestmesure > UpperLimitWithFasting){
           indicateur.setText("Votre glycémie est élevée");
            smileEmoji.setVisibility(View.INVISIBLE);
            sadEmoji.setVisibility(View.VISIBLE);

        }else if(isFast && latestmesure < LowerLimitWithFasting){
            indicateur.setText("Votre glycémie est basse");
            smileEmoji.setVisibility(View.INVISIBLE);
            sadEmoji.setVisibility(View.VISIBLE);   
        }
        else if(!isFast && latestmesure > UpperLimitWithoutFasting){
            indicateur.setText("Votre glycémie est élevée");
            smileEmoji.setVisibility(View.INVISIBLE);
            sadEmoji.setVisibility(View.VISIBLE);   
        }
        else if(!isFast && latestmesure < LowerLimitWithoutFasting){
            indicateur.setText("Votre glycémie est basse");
            smileEmoji.setVisibility(View.INVISIBLE);
            sadEmoji.setVisibility(View.VISIBLE);   
        }
        else {
            indicateur.setText("Votre glycémie est normal");
            smileEmoji.setVisibility(View.VISIBLE);
            sadEmoji.setVisibility(View.INVISIBLE);
        }



        // configuration du premier chrono
        START_TIME_IN_MILLIS = convertToMilis(rappelcountdown);
        timeLeftInMillis = convertToMilis(rappelcountdown);

        countDownTextView = (TextView) view.findViewById(R.id.countDownTextView);
        countDownTextView.setText(rappelcountdown);

        //lancer le compteur 
        startImageView = (ImageView) view.findViewById(R.id.startImageView);

        startImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPause(view);
            }
        });

        pauseImageView = (ImageView) view.findViewById(R.id.pauseImageView);

        //mettre le compteur en pause
        pauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPause(view);
            }
        });

        resetImageView = (ImageView) view.findViewById(R.id.resetImageView);

        //remettre le compteur à sa valeur initiale
        resetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(view);
            }
        });


        //Obtenir et afficher la liste des contacts

        ArrayList<String> contactNames = new ArrayList<>();
        ArrayList<String[]> contacts = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.p.diabetz", Context.MODE_PRIVATE);

        try {
            contacts = (ArrayList<String[]>) ObjectSerializer.deserialize(sharedPreferences.getString("c", ObjectSerializer.serialize(new ArrayList<String[]>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (contacts != null) {

            for (String[] arrStr : contacts) {
                contactNames.add(arrStr[0]);
            }

        }


        final ListView lv = (ListView) view.findViewById(R.id.urgenceListView);//liste des contacts à appeler dans cas d'urgence
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, contactNames);
        lv.setAdapter(adapter);


        // cela permet d'appeler le numéro de télépjone si l'utilisateur clique sur un élément de la liste
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (String[] strArr : contactsInfo) {
                    if (i == Integer.parseInt(strArr[2])) {
                        Uri number = Uri.parse("tel:" + strArr[1]);
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        startActivity(callIntent);

                    }
                }
            }

        });


        return view;
    }

       
        //Planifie un compte à rebours jusqu'à une date ultérieure, avec des notifications régulières.
        private void startTimer() {

            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long l) {
                    
                    timeLeftInMillis = l;
                    updateCountDownText();

                }

                @Override
                public void onFinish() {


                    startAlarme();
                    showNotification();

                    countDownTextView.setText("00:00:00");
                    timerRunning = false;
                    pauseImageView.setVisibility(View.INVISIBLE);
                    startImageView.setVisibility(View.VISIBLE);
                    resetImageView.setVisibility(View.VISIBLE);
                }
            }.start();

            timerRunning = true;
            startImageView.setVisibility(View.INVISIBLE);
            pauseImageView.setVisibility(View.VISIBLE);
            resetImageView.setVisibility(View.INVISIBLE);
        }

        //Annule le compte à rebours
        private void pauseTimer() {
            countDownTimer.cancel();
            timerRunning = false;
            pauseImageView.setVisibility(View.INVISIBLE);
            resetImageView.setVisibility(View.VISIBLE);
            startImageView.setVisibility(View.VISIBLE);
        }

        //Remis le compteur à sa valeur initiale
        private void resetTimer() {

            timeLeftInMillis = this.START_TIME_IN_MILLIS;
            updateCountDownText();
            pauseImageView.setVisibility(View.INVISIBLE);
            startImageView.setVisibility(View.VISIBLE);
            resetImageView.setVisibility(View.INVISIBLE);
        }

        //Modifie a chaque fois le temps afficher et le convertie en heures, minutes et secondes
        private void updateCountDownText() {

            int hours = (int) (timeLeftInMillis / 1000) / 3600;
            int minutes = (int) (timeLeftInMillis / 1000) / 60 % 60;
            int seconds = (int) (timeLeftInMillis / 1000) % 60;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

            countDownTextView.setText(timeLeftFormatted);
        }

        //décalnche la sonnerie
        private void startAlarme() {
            Intent intent = new Intent(getActivity(), MyBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 12, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);

        }


    //afficher la notification
    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("default","test", NotificationManager.IMPORTANCE_DEFAULT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }


        Intent intent = new Intent(getActivity(), Accueil.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),0,intent,0);

        //Préciser le contenu du notfication
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "default")
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Important!")
                .setContentText("Il est temps de mésurer votre glycémie")
                .setContentIntent(pendingIntent);




        notificationManager.notify(0, builder.build());


}
}





