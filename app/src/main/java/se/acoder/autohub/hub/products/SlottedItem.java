package se.acoder.autohub.hub.products;

import android.support.annotation.NonNull;

/**
 * Created by Johannes Westlund on 2017-04-01.
 */

public abstract class SlottedItem {
    private final static String DELIMITER = "^";

    private int slot;
    private String name;

    public SlottedItem(int slot, String name){
        this.slot = slot;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSlot() {
        return slot;
    }

    public String getName() {
        return name;
    }

    public String toCSV(String... params){
        String csv = slot + DELIMITER + name + DELIMITER;
        for(int i = 0; i<params.length; i++){
            csv = csv+params[i]+DELIMITER;
        }
        return csv;
    }

    public static String[] fromCSV(String csv){
        return csv.split("\\^");
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SlottedItem){
            SlottedItem other = (SlottedItem) obj;
            return slot == other.slot && name.equals(other.name);
        }
        return false;
    }
}
