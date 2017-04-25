package se.acoder.autohub.hub.products.Phone;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import se.acoder.autohub.R;
import se.acoder.autohub.hub.products.SlottedItem;

/**
 * Created by Johannes Westlund on 2017-03-31.
 */

public class FavoriteContact extends SlottedItem{
    private String number;

    public FavoriteContact(int slot, String uri){
        super(slot,uri);
    }
    public FavoriteContact(int slot, String name, String number){
        super(slot,name);
        this.number = number;
    }

    public String getIdentifier(Context context) {
        if(number == null)
            return getNameFromUri(context);
        return super.getIdentifier();
    }

    public Drawable getDrawable(Context context) {
        if (number == null){
            InputStream is;
            if ((is = getThumbnailStream(context)) != null) {
                return Drawable.createFromStream(is, null);
            }
        }
        return null;
    }

    private InputStream getThumbnailStream(Context context){
        Uri uri = Uri.parse(super.getIdentifier());
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        int imgUriIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI);
        try {
            cursor.moveToFirst();
            String thumbUri = cursor.getString(imgUriIndex);
            if(thumbUri != null)
                return context.getContentResolver().openInputStream(Uri.parse(thumbUri));
        } catch (FileNotFoundException FNFE){
            Log.d(FavoriteContact.class.toString(), FNFE.getCause().toString());
            FNFE.printStackTrace();
        }
        return null;
    }

    private String getNameFromUri(Context context){
        Uri uri = Uri.parse(super.getIdentifier());
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        return cursor.getString(nameIndex);
    }

    public String serialize(){
        if(number == null)
            return super.toCSV();
        return super.toCSV(number);
    }

    public static FavoriteContact deserialize(String csv){
        String[] params = fromCSV(csv);
        if(csv.contains("Empty Slot"))
            return new FavoriteContact(Integer.parseInt(params[0]), params[1], "");
        if(params.length == 3)
            return new FavoriteContact(Integer.parseInt(params[0]), params[1], params[2]);
        if(params.length == 2)
            return new FavoriteContact(Integer.parseInt(params[0]), params[1]);
        return null;
    }
}
