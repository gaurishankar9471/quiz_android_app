package gaurishankar.com.hifisoftlab.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmail, mPassword, mCnfPassword, mName;

    private Button mSbmBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mRootRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseFirestore.getInstance();

        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setMessage("Signing Up...");
        mProgress.show();

        mEmail = findViewById(R.id.edit_email);
        mName = findViewById(R.id.edit_name);
        mPassword = findViewById(R.id.edit_password);
        mCnfPassword = findViewById(R.id.edit_cnf_password);

        mSbmBtn =  findViewById(R.id.sbm_btn);
        mSbmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUpProcess();
            }
        });

        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void startSignUpProcess() {
        final String email = mEmail.getText().toString().trim();
        final String name = mName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String cnf_password = mCnfPassword.getText().toString().trim();

        if (!password.equals(cnf_password)){
            Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_LONG).show();
            return;
        }

        mProgress.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, String> user_data = new HashMap<>();
                            user_data.put("user_name", name);
                            user_data.put("user_email",email);

                            mRootRef.collection("user_database")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .set(user_data)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mProgress.dismiss();
                                            Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mProgress.dismiss();
                                            Toast.makeText(getApplicationContext(), "Failed to save user", Toast.LENGTH_LONG).show();
                                        }
                                    });

                        }
                        else {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed to create user", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
                    }
                });




    }
}
