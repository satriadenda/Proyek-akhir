package com.udinus.newsahkesport.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udinus.newsahkesport.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail;
    private EditText edtPassword;
    private CheckBox chkRememberEmail;
    private Spinner spnRole;
    private TextView tvRegister;

    private SharedPreferences sharedPrefs;
    private static final String EMAIL_KEY = "key_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.sharedPrefs = this.getSharedPreferences("newsahkesport_sharedprefs", Context.MODE_PRIVATE);
        this.initComponents();
        this.loadSavedEmail();
    }

    private void initComponents() {
        this.edtEmail = this.findViewById(R.id.edtEmail);
        this.edtPassword = this.findViewById(R.id.edtPassword);
        this.chkRememberEmail = this.findViewById(R.id.chk_remember_email);
        this.spnRole = this.findViewById(R.id.spnRole);
        this.tvRegister = this.findViewById(R.id.tvRegister);
    }

    public void clickLogin(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mAuth.signInWithEmailAndPassword(this.edtEmail.getText().toString(), this.edtPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        CollectionReference accountsRef = mFirestore.collection(spnRole.getSelectedItem().toString());
                        DocumentReference uidRef = accountsRef.document(authResult.getUser().getUid());

                        uidRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot ds) {
                                if (ds.exists()) {
                                    Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                                    ii.putExtra("USER_ROLE", spnRole.getSelectedItem().toString());
                                    startActivity(ii);
                                    saveEmail();
                                } else {
                                    Toast.makeText(getApplicationContext(), "User is not found. Please check your username/password again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }

    public void clickRegister(View view) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    private void saveEmail() {
        SharedPreferences.Editor editor = this.sharedPrefs.edit();

        if (this.chkRememberEmail.isChecked())
            editor.putString(EMAIL_KEY, this.edtEmail.getText().toString());
        else
            editor.remove(EMAIL_KEY);

        editor.apply();
    }

    private void loadSavedEmail() {
        String savedEmail =
                this.sharedPrefs.getString(EMAIL_KEY, null);

        if (savedEmail != null) {
            this.edtEmail.setText(savedEmail);

            this.chkRememberEmail.setChecked(true);
        }
    }
}