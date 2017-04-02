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
    private Context context;

    private String number;

    public FavoriteContact(int slot, String uri, Context context){
        super(slot,uri);
        this.context = context;
    }
    public FavoriteContact(int slot, String name, String number){
        super(slot,name);
        this.number = number;
    }

    @Override
    public String getIdentifier() {
        if(context != null)
            return getNameFromUri();
        return super.getIdentifier();
    }

    public Drawable getDrawable(){
        InputStream is;
        if((is = getThumbnailStream()) != null){
            return Drawable.createFromStream(is, null);
        }
        return null;
    }

    private InputStream getThumbnailStream(){
        if(context != null){
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
        }
        return null;
    }

    private String getNameFromUri(){
        Uri uri = Uri.parse(super.getIdentifier());
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        return cursor.getString(nameIndex);
    }
}
