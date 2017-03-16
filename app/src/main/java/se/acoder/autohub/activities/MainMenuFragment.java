package se.acoder.autohub.activities;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import se.acoder.autohub.R;
import se.acoder.autohub.products.Product;
import se.acoder.autohub.products.ProductManager;

/**
 * Created by Johannes Westlund on 2017-03-16.
 */

public class MainMenuFragment extends Fragment {
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
        rootView.setBackgroundColor(Color.BLUE);
        ViewPager menu = new ViewPager(getContext());
        menu.setAdapter(new MenuPagerAdapter(getContext(), PM.getProducts()));
        rootView.addView(menu);
        return rootView;
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
            Log.d("TEST", "CALLED");
            return view == ((LinearLayout)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View item_view = layoutInflater.inflate(R.layout.menu_page_layout, container, false);

            Button slot0 = (Button) item_view.findViewById(R.id.slot0);
            Button slot1 = (Button) item_view.findViewById(R.id.slot1);
            Button slot2 = (Button) item_view.findViewById(R.id.slot2);

            Log.d("TEST", slot0.toString());

            Button[] slots = {slot0,slot1,slot2};

            int widgetsOnPage = items.size() - position*3;

            for(int i=0; i<pageSize; i++){
                if(i < widgetsOnPage){
                    Product p = items.get(position*3+i);
                    slots[i].setText(p.getName());
                    Drawable icon = ContextCompat.getDrawable(getContext(), p.getIcon());
                    slots[i].setCompoundDrawablesWithIntrinsicBounds(null,icon,null,null);
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
