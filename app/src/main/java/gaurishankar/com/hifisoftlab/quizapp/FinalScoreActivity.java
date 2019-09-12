package gaurishankar.com.hifisoftlab.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class FinalScoreActivity extends AppCompatActivity {

    private TextView mScoreTxt;
    private FirebaseFirestore mRootRef;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);

        mRootRef = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mProgess = new ProgressDialog(this);
        mProgess.setMessage("Loading...");
        mProgess.setCanceledOnTouchOutside(false);
        mProgess.show();

        mScoreTxt = findViewById(R.id.score_txt);


        mRootRef.document("user_database/"+mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        long score = document.getLong("score");
                        mScoreTxt.setText(String.valueOf(score));
                        mProgess.dismiss();


                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button mHomeBtn = findViewById(R.id.back_to_home);
        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}
