package com.example.foodfood1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class OpenDetailsActivity extends AppCompatActivity {
    ImageView foodimage;
    TextView foodName, foodPrice, foodQuantity, tvbacktomenu;
    Button btnminus, btnplus, btnaddtocart, btnmenu;
    EditText etmobilenumber;
    //to get the phonenumber to make a child inside addtocart node in the firebase to separate the orders of different users using different phonenumbers
    private DatabaseReference mydatabasereference1;
    NavigationView nav1;
    ActionBarDrawerToggle toggle1;
    DrawerLayout drawerLayout1;
    Toolbar toolbar1;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    int flagHasSharedPreference=0;
    int c = 1;//for storing quantity of food item set as 1 by default
    String storePrice;//to store the price of 1 food item
    int amt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_details);
        foodimage = findViewById(R.id.imgviewopendetails);
        foodName = findViewById(R.id.tvnameopendetail);
        foodPrice = findViewById(R.id.tvpriceopendetail);
        foodQuantity = findViewById(R.id.tvquantiyopendetail);
        btnminus = findViewById(R.id.btnminus);
        btnplus = findViewById(R.id.btnplus);
        btnaddtocart = findViewById(R.id.btnaddtocart);
        btnmenu = findViewById(R.id.btnshowcart);
        tvbacktomenu = findViewById(R.id.tvbacktomenu);
        etmobilenumber = findViewById(R.id.etmobilenumber);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        checkSharedPreferences();

        toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        nav1 = findViewById(R.id.navmenu1);
        drawerLayout1 = findViewById(R.id.drawer1);

        toggle1 = new ActionBarDrawerToggle(this, drawerLayout1, toolbar1, R.string.open, R.string.close);
        drawerLayout1.addDrawerListener(toggle1);
        toggle1.syncState();

        nav1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.exit_app:
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        break;
                    case R.id.showcart:
                        Intent int1 = new Intent(OpenDetailsActivity.this, CartActivity.class);
                        int1.putExtra("phonenumber",etmobilenumber.getText().toString());
                        startActivity(int1);
                        break;
                }
                return true;
            }
        });



        Bundle bundle = getIntent().getExtras();
        //get name of food
        String name = bundle.getString("name");
        //get price of food
        final String price = bundle.getString("price");
        assert price != null;
        //get price of food without any spaces
        String pricewithoutwhitespaces = price.replaceAll(" ", "");
        //storeprice for storing pricewithout white spaces to be used in NewPriceWithChangingQuantity()
        storePrice = pricewithoutwhitespaces;
        String imageuri = bundle.getString("image");
        Picasso.with(OpenDetailsActivity.this).load(imageuri).placeholder(R.drawable.foodfoo).fit().into(foodimage);
        foodName.setText(name);
        foodPrice.setText(price);
        if (c == 1)
            btnminus.setEnabled(false);
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c++;
                foodQuantity.setText(Integer.toString(c));
                if (c > 1) {
                    btnminus.setEnabled(true);
                    NewPriceWithChangingQuantity();
                }
            }
        });
        btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c--;
                if (c <= 1) {
                    btnminus.setEnabled(false);
                    c = 1;
                }
                foodQuantity.setText(Integer.toString(c));
                NewPriceWithChangingQuantity();
            }
        });
        if(flagHasSharedPreference==1)
        {
            etmobilenumber.setEnabled(false);
        }
        btnaddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnaddtocart.getText().toString().compareTo("ADD TO ORDER") == 0) {
                    int c1 = 0;
                    if (etmobilenumber.getText().toString().length() != 10) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                        c1++;
                    }
                    if (etmobilenumber.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter your phone number to continue", Toast.LENGTH_SHORT).show();
                        c1++;
                    }
                    if (c1 == 0) {
                        if(flagHasSharedPreference==0) {
                            String inp = etmobilenumber.getText().toString();
                            mEditor.putString(getString(R.string.mobile), inp);
                            mEditor.apply();
                        }
                        etmobilenumber.setEnabled(false);

                        mydatabasereference1 = FirebaseDatabase.getInstance().getReference("cartitems").child(etmobilenumber.getText().toString());
                        //child is made in the cart node using phonenumber as child name to separate the carts of each different customer since everyone have different phone numbers
                        AddToCart addToCart = new AddToCart(foodName.getText().toString(), storePrice, foodQuantity.getText().toString(), foodPrice.getText().toString());
                        String id = mydatabasereference1.push().getKey();
                        assert id != null;
                        mydatabasereference1.child(id).setValue(addToCart);
                        Toast.makeText(getApplicationContext(), foodName.getText().toString().concat(" Added to cart"), Toast.LENGTH_SHORT).show();
                        btnaddtocart.setText("SHOW CART");

                    }
                } else {
                    Intent int1 = new Intent(OpenDetailsActivity.this, CartActivity.class);
                    int1.putExtra("phonenumber",etmobilenumber.getText().toString());
                    startActivity(int1);
                    btnaddtocart.setText("ADD TO ORDER");
                }
            }
        });
        btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(OpenDetailsActivity.this, MainActivity.class);
                startActivity(int1);
            }
        });
        tvbacktomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(OpenDetailsActivity.this, MainActivity.class);
                startActivity(int1);
            }
        });

    }


    public void NewPriceWithChangingQuantity() {

        int l = storePrice.length(), t = 3;
        String rupeesinwords = storePrice.substring(0, t);
        String pricenumber = storePrice.substring(t, l);
        int priceininteger = Integer.parseInt(pricenumber);
        amt = priceininteger * c;
        String stringprice = Integer.toString(amt);
        String amount = rupeesinwords.concat(stringprice);
        foodPrice.setText(amount);

    }

    private void checkSharedPreferences() {
        String input1 = mPreferences.getString(getString(R.string.mobile), "");
        if(input1.length()==10)
            flagHasSharedPreference=1;
        etmobilenumber.setText(input1);
    }
}

