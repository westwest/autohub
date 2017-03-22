package se.acoder.autohub.hub.products.InternetRadio;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import se.acoder.autohub.HubApp;
import se.acoder.autohub.R;
import se.acoder.autohub.hub.products.ProductFragment;

/**
 * Created by Johannes Westlund on 2017-03-17.
 */

public class ChannelListFragment extends ProductFragment {
    private ArrayList<Channel> channels = new ArrayList<Channel>();
    private ListView channelList;
    private RadioService.RadioInteraction radioInteraction;
    private RadioService.RadioServiceListener RSL;
    private Channel selected;

    private final String KEY = "channel_presets";
    // private final String KEY_PLAYING = "currentChannel";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HubApp activity = (HubApp) getActivity();
        radioInteraction = (RadioService.RadioInteraction) activity.getServiceInterface(RadioService.class.toString().hashCode());
        selected = radioInteraction.getCurrentChannel();

        Set<String> channelRaw = getStoredProductStates().getStringSet(KEY, new HashSet<String>());
        if (channelRaw.isEmpty())
            initDefaultChannels();
        else
            initChannels(channelRaw);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        FrameLayout productView = (FrameLayout) rootView.findViewById(R.id.product_view);
        channelList = new ListView(getContext());
        final ChannelsAdapter adapter = new ChannelsAdapter(getContext(), channels);
        channelList.setAdapter(adapter);
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                channelClicked(channels.get(position));
            }
        });
        RSL = new RadioService.RadioServiceListener(){
            @Override
            public void onChannelPending(Channel channel) {
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChannelStartsPlaying(Channel channel) {
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onRadioStop() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRadioPause() {

            }
        };
        radioInteraction.registerRadioServiceListener(RSL);
        productView.addView(channelList);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        radioInteraction.unregisterRadioServiceListener(RSL);
    }

    private void channelClicked(final Channel channel){
        radioInteraction.channelPressed(channel);
    }

    private void initDefaultChannels() {
        Channel c1 = new Channel(0, "Pop Hits Station", "http://listen.radionomy.com:80/POPHITSSTATION");
        channels.add(c1);
        Channel c2 = new Channel(1, "Classic Country 1630", "http://198.105.216.204:8194/stream");
        channels.add(c2);
        Channel c3 = new Channel(2, "181.FM 80's Hairband", "http://listen.181fm.com/181-hairband_128k.mp3");
        channels.add(c3);
        saveChannels();
    }

    private void initChannels(Set<String> channelRaw) {
        for (String s : channelRaw) {
            channels.add(Channel.deserialize(s));
        }
        Collections.sort(channels);
    }

    private void saveChannels() {
        Set<String> serialized = new HashSet<String>();
        for (Channel c : channels) {
            serialized.add(c.serialize());
        }
        getStoredProductStates().edit().putStringSet(KEY, serialized).apply();
    }

    private class ChannelsAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Channel> channels;

        private int primaryColor, textColor;

        ChannelsAdapter(Context context, ArrayList<Channel> channels){
            super();
            this.context = context;
            this.channels = channels;
            primaryColor = ContextCompat.getColor(context, R.color.colorPrimary);
            textColor = 0xFFCCCCCC;
        }

        @Override
        public int getCount() {
            return channels.size();
        }

        @Override
        public Channel getItem(int position) {
            return channels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Channel atPosition = getItem(position);
            Channel playing = radioInteraction.getCurrentChannel();
            Channel pending = radioInteraction.getPendingChannel();

            TextView item = new TextView(context);
            int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
            item.setPadding(0,padding,0,padding);
            item.setGravity(Gravity.CENTER);
            item.setText(atPosition.toString());
            item.setTextSize(24);

            if(playing != null && atPosition.equals(playing)) {
                item.setTypeface(null, Typeface.BOLD);
                item.setTextColor(primaryColor);
            } else if(pending != null && atPosition.equals(pending)){
                animatePending(pending, item);
            }else{
                item.setTextColor(textColor);
            }
            return item;
        }

        private void animatePending(Channel channel, TextView textView){
            final Channel pending = channel;
            final TextView item = textView;
            ValueAnimator pendingAnimation = ObjectAnimator.ofInt(item, "textColor", textColor, primaryColor);
            pendingAnimation.setEvaluator(new ArgbEvaluator());
            pendingAnimation.setDuration(500);
            pendingAnimation.setRepeatCount(2);
            pendingAnimation.setRepeatMode(ValueAnimator.REVERSE);
            pendingAnimation.start();
            pendingAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(pending.equals(radioInteraction.getPendingChannel()))
                        animatePending(pending, item);
                }
            });
        }
    }
}
