package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UserRegInfoActivity extends AppCompatActivity {

    TextView nameTv,emailTv,universityNameTv,contactNumberTv,bkashTrxTv;
    Spinner spinner;
    Button saveButton;

    FirebaseFirestore firebaseFirestore;
    Query query;

    String name,email,universityName,contactNumber,bkashTrx,scheduleId,eventId,userRegId,status,spinnerItem;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg_info);

        nameTv=findViewById(R.id.userInfoNameTvId);
        emailTv=findViewById(R.id.userInfoEmailTvId);
        universityNameTv=findViewById(R.id.userInfoUniversityTvId);
        contactNumberTv=findViewById(R.id.userInfoContactTv);
        bkashTrxTv=findViewById(R.id.userInfoBkashTv);
        spinner=findViewById(R.id.userInfoSpinnerId);
        saveButton=findViewById(R.id.userInfoSaveId);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");


        Intent intent=getIntent();
        name=intent.getStringExtra("userName");
        email=intent.getStringExtra("email");
        universityName=intent.getStringExtra("departmentName");
        contactNumber=intent.getStringExtra("contactNumber");
        bkashTrx=intent.getStringExtra("bkashTrx");
        scheduleId=intent.getStringExtra("scheduleId");
        eventId=intent.getStringExtra("eventId");
        userRegId=intent.getStringExtra("userRegId");
        status=intent.getStringExtra("status");

        nameTv.setText(name);
        emailTv.setText(email);
        universityNameTv.setText(universityName);
        contactNumberTv.setText(contactNumber);
        bkashTrxTv.setText(bkashTrx);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item=spinner.getItemAtPosition(i).toString();

                if (item.equals("Accept"))
                {
                    spinnerItem="Completed";
                }
                if (item.equals("Pending"))
                {
                    spinnerItem="Pending";
                }if (item.equals("Reject"))
                {
                    spinnerItem="Rejected";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDatabase();
            }
        });

        firebaseFirestore=FirebaseFirestore.getInstance();
    }

    private void updateDatabase() {

        progressDialog.show();
        firebaseFirestore.collection("RegistrationInfo").document(eventId)
                .collection("Schedules")
                .document(scheduleId)
                .collection("userFormInfo")
                .document(userRegId).update("status",spinnerItem)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.cancel();

                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(UserRegInfoActivity.this,SingleEventDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                            Toast.makeText(UserRegInfoActivity.this, "Updated", Toast.LENGTH_LONG).show();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(UserRegInfoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
