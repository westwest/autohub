package se.acoder.autohub.hub;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import se.acoder.autohub.HubApp;
import se.acoder.autohub.R;
import se.acoder.autohub.hub.products.Phone.PhoneProduct;
import se.acoder.autohub.hub.products.Product;
import se.acoder.autohub.hub.products.ProductManager;

/**
 * Created by Johannes Westlund on 2017-03-16.
 */

public class HubMenuFragment extends Fragment {
    ProductManager PM;
    ViewGroup rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PM = new ProductManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.main_menu_layout, container, false);
        ViewPager menu = new ViewPager(getContext());
        menu.setAdapter(new MenuPagerAdapter(getContext(), PM.getProducts()));
        rootView.addView(menu);
        return rootView;
    }

    public void triggerProductInit(Product product){
        List<Product> products = PM.getProducts();
        Product p = products.get(products.indexOf(product));
        getActivity().getSupportFragmentManager().beginTransaction()
                     .replace(R.id.mainView, p.bootstrap())
                     .addToBackStack(p.getName()+"-entrypoint")
                     .commitAllowingStateLoss();
    }

    private void initProduct(Product p){
        getActivity().getSupportFragmentManager().beginTransaction()
                                                 .replace(R.id.mainView, p.bootstrap())
                                                 .addToBackStack(p.getName()+"-entrypoint")
                                                 .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case HubApp.PHONE_GATE_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    triggerProductInit(new PhoneProduct());
            }
        }
    }

    private class MenuPagerAdapter extends PagerAdapter{
        private Context context;
        private List<Product> items;
        private final int pageSize = 3;

        MenuPagerAdapter(Context context, List<Product> products){
            this.context = context;
            items = products;
        }

        @Override
        public int getCount() {
            return (int)Math.ceil((float) items.size()/pageSize);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View item_view = layoutInflater.inflate(R.layout.menu_page_layout, container, false);

            TextView slot0 = (TextView) item_view.findViewById(R.id.slot0);
            TextView slot1 = (TextView) item_view.findViewById(R.id.slot1);
            TextView slot2 = (TextView) item_view.findViewById(R.id.slot2);

            TextView[] slots = {slot0,slot1,slot2};

            int widgetsOnPage = items.size() - position*3;

            for(int i=0; i<pageSize; i++){
                if(i < widgetsOnPage){
                    final Product p = items.get(position*3+i);
                    slots[i].setText(p.getName());
                    Drawable icon = ContextCompat.getDrawable(getContext(), p.getIcon());
                    slots[i].setCompoundDrawablesWithIntrinsicBounds(null,icon,null,null);
                    slots[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(p.ensureGatePermission(getContext()))
                                initProduct(p);
                        }
                    });
                }else{
                    slots[i].setAlpha(0);
                }
            }
            container.addView(item_view);
            return item_view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
