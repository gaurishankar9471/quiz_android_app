package gaurishankar.com.hifisoftlab.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button mPlayQuizBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mRootRef;
    private TextView mUserName;
    private Button mLogout;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseFirestore.getInstance();

        mUserName = findViewById(R.id.user_name_txt);
        mLogout = findViewById(R.id.logout_btn);

        if (mAuth.getCurrentUser()==null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setCancelable(false);
        mProgress.show();



        mRootRef.document("user_database/"+mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String user_name = document.getString("user_name");
                        mUserName.setText("Welcome, "+user_name);
                        mProgress.dismiss();

                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });


        mPlayQuizBtn = findViewById(R.id.play_quiz_btn);
        mPlayQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser()!=null){
                    Intent intent = new Intent(getApplicationContext(), SelectCategoryActivity.class);
                    startActivity(intent);
                }

            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(getIntent());finish();
                startActivity(getIntent());

            }
        });



    }
}
