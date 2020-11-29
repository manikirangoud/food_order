package com.mkg.orderfood;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkg.foodorder.R;


/**
 * Created by Manikiran Goud on 23-02-2018.
 */

public class RestaurantMenu extends AppCompatActivity implements View.OnClickListener{

    private GridLayout gridLayoutAddMenu;
    private View view;
    private LayoutInflater layoutInflater;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static int images[] = {R.drawable.item_0005, R.drawable.item_0006, R.drawable.item_0007, R.drawable.item_0008,
            R.drawable.item_0001, R.drawable.item_0002, R.drawable.item_0003, R.drawable.item_0004};

    private static String[] itemsName = {"Tomato Soup", "Crispy Corn", "Tandoori Chicken", "Murgh Malai Kebab", "Special Kebab",
            "Chicken 65", "Chicken Lollipop", "Kodi Vepudu"};

    private static String type1 = "Starters", type2 = "Soups";

    private static String[] prices = {"80", "200", "180", "300", "250", "190", "150", "250"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.restaurant_menu);

        gridLayoutAddMenu = ( GridLayout ) findViewById(R.id.ll_add_restaurant_menu);
        layoutInflater = ( LayoutInflater ) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = findViewById(R.id.ll_restaurant_menu);
        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);



        for(int i = 0; i < 8; i++) {
            View view = layoutInflater.inflate(R.layout.menu_item_layout, null);
            ImageView imageView = ( ImageView ) view.findViewById(R.id.menu_item_img);
            TextView textViewItemName = ( TextView ) view.findViewById(R.id.tv_menu_item);
            TextView textViewItemType = ( TextView ) view.findViewById(R.id.tv_item_type);
            TextView textViewItemPrice = ( TextView ) view.findViewById(R.id.tv_item_price);

            imageView.setImageResource(images[i]);
            textViewItemName.setText(itemsName[i]);
            if(i == 0) {
                textViewItemType.setText(type2);
            } else {
                textViewItemType.setText(type1);
            }
            textViewItemPrice.setText(prices[i]);

            Button button = ( Button ) view.findViewById(R.id.button_item);
            button.setOnClickListener(this);
            gridLayoutAddMenu.addView(view);
        }
    }


    @Override
    public void onClick(View v) {
        editor = sharedPreferences.edit();
        editor.putBoolean("itemsAvailable", true);
        editor.apply();
        Snackbar.make(view, "Item Added to Cart", Snackbar.LENGTH_SHORT).show();

    }
}
