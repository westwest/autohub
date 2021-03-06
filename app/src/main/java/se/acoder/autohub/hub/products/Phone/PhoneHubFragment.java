package se.acoder.autohub.hub.products.Phone;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import se.acoder.autohub.HubApp;
import se.acoder.autohub.R;
import se.acoder.autohub.hub.products.ProductFragment;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Johannes Westlund on 2017-03-29.
 */

public class PhoneHubFragment extends ProductFragment {
    private final static String KEY = "favcontacts";
    private final static int RESULT_PICK_CONTACT_BASE = 210;

    // Fields for managing favorite contacts
    private ArrayList<FavoriteContact> favContacts = new ArrayList<>();
    private TextView[] slots = new TextView[3];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Set<String> contactsRaw = getStoredProductStates().getStringSet(KEY, new HashSet<String>());
        if(contactsRaw.isEmpty()){
            initEmptyContacts();
        } else {
            initContacts(contactsRaw);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater,container,savedInstanceState);
        ViewGroup productView= (ViewGroup) rootView.findViewById(R.id.product_view);
        View phoneView = inflater.inflate(R.layout.phone_main, productView, false);
        wireFavContacts(phoneView);
        productView.addView(phoneView);
        return rootView;
    }

    private void initEmptyContacts(){
        FavoriteContact[] contacts = new FavoriteContact[3];
        for(int i=0; i<3; i++){
            favContacts.add(new FavoriteContact(i, "Empty Slot", ""));
        }
        saveContacts();
    }

    private void initContacts(Set<String> contactsRaw){
        for (String s : contactsRaw) {
            favContacts.add(FavoriteContact.deserialize(s));
        }
        Collections.sort(favContacts);
    }

    private void wireFavContacts(View phoneView){
        slots[0] = (TextView) phoneView.findViewById(R.id.favcontact0);
        slots[1] = (TextView) phoneView.findViewById(R.id.favcontact1);
        slots[2] = (TextView) phoneView.findViewById(R.id.favcontact2);
        for(int i = 0; i<slots.length; i++){
            final int index = i;
            slots[i].setText(favContacts.get(i).getIdentifier(getContext()));
            setThumbToSlot(favContacts.get(i).getDrawable(getContext()), i);
            slots[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    createContactSlotDialog(index).show();
                    return true;
                }
            });
            slots[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!favContacts.get(index).getIdentifier().contains("Empty"))
                        call(favContacts.get(index).getNumber(getContext()));
                    else
                        createContactSlotDialog(index).show();
                }
            });
        }
    }

    private void saveContacts() {
        Set<String> serialized = new HashSet<String>();
        for(FavoriteContact c : favContacts){
            serialized.add(c.serialize());
        }
        getStoredProductStates().edit().putStringSet(KEY, serialized).apply();
    }

    protected void call(String number) {
        if(number != null){
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, HubApp.PHONE_CALL_REQUEST);
                return;
            }
            Uri tel = Uri.parse("tel:"+number);
            Intent callIntent = new Intent(Intent.ACTION_CALL, tel);
            startActivity(callIntent);
        } else {
            Toast.makeText(getContext(), "This contact has no phone-number", Toast.LENGTH_SHORT).show();
        }
    }

    private Dialog createContactSlotDialog(final int slot){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.phone_addfav_dialog,null);
        TextView title = (TextView) dialogView.findViewById(R.id.phone_slot_edit_title);
        title.setText(title.getText()+" " + (slot+1));
        View btnCustom = dialogView.findViewById(R.id.phone_add_custom);
        View btnFromContacts = dialogView.findViewById(R.id.phone_add_from_contacts);
        builder.setView(dialogView);

        final Dialog dialog = builder.create();
        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCustomNumber(slot);
                dialog.dismiss();
            }
        });
        btnFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, RESULT_PICK_CONTACT_BASE+slot);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    private void createCustomNumber(final int slot){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please input name and number below");
        LinearLayout list = new LinearLayout(getContext());
        list.setOrientation(LinearLayout.VERTICAL);
        final EditText nameInput = new EditText(getContext());
        nameInput.setHint("Name");
        final EditText numInput = new EditText(getContext());
        numInput.setHint("Number");
        numInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        list.addView(nameInput);
        list.addView(numInput);
        builder.setView(list);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameInput.getText().toString();
                String number = numInput.getText().toString();
                if(!name.isEmpty() && PhoneNumberUtils.isGlobalPhoneNumber(number)){
                    changeSlot(slot, new FavoriteContact(slot, name, number));
                    dialog.dismiss();
                }else{
                    Toast.makeText(getContext(),
                                   "Creation failed. Please provide a name and a valid number",
                                   Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void changeSlot(int slot, FavoriteContact contact){
        favContacts.set(slot, contact);
        slots[slot].setText(contact.getIdentifier(getContext()));
        setThumbToSlot(contact.getDrawable(getContext()), slot);
        saveContacts();
    }

    private void contactPickedForSlot(int slot, Intent data){
        changeSlot(slot, new FavoriteContact(slot, data.getDataString()));
    }

    private void setThumbToSlot(Drawable thumb, int slot){
        if(thumb != null){
            thumb.setBounds(slots[slot].getCompoundDrawables()[0].getBounds());
            slots[slot].setCompoundDrawables(thumb,null,null,null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode/RESULT_PICK_CONTACT_BASE == 1){
                contactPickedForSlot(requestCode%RESULT_PICK_CONTACT_BASE, data);
            }
        }
    }
}
