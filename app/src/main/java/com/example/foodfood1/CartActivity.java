package com.example.foodfood1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
private RecyclerView mRecyclerview;
private DatabaseReference mdatabaseReference2,mydatabaserefplacedorders;
private List<AddToCart> myaddtocart;
private CartAdapter mAdapter;
    NavigationView nav2;
    ActionBarDrawerToggle toggle2;
    DrawerLayout drawerLayout2;
    Toolbar toolbar2;
Button btnclearcart,btnadd,btnplaceorder;
String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mRecyclerview=findViewById(R.id.recycler_view_cart);
        btnclearcart=findViewById(R.id.btnclearcart);
        btnadd=findViewById(R.id.btnaddmoreitems);
        btnplaceorder=findViewById(R.id.btnplaceorder);

        toolbar2=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        nav2=findViewById(R.id.navmenu2);
        drawerLayout2=findViewById(R.id.drawer2);

        toggle2=new ActionBarDrawerToggle(this,drawerLayout2,toolbar2,R.string.open,R.string.close);
        drawerLayout2.addDrawerListener(toggle2);
        toggle2.syncState();

        nav2.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.exit_app:
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        break;

                }
                return true;
            }
        });

        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        myaddtocart=new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        //get phonenumber
        phonenumber= bundle.getString("phonenumber");
        mdatabaseReference2= FirebaseDatabase.getInstance().getReference("cartitems").child(phonenumber);
        mdatabaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    AddToCart addToCart=postSnapshot.getValue(AddToCart.class);

                    myaddtocart.add(addToCart);
                }
                mAdapter=new CartAdapter(CartActivity.this,myaddtocart);
                mRecyclerview.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        btnclearcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCart();
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CartActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnplaceorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               mydatabaserefplacedorders=FirebaseDatabase.getInstance().getReference("placed_order").child(phonenumber);
               //to store the placed order in another node from where the company would get details of the placed orders and provide services to the user

                mydatabaserefplacedorders.push().setValue(myaddtocart);
                clearCart();
                Intent intent=new Intent(CartActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Order Placed.\nThank you for shopping with food food.",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void clearCart()
    {
        mdatabaseReference2.removeValue();
        myaddtocart.clear();
        mAdapter.notifyDataSetChanged();
    }
}
