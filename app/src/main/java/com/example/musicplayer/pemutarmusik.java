package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class pemutarmusik extends AppCompatActivity {

    TextView titleTv;
    SeekBar playerControl;
    ImageButton btnPlay, btnStop, btnNext, btnPrevious;
    TextView totalWaktu, waktuBerjalan;
    ArrayList<ModelAudio> songsList;
    ModelAudio currentSong;
    ImageView musicLogo;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x=0;

    private boolean isPlaying = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemutarmusik);

        titleTv = findViewById(R.id.song_title);
        playerControl = findViewById(R.id.player_control);
        btnPlay = findViewById(R.id.btn_play);
        btnStop = findViewById(R.id.btn_stop);
        btnNext = findViewById(R.id.next);
        btnPrevious = findViewById(R.id.previous);
        waktuBerjalan = findViewById(R.id.waktu_berjalan);
        totalWaktu = findViewById(R.id.total_waktu);
        musicLogo = findViewById(R.id.Logo);

        titleTv.setSelected(true);

        songsList = (ArrayList<ModelAudio>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();

        //pemutaran logo
        pemutarmusik.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    playerControl.setProgress(mediaPlayer.getCurrentPosition());
                    waktuBerjalan.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying()){
                        btnPlay.setImageResource(R.drawable.pause);
                        musicLogo.setRotation(x++);
                    }else{
                        btnPlay.setImageResource(R.drawable.play);
                        musicLogo.setRotation(x++);
                    }

                }
                new Handler().postDelayed(this,100);
            }
        });



        //seekbar
        playerControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //ResourceButton
    void setResourcesWithMusic() {
        currentSong = songsList.get(MyMediaPlayer.currentIndex);

        titleTv.setText(currentSong.getTitle());
        totalWaktu.setText(convertToMMSS(currentSong.getDuration()));

        btnStop.setOnClickListener(v -> BtnStop());
        btnPlay.setOnClickListener(v -> BtnPlay());
        btnNext.setOnClickListener(v -> playNextSong());
        btnPrevious.setOnClickListener(v -> playPreviousSong());

        playMusic();
    }


    //playmusic
    private void playMusic() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            playerControl.setProgress(0);
            playerControl.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //btnplay
    private void BtnPlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    //btnstop
    private void BtnStop(){
        mediaPlayer.seekTo(0);
        mediaPlayer.pause();
        playerControl.setProgress(0);
        isPlaying = false;
    }

    //btn previouse
    private void playPreviousSong() {
        if (MyMediaPlayer.currentIndex == 0)
            return;
        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    //btn next
    private void playNextSong() {

        if (MyMediaPlayer.currentIndex == songsList.size() - 1)
            return;
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    //untuk waktu berjalan
    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }



}













