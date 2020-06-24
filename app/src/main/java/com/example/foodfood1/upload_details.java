package com.example.foodfood1;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Timer;

public class upload_details extends AppCompatActivity {
    private  static final int PICK_IMAGE_REQUEST=1;
    private Button btnchooseimage,btnupload;
    private EditText etfoodname,etfoodprice;
    private ImageView myimageview;
    private ProgressBar myprogressbar;
    private Uri mImageUri;
    TextView tvhomescreen;
    private StorageReference mystoragereference;
    private DatabaseReference mydatabasereference;
    private StorageTask muploadtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_details);
        btnchooseimage=findViewById(R.id.btnchoosefile);
        btnupload=findViewById(R.id.btnupload);
        etfoodname=findViewById(R.id.etfdname);
        etfoodprice=findViewById(R.id.etfdprice);
        myimageview=findViewById(R.id.myimageview);
        myprogressbar=findViewById(R.id.progress_bar);
        tvhomescreen=findViewById(R.id.tvhomescreen);

        mystoragereference= FirebaseStorage.getInstance().getReference("uploads");
        mydatabasereference= FirebaseDatabase.getInstance().getReference("uploads");


        btnchooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etfoodprice.setText("");
                etfoodname.setText("");
             openFileChooser();
            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(etfoodname.getText().toString().isEmpty()||etfoodprice.getText().toString().isEmpty())
                 Toast.makeText(getApplicationContext(),"both the food name and food price should be entered",Toast.LENGTH_SHORT).show();
             else if(muploadtask!=null&&muploadtask.isInProgress()) {
                 Toast.makeText(getApplicationContext(),"upload in progress",Toast.LENGTH_SHORT).show();
             }
             else
                 uploadFile();
            }
        });
        tvhomescreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent int1=new Intent(upload_details.this,MainActivity.class);
             startActivity(int1);
            }
        });
    }
   private void openFileChooser(){
       Intent intent=new Intent();
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri=data.getData();
            myimageview.setImageURI(mImageUri);
        }
    }
    private  String getFileExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private  void uploadFile(){
        if(mImageUri!=null){
            StorageReference fileReference=mystoragereference.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
            muploadtask=fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if(taskSnapshot.getMetadata()!=null){
                        if(taskSnapshot.getMetadata().getReference()!=null){
                            Task<Uri> result=taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl=uri.toString();
                                    myprogressbar.setProgress(0);
                                    Toast.makeText(getApplicationContext(), "Upload Successful!!", Toast.LENGTH_SHORT).show();
                                    Upload upload=new Upload(etfoodname.getText().toString(),etfoodprice.getText().toString(),imageUrl);
                                    String uploadid=mydatabasereference.push().getKey();
                                    assert uploadid != null;
                                    mydatabasereference.child(uploadid).setValue(upload);
                                }
                            });
                        }
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){

                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                myprogressbar.setProgress((int)progress);
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }
    }
}
