package se.acoder.autohub.hub.products;

import android.support.annotation.NonNull;

/**
 * Created by Johannes Westlund on 2017-04-01.
 */

public abstract class SlottedItem {
    private final static String DELIMITER = "^";

    private int slot;
    private String identifier;

    public SlottedItem(int slot, String identifier){
        this.slot = slot;
        this.identifier = identifier;
    }

    public void setIdentifier(String name) {
        this.identifier = name;
    }

    public int getSlot() {
        return slot;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String toCSV(String... params){
        String csv = slot + DELIMITER + identifier;
        for(int i = 0; i<params.length; i++){
            csv = csv + DELIMITER + params[i];
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
            return slot == other.slot && identifier.equals(other.identifier);
        }
        return false;
    }
}
