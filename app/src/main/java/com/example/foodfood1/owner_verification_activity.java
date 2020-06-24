package com.example.foodfood1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class owner_verification_activity extends AppCompatActivity {
    EditText username1,password1;
    Button buttonlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_verification_activity);
        username1=findViewById(R.id.etusername1);
        password1=findViewById(R.id.etpassword1);
        buttonlogin=findViewById(R.id.login);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c=0;
                String username=username1.getText().toString();
                String password=password1.getText().toString();

                if(password.compareTo("Rajivisgreat@1234")!=0){
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                    c++;
                }
                if(username.compareTo("Rajiv@foodfood.com")!=0){
                    Toast.makeText(getApplicationContext(), "Invalid Useername", Toast.LENGTH_SHORT).show();
                    c++;
                }
                if(password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    c++;
                }
                if(username.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_SHORT).show();
                    c++;
                }
                if(c==0){
                    Toast.makeText(getApplicationContext(), "Login Successful!!", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder=new AlertDialog.Builder(owner_verification_activity.this);
                    builder.setIcon(R.drawable.ic_account_);
                    builder.setTitle("Login successful!!");
                    builder.setMessage("Welcome Boss...you can add new food items to menu");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent int1=new Intent(owner_verification_activity.this,upload_details.class);
                            startActivity(int1);
                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();

                }

                }

        });
    }
}
