package se.acoder.autohub.products;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import se.acoder.autohub.R;
import se.acoder.autohub.products.InternetRadio.InternetRadioProduct;
import se.acoder.autohub.products.Phone.PhoneProduct;

/**
 * Created by Johannes Westlund on 2017-03-16.
 */

public class ProductManager {
    private Context context;
    private Set<String> purchasedProducts;
    private final String KEY = "products";
    private ArrayList<Product> products;

    //Standard product constants
    private final String P_PHONE = "phone";
    private final String P_RADIO = "radio";
    private final String P_SHOP = "shop";

    private final String[] stdProducts = {
            P_PHONE, P_RADIO, P_SHOP
    };

    public ProductManager(Context context){
        this.context = context;
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(stdProducts));

        SharedPreferences SP = this.context.getSharedPreferences("purchasedProducts", Context.MODE_PRIVATE);
        Set<String> purchasedProducts = SP.getStringSet(KEY, null);
        if(purchasedProducts != null)
            names.addAll(purchasedProducts);

        products = createProducts(names);
    }

    private ArrayList<Product> createProducts(ArrayList<String> names){
        ArrayList<Product> list = new ArrayList<Product>();
        for (String name: names) {
            Product p;
            switch(name){
                case P_PHONE:
                    list.add(new PhoneProduct());
                    break;
                case P_RADIO:
                    list.add(new InternetRadioProduct());
            }
        }
        return list;
    }

    public List<Product> getProducts(){
        return products;
    }
}
