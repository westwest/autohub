package se.acoder.autohub.hub.products.Phone;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

    public String getNumber(Context context){
        if(number == null){
            int id = getIdFromUri(context);
            if(id != -1){
                Map<Integer,String> numbers = new HashMap<>();
                String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id;
                Cursor phones = context.getContentResolver()
                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, where, null, null);
                while(phones.moveToNext()){
                    String k = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    String n = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                    numbers.put(Integer.parseInt(k),n);
                }
                phones.close();
                number = selectNumber(numbers);
            }
        }
        return number;
    }

    /**
     * Selects which number to dial if contact is found to have several. Prioritization is
     * mobile if possible, else home-number.
     * @param numbers
     * @return
     */
    private String selectNumber(Map<Integer,String> numbers){
        if(numbers.containsKey(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE))
            return numbers.get(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        if(numbers.containsKey(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE))
            return numbers.get(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE);
        if(numbers.containsKey(ContactsContract.CommonDataKinds.Phone.TYPE_HOME))
            return numbers.get(ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        else
            return numbers.get(ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
    }

    public Drawable getDrawable(Context context) {
        if (number == null){
            InputStream is;
            if ((is = getThumbnailStream(context)) != null) {
                return Drawable.createFromStream(is, null);
            }
        }
        return ContextCompat.getDrawable(context, R.drawable.ic_generic_person);
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

    private int getIdFromUri(Context context){
        int id = -1;
        Uri uri = Uri.parse(super.getIdentifier());
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
        id = cursor.getInt(idIndex);
        cursor.close();
        return id;
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
