package com.example.mendiola_turnbasedgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // wStats
    int w_base_health = 30;
    int w_base_mana = 5;
    int w_heath = w_base_health;
    int w_mana = w_base_mana;
    int w_low_dmg = 1;
    int w_high_dmg = 3;
    // bStats
    int b_base_health = 75;
    int b_base_mana = 3;
    int b_heath = b_base_health;
    int b_mana = b_base_mana;
    int b_low_dmg = 2;
    int b_high_dmg = 5;

    //game related
    int turnCount= 1;
    TextView wHealth, wMana, bHealth, bMana, transmission;
    ImageView go, skill1;
    int prevDMG, DMG;
    Random randomizer;
    MediaPlayer bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        wHealth = findViewById(R.id.me_hp);
        wMana = findViewById(R.id.me_mana);
        bHealth = findViewById(R.id.villain_hp);
        bMana = findViewById(R.id.villain_mana);
        transmission = findViewById(R.id.transmission);
        go = findViewById(R.id.go);
        skill1 =findViewById(R.id.skill);
        go.setOnClickListener(this);
        skill1.setOnClickListener(this);
        randomizer = new Random();
        updateapp();
        enableFullscreen();
        bgm = new MediaPlayer();
        bgm = MediaPlayer.create(this,R.raw.background_music);
        bgm.setLooping(true);
        bgm.start();
        bgm.seekTo(4000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go:
                nextTurn();
                break;
            case R.id.skill:
                skill();
                break;
        }
    }
    public void nextTurn(){
        if (turnCount == 0){
            reset();
            transmission.setText("Begin!");
            updateapp();
            turnCount++;
        }
        else if (turnCount % 2 == 1){
            prevDMG = randomizer.nextInt(w_high_dmg-w_low_dmg) + w_low_dmg;
            DMG = prevDMG;
            b_heath= b_heath - DMG;
            transmission.setText("Fluffy dealt "+ String.valueOf(DMG)+" to Cindy");
            turnCount++;
            disableskill();
            w_mana=w_mana+1;
            if(b_heath<=0){
                b_heath=0;
                updateapp();
                turnCount=0;
                transmission.setText("Victory!");
            }
        }
        else if (turnCount % 2 == 0){
            prevDMG = randomizer.nextInt(b_high_dmg-b_low_dmg) + b_low_dmg;
            DMG = prevDMG;
            w_heath= w_heath - DMG;
            transmission.setText("Cindy dealt "+ String.valueOf(DMG)+" to Fluffy");
            turnCount++;
            enableskill();
            if(w_heath<=0){
                disableskill();
                w_heath=0;
                updateapp();
                turnCount=0;
                transmission.setText("Defeat!");
            }
        }
        updateapp();
    }
    public void skill(){
        if (w_mana<1){
            transmission.setText("You don't have enough mana!");
            disableskill();
        }else {
            b_heath=b_heath-10;
            transmission.setText("Fluffy dealt 10 to Cindy");
            disableskill();
            turnCount++;
            w_mana--;
            updateapp();
            if(b_heath<=0){
                b_heath=0;
                updateapp();
                turnCount=0;
                transmission.setText("Victory");
            }
        }
    }

    private void reset() {
        w_heath = w_base_health;
        w_mana = w_base_mana;
        b_heath = b_base_health;
        b_mana = b_base_mana;
    }

    private void enableskill() {
        skill1.setVisibility(View.VISIBLE);
    }

    private void disableskill() {
        skill1.setVisibility(View.INVISIBLE);
    }

    private void updateapp() {
        wHealth.setText("HP-"+String.valueOf(w_heath));
        wMana.setText("MP-"+String.valueOf(w_mana));
        bHealth.setText("HP-"+String.valueOf(b_heath));
        bMana.setText("MP-"+String.valueOf(b_mana));
    }
    private void enableFullscreen() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
    }
}