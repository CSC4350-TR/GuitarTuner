package com.guitartuner.tuningfeedback;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.os.Bundle;
import android.os.Vibrator;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class Activity2 extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ToggleButton mic;
    TextView mic_status;
    ImageButton Tuner;
    TextView tuneTxt;
    TextView noteText;
    EditText textInput;
    MediaPlayer mp;
    Context context = this;
    String[] keys = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    Map<String, Map<String, Map<String, ArrayList<Double>>>> keysMap = new HashMap<>();
    boolean found = false;
    String foundKey = "";
    String foundRange = "";
    String foundOctave = "";
    int counter = 0;
    VibrationEffect vibe = VibrationEffect.createOneShot(400, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) throws NumberFormatException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        Tuner = findViewById(R.id.tuner);
        tuneTxt = findViewById(R.id.tuneText);
        mic = findViewById(R.id.muteButton2);
        mic_status = findViewById(R.id.micState2);
        noteText = findViewById(R.id.noteTxt);
        noteText.setText(" ");
        mic_status.setText("Mic off");
        tuneTxt.setText(" ");
        textInput = findViewById(R.id.inputTxt);






        Tuner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( mic_status.getText() == "Mic on"){
                    getTuningFromText();
                    counter++;
                }
                else
                    return;

//                counterTune();







            }

        });
        mic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(mic.isChecked()) {

                    mic_status.setText("Mic on");
                    Tuner.setImageResource(R.drawable.earlistening);
                }
                else {
                    Tuner.setImageResource(R.drawable.nosound);noteText.setText(" ");tuneTxt.setText(" ");
                    mic_status.setText("Mic off ");
                }
            }
        });
    }

    private void getTuningFromText() {
        double input = Double.parseDouble(String.valueOf(textInput.getText()));

        DocumentReference docRef = db.collection("frequencies").document("LpCXiyfJF84ZPbAi8ETw");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for (String k : keys) {
                            keysMap.put(k, (Map<String, Map<String, ArrayList<Double>>>) document.get(k));
                        }
                        Log.d("Tuning", keysMap.get("C").get("toosharp").get("oct2").toString());
                        findMatch(input);
                        giveFeedback();
                    } else {
                        tuneTxt.setText("No such document");
                    }
                } else {
                    tuneTxt.setText("Too bad! " + task.getException());
                }
            }
        });


    }

    private void findMatch(double input) {
        found = false;
        for (String k : keysMap.keySet()) {
            if (found)
                break;
            for (String range : keysMap.get(k).keySet()) {
                if (found)
                    break;
                for (String oct : keysMap.get(k).get(range).keySet()) {
                    if (found)
                        break;
                    double low = keysMap.get(k).get(range).get(oct).get(0);
                    double high = keysMap.get(k).get(range).get(oct).get(1);

                    if (low < input && input < high) {
                        recordMatch(k, range, oct);
                        found = true;
                    }
                }
            }
        }
    }

    private void recordMatch(String key, String range, String oct) {
        foundKey = key;
        foundRange = range;
        foundOctave = oct;
    }

    private void counterTune() {
        if (counter==1 ){
            Tuner.setImageResource(R.drawable.redup);
            tuneTxt.setText("Too flat tune up");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("E");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }
        if (counter==2){
            Tuner.setImageResource(R.drawable.yellowdown);
            tuneTxt.setText("Slightly sharp tune down");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("E");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }
        if (counter==3){
            Tuner.setImageResource(R.drawable.greenthumbs);
            tuneTxt.setText("You're in tune");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("E");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION));
            mp.start();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(vibe);
        }
        if (counter==4){
            Tuner.setImageResource(R.drawable.yellowup);
            tuneTxt.setText("Slightly flat tune up");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("A");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }
        if (counter==5){
            Tuner.setImageResource(R.drawable.reddown);
            tuneTxt.setText("Too sharp tune down");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("A");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }
        if (counter==6){
            Tuner.setImageResource(R.drawable.yellowdown);
            tuneTxt.setText("Slightly sharp tune down");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("A");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }
        if (counter==7){
            Tuner.setImageResource(R.drawable.greenthumbs);
            tuneTxt.setText("You're in tune");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("A");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION));
            mp.start();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(vibe);
        }
        if (counter==8){
            Tuner.setImageResource(R.drawable.redup);
            tuneTxt.setText("Too flat tune up");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("D");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }
        if (counter==9){
            Tuner.setImageResource(R.drawable.yellowup);
            tuneTxt.setText("Slightly flat tune up");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("D");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }

        if (counter==10){
            Tuner.setImageResource(R.drawable.greenthumbs);
            tuneTxt.setText("You're in tune");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("D");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION));
            mp.start();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(vibe);
        }
        if (counter==11){
            Tuner.setImageResource(R.drawable.reddown);
            tuneTxt.setText("Too sharp tune down");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("G");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }
        if (counter==12){
            Tuner.setImageResource(R.drawable.yellowdown);
            tuneTxt.setText("Slightly sharp tune down");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("G");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }

        if (counter==13){
            Tuner.setImageResource(R.drawable.greenthumbs);
            tuneTxt.setText("You're in tune");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("G");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION));
            mp.start();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(vibe);
        }
        if (counter==14){
            Tuner.setImageResource(R.drawable.yellowup);
            tuneTxt.setText("Slightly flat tune up");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("B");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }

        if (counter==15){
            Tuner.setImageResource(R.drawable.greenthumbs);
            tuneTxt.setText("You're in tune");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("B");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION));
            mp.start();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(vibe);
        }
        if (counter==16){
            Tuner.setImageResource(R.drawable.reddown);
            tuneTxt.setText("Too sharp tune down");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("e");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }
        if (counter==17){
            Tuner.setImageResource(R.drawable.redup);
            tuneTxt.setText("Too flat tune up");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("e");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }
        if (counter==18){
            Tuner.setImageResource(R.drawable.yellowdown);
            tuneTxt.setText("Slightly sharp tune down");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("e");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }
        if (counter==19){
            Tuner.setImageResource(R.drawable.yellowup);
            tuneTxt.setText("Slightly flat tune up");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("e");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
            mp.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mp.stop();
        }

        if (counter==20){
            Tuner.setImageResource(R.drawable.greenthumbs);
            tuneTxt.setText("You're in tune");
            tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
            noteText.setText("e");
            noteText.setScaleX(2);noteText.setScaleY(2);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION));
            mp.start();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            v.vibrate(vibe);
        }
        if (counter > 20){
            counter =0;
        }
    }

    private void giveFeedback() {
        if (foundRange.equals("")) {
            invalidFeedback();
        }
        else if (foundRange.equals("toosharp")) {
            tooSharpFeedback();
        }
        else if (foundRange.equals("sharp")) {
            sharpFeedback();
        }
        else if (foundRange.equals("perfect")) {
            perfectFeedback();
        }
        else if (foundRange.equals("flat")) {
            flatFeedback();
        }
        else if (foundRange.equals("tooflat")) {
            tooFlatFeedback();
        }
    }

    private void tooSharpFeedback() {
        Tuner.setImageResource(R.drawable.reddown);
        tuneTxt.setText("Too sharp, tune down");
        tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
        noteText.setText(foundKey);
        noteText.setScaleX(2);noteText.setScaleY(2);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
        mp.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        mp.stop();
    }

    private void sharpFeedback() {
        Tuner.setImageResource(R.drawable.yellowdown);
        tuneTxt.setText("Slightly sharp, tune down");
        tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
        noteText.setText(foundKey);
        noteText.setScaleX(2);noteText.setScaleY(2);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
        mp.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        mp.stop();
    }

    private void perfectFeedback() {
        Tuner.setImageResource(R.drawable.greenthumbs);
        tuneTxt.setText("You're in tune!!");
        tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
        noteText.setText(foundKey);
        noteText.setScaleX(2);noteText.setScaleY(2);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION));
        mp.start();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(vibe);
    }

    private void flatFeedback() {
        Tuner.setImageResource(R.drawable.yellowup);
        tuneTxt.setText("Slightly flat, tune up");
        tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
        noteText.setText(foundKey);
        noteText.setScaleX(2);noteText.setScaleY(2);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
        mp.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        mp.stop();
    }

    private void tooFlatFeedback() {
        Tuner.setImageResource(R.drawable.redup);
        tuneTxt.setText("Too flat, tune up");
        tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
        noteText.setText(foundKey);
        noteText.setScaleX(2);noteText.setScaleY(2);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
        mp.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        mp.stop();
    }

    private void invalidFeedback() {
        tuneTxt.setText("Frequency is out of range!");
        tuneTxt.setScaleX(2);tuneTxt.setScaleY(2);
        noteText.setText("");
        noteText.setScaleX(2);noteText.setScaleY(2);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM));
        mp.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        mp.stop();
    }
}