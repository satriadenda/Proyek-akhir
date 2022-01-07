package com.udinus.newsahkesport.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udinus.newsahkesport.R;
import com.udinus.newsahkesport.databinding.ActivityRegisterBinding;
import com.udinus.newsahkesport.model.UserAccount;

public class RegisterActivity extends AppCompatActivity {

    private String TAG = RegisterActivity.class.getName();
    private ActivityRegisterBinding uiBinding;
    private MaterialButton btnRegister;
    private EditText edtFullname, edtEmail, edtPhone, edtPassword, edtPasswordConfirm;
    private Spinner spnRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        getSupportActionBar().setTitle("TOKO OLAHRAGA NEWSAHKE");

        edtFullname = uiBinding.edtFullName;
        edtEmail = uiBinding.edtEmail;
        edtPhone = uiBinding.edtPhone;
        edtPassword = uiBinding.edtPassword;
        edtPasswordConfirm = uiBinding.edtConfirmPassword;
        btnRegister = uiBinding.btnRegister;
        spnRole = uiBinding.spnRole;

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = edtFullname.getText().toString();
                String email = edtEmail.getText().toString();
                String phone = edtPhone.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPassword = edtPasswordConfirm.getText().toString();
                String role = spnRole.getSelectedItem().toString();

                registerUser(fullname, phone, email, password, role);
            }
        });

        setContentView(uiBinding.getRoot());
    }

    public void registerUser(String fullname, String phone, String email, String password, String role){

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)

                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(final AuthResult authResult) {
                        if (authResult.getUser() != null) {

                                CollectionReference accountsRef = mFirestore.collection(role);
                                DocumentReference uidRef = accountsRef.document(authResult.getUser().getUid());

                                UserAccount account = new UserAccount();
                                account.setFullname(fullname);
                                account.setPhone(phone);

                                uidRef.set(account)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                finish();
                                                Intent ii = new Intent(RegisterActivity.this, MainActivity.class);
                                                ii.putExtra("USER_ROLE", role);
                                                startActivity(ii);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
         }
}
