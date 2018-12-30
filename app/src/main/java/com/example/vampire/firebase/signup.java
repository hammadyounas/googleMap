package com.example.vampire.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signup extends AppCompatActivity {
    EditText uName;
    EditText email;
    EditText password;
    EditText confirmPassword;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("appUsers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mAuth = FirebaseAuth.getInstance();
        uName = (EditText)findViewById(R.id.userName);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);
    }

    public void Singup(View view){
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String CPassword = confirmPassword.getText().toString();
        Log.w("email =>",Email);
        Log.w("password =>",Password);
//        if(Password == CPassword){
            mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("auth fialdes");
                            postDataToFirebaseDatabase();
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            Toast.makeText(signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        }
                });
//        }

    }
    public void postDataToFirebaseDatabase(){
        String Email = email.getText().toString();
        String Name = uName.getText().toString();
        HashMap<String,String> dataMap = new HashMap<String, String>();
        dataMap.put("AccountType","User");
        dataMap.put("Email",Email);
        dataMap.put("Name",Name);
        myRef.child(mAuth.getInstance().getCurrentUser().getUid()).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task){
             if(task.isSuccessful()){
                 Toast.makeText(signup.this,"successfull authenticated",Toast.LENGTH_SHORT).show();
                 Intent i = new Intent(signup.this , MainActivity.class);
                 startActivity(i);
             }
             else{
                 Toast.makeText(signup.this,"authentication failed",Toast.LENGTH_SHORT).show();
             }
           }
        });
    }
}
