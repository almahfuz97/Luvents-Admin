package com.example.myapplication.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialEditText email,pass;
    private Button login;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog,progressDialog2;


    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //findingView
        email=findViewById(R.id.email_id);
        pass=findViewById(R.id.password_id);
        login=findViewById(R.id.login_id);

        //onClick
        login.setOnClickListener(this);

        //firebase
        mAuth=FirebaseAuth.getInstance();

        //progressDialogue
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging In...");
        progressDialog2=new ProgressDialog(this);
        progressDialog2.setMessage("Checking...");
    }



    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.login_id)
        {
            adminLogin();


        }

    }

    private void adminLogin() {
        final String Email=email.getText().toString().trim();
        String Pass=pass.getText().toString();

        //checking

        if(Email.isEmpty())
        {
            email.setError("Email Required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
        {
            email.setError("Invalid Email");
            email.requestFocus();
            return;
        }
        if(Pass.isEmpty())
        {
            pass.setError("Incorrect Password");
            pass.requestFocus();
            return;
        }

        progressDialog.show();



        //firebaselogin

        mAuth.signInWithEmailAndPassword(Email,Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful())
                        {
                            DataBaseAuth(Email);

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    //AdminDatabaseAuth
    String Email;
    public void DataBaseAuth(String email)
    {
        Email=email;

        db.collection("Admins").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {



                            progressDialog.cancel();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String email = document.getString("email");

                                if (email.equals(Email)) {
                                    Toast.makeText(getApplicationContext(), "Login succesfull", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(MainActivity.this, Bottom_Navigation.class));
                                    finish();
                                } else {

                                    Toast.makeText(getApplicationContext(), "You are not an admin!", Toast.LENGTH_LONG).show();

                                }
                            }

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.cancel();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }
}
