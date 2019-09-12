package gaurishankar.com.hifisoftlab.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mSbmBtn,mSigUpBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setMessage("Loading...");

        mEmail = findViewById(R.id.edit_email);
        mPassword = findViewById(R.id.edit_password);
        mSbmBtn = findViewById(R.id.login_btn);

        mSbmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginProcess();
            }
        });

        mSigUpBtn = findViewById(R.id.sign_up_btn);
        mSigUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        CardView mLgBtn = findViewById(R.id.login_icon_btn);
        mLgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginProcess();
            }
        });
    }

    private void startLoginProcess() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Email is Empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Password is Empty", Toast.LENGTH_LONG).show();
            return;
        }

        mProgress.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mProgress.dismiss();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed to Sign In", Toast.LENGTH_LONG).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                });



    }
}
