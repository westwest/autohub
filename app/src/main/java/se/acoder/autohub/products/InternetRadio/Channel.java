package se.acoder.autohub.products.InternetRadio;

import android.support.annotation.NonNull;

/**
 * Created by Johannes Westlund on 2017-03-18.
 */

public class Channel implements Comparable<Channel>{
    private final static String DELIMITER = "^";

    private String name;
    private String url;
    private int slot;

    public Channel(int slot, String name, String url){
        this.name = name;
        this.url = url;
        this.slot = slot;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getSlot(){
        return slot;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    @Override
    public String toString() {
        return name;
    }

    public String serialize(){
        return slot+Channel.DELIMITER+name+Channel.DELIMITER+url;
    }

    public static Channel deserialize(String csv){
        String[] params = csv.split("^");
        return new Channel(Integer.parseInt(params[0]), params[1], params[2]);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Channel){
            Channel other = (Channel) obj;
            return slot == other.slot;
        }
        return false;
    }

    @Override
    public int compareTo(@NonNull Channel o) {
        return slot - o.getSlot();
    }
}
