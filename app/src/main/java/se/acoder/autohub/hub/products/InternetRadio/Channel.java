package se.acoder.autohub.hub.products.InternetRadio;

import android.support.annotation.NonNull;

import se.acoder.autohub.hub.products.SlottedItem;

/**
 * Created by Johannes Westlund on 2017-03-18.
 */

public class Channel extends SlottedItem implements Comparable<Channel> {
    private String url;

    public Channel(int slot, String name, String url){
        super(slot,name);
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String serialize(){
        return super.toCSV(url);
    }

    public static Channel deserialize(String csv){
        String[] params = fromCSV(csv);
        return new Channel(Integer.parseInt(params[0]), params[1], params[2]);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Channel)
            return super.equals(obj);
        return false;
    }

    public int compareTo(@NonNull Channel o) {
        return getSlot() - o.getSlot();
    }

}
