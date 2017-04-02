package se.acoder.autohub.hub.products.Phone;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.ByteArrayInputStream;
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
        InputStream is = null;
        if((is = getThumbnailStream()) != null){
            return Drawable.createFromStream(is, null);
        }
        return ContextCompat.getDrawable(context, R.drawable.ic_generic_person);
    }

    private InputStream getThumbnailStream(){
        if(context != null){
            Uri uri = Uri.parse(super.getIdentifier());
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            int imgUriIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI);
            cursor.moveToFirst();
            Uri thumbUri = Uri.parse(cursor.getString(imgUriIndex));
            cursor = context.getContentResolver().query(thumbUri, null, null, null, null);
            //int imgIndex = cursor.getColumnIndex();
            cursor.moveToFirst();
            for(int i = 0; i<cursor.getColumnCount(); i++){
                Log.d("TEST", cursor.getColumnName(i));
            }
            //return new ByteArrayInputStream(cursor.getBlob(imgIndex));
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
