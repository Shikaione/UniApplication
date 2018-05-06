package com.mpetroiu.uniapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private EditText inputEmail, inputPassword, inputName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPass);
        inputName = findViewById(R.id.username);

        findViewById(R.id.signUp).setOnClickListener(this);
        findViewById(R.id.havingAccount).setOnClickListener(this);

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
    }

    private void createAccount(String email, String password){
        Log.d(TAG, "signUp:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");

                            String name = inputName.getText().toString();
                            String email = inputEmail.getText().toString();

                            DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(mAuth.getUid());

                            Map<String, String> userMap = new HashMap<>();

                            userMap.put("name", name);
                            userMap.put("email", email);

                            docRef.set(userMap);


                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = inputName.getText().toString();
        if(TextUtils.isEmpty(name)){
            inputName.setError("Required");
            valid = false;
        }else{
            inputName.setError(null);
        }

        String email = inputEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Required.");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        String password = inputPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Required.");
            valid = false;
        } else {
            inputPassword.setError(null);
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signUp) {
            createAccount(inputEmail.getText().toString(), inputPassword.getText().toString());
        }
        else if(i == R.id.havingAccount){
            startActivity(new Intent(RegisterActivity.this, LoginOptionsActivity.class));
        }
    }

}

