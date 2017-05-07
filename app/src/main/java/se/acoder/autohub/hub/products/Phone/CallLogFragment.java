package se.acoder.autohub.hub.products.Phone;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import se.acoder.autohub.R;

/**
 * Created by Johannes Westlund on 2017-05-07.
 */

public class CallLogFragment extends ListFragment {
    private CallsAdapter callsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callsAdapter = new CallsAdapter(getContext(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        getActivity().getSupportLoaderManager().initLoader(0,null,loaderCallbacks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setListAdapter(callsAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String number = callsAdapter.getNumber((Cursor) callsAdapter.getItem(position));
        ((PhoneHubFragment) getParentFragment()).call(number);
    }

    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader onCreateLoader(int id, Bundle args) {
            String[] projection = { CallLog.Calls._ID,
                                    CallLog.Calls.TYPE,
                                    CallLog.Calls.NUMBER,
                                    CallLog.Calls.CACHED_NAME,
                                    CallLog.Calls.DATE};
            String sortOrder = CallLog.Calls.DATE + " DESC LIMIT 50";
            return new CursorLoader(getContext(),
                                    CallLog.Calls.CONTENT_URI,
                                    projection, null, null, sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            callsAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader loader) {
            callsAdapter.swapCursor(null);
        }
    };

    private class CallsAdapter extends ResourceCursorAdapter {

        public CallsAdapter(Context context, Cursor cursor, int flags ){
            super(context, R.layout.call_log_row, cursor, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView typeThumb = (ImageView) view.findViewById(R.id.call_log_type);
            typeThumb.setImageDrawable(getTypeThumb(context, cursor));

            TextView contactText = (TextView) view.findViewById(R.id.call_log_caller);
            contactText.setText(getContactDisplay(cursor));

            TextView dateText = (TextView) view.findViewById(R.id.call_log_date);
            dateText.setText(getDate(cursor));
        }

        private Drawable getTypeThumb(Context context, Cursor cursor){
            int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
            Drawable thumb = null;
            switch(type){
                case CallLog.Calls.INCOMING_TYPE:
                    thumb = ContextCompat.getDrawable(context, android.R.drawable.sym_call_incoming);
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    thumb = ContextCompat.getDrawable(context, android.R.drawable.sym_call_outgoing);
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    thumb = ContextCompat.getDrawable(context, android.R.drawable.sym_call_missed);
                    break;
            }
            return thumb;
        }

        private String getContactDisplay(Cursor cursor){
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            if(name != null)
                return name;
            return getNumber(cursor);
        }

        private String getDate(Cursor cursor){
            String rawDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            long t = Long.parseLong(rawDate);
            Date d = new Date(t);
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            if(dateFormat.format(d).equals(dateFormat.format(now)))
                return timeFormat.format(d);
            return dateFormat.format(d);
        }

        private String getNumber(Cursor cursor){
            return cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
        }
    }
}
