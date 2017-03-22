package se.acoder.autohub.hub.products.InternetRadio;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import se.acoder.autohub.hub.products.ProductService;

/**
 * Created by Johannes Westlund on 2017-03-20.
 */

public class RadioService extends ProductService {
    private AudioManager AM;
    private AudioManager.OnAudioFocusChangeListener audioFocusListener;
    private MediaPlayer radio;

    private Channel currentChannel, pendingChannel;
    private boolean isPaused = false;
    private int volume;

    private List<RadioServiceListener> listeners;

    @Override
    public void onCreate() {
        super.onCreate();
        AM = (AudioManager) getSystemService(AUDIO_SERVICE);
        volume = AM.getStreamVolume(AudioManager.STREAM_MUSIC);
        radio = new MediaPlayer();
        listeners = new LinkedList<RadioServiceListener>();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String wasPlaying = intent.getStringExtra(InternetRadioProduct.KEYS.WAS_PLAYING.name());
        if(wasPlaying != null && !radio.isPlaying())
            handlePlay(Channel.deserialize(wasPlaying));
        return new RadioInteraction();
    }

    private void handleChannelPressed(Channel channel){
        if(currentChannel == channel)
            handleStop();
        else
            handlePlay(channel);
    }

    private void handleStop(){
        radio.stop();
        currentChannel = null;
        isPaused = false;
        AM.abandonAudioFocus(audioFocusListener);
        for(RadioServiceListener l : listeners){ l.onRadioStop(); }
    }

    private void handlePause(){
        radio.pause();
        isPaused = true;
        for(RadioServiceListener l : listeners){ l.onRadioPause(); }
    }

    private void handlePlay(Channel channel){
        if(audioFocusListener != null)
            audioFocusListener = createAudioFocusListener();
        int canPlay = AM.requestAudioFocus(audioFocusListener,
                                           AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if(canPlay == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            pendingChannel = channel;
            for(RadioServiceListener l : listeners){ l.onChannelPending(pendingChannel); }
            startMusic(channel);
        }
    }

    private void startMusic(Channel channel){
        if(isPaused){
            radio.start();
        } else {
            final Channel selected = channel;
            radio.reset();
            radio.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            try {
                radio.setDataSource(selected.getUrl());
                radio.prepareAsync();
            } catch (IOException IOE) {
                IOE.printStackTrace();
            }
            radio.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch(what){
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            currentChannel = selected;
                            pendingChannel = null;
                            for(RadioServiceListener l : listeners){ l.onChannelStartsPlaying(currentChannel); }
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    private AudioManager.OnAudioFocusChangeListener createAudioFocusListener() {
        return new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch(focusChange){
                    case AudioManager.AUDIOFOCUS_GAIN:
                        AM.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
                        if(currentChannel != null)
                            handlePlay(currentChannel);
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        handleStop();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        handlePause();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        AM.setStreamVolume(AudioManager.STREAM_MUSIC,
                                volume/6, 0);
                        break;
                }
            }
        };
    }

    protected class RadioInteraction extends ProductServiceBinder {
        public void channelPressed(Channel channel){
            handleChannelPressed(channel);
        }
        public void pause(){
        }
        public void play(){
        }
        public Channel getCurrentChannel(){
            return currentChannel;
        }
        public Channel getPendingChannel(){
            return pendingChannel;
        }

        public void registerRadioServiceListener(RadioServiceListener RSL){
            listeners.add(RSL);
        }
        public void unregisterRadioServiceListener(RadioServiceListener RSL){
            listeners.remove(RSL);
        }
    }

    protected interface RadioServiceListener {
        void onChannelPending(Channel channel);
        void onChannelStartsPlaying(Channel channel);
        void onRadioStop();
        void onRadioPause();
    }
}
