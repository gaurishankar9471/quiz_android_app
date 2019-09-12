package gaurishankar.com.hifisoftlab.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    private CardView mOption1, mOption2, mOption3, mOption4;
    private Button mSbmBtn;

    private String cat;
    private ViewPager sliderViewPager;
    private TabLayout indicator;
    private ArrayList<String> img_url;
    private ArrayList<String> offer_id;



    private ArrayList<String> question;
    private ArrayList<String> option1;
    private ArrayList<String> option2;
    private ArrayList<String> option3;
    private ArrayList<String> option4;
    private ArrayList<String> ans;

    private ProgressDialog mProgress;


    private FirebaseFirestore mRootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        if (getIntent()!=null){
            cat = getIntent().getStringExtra("cat");
        }

        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setMessage("Loading...");
        mProgress.show();

        mRootRef = FirebaseFirestore.getInstance();

//        mOption1 = findViewById(R.id.option1);
//        mOption2 = findViewById(R.id.option2);
//        mOption3 = findViewById(R.id.option3);
//        mOption4 = findViewById(R.id.option4);

//        mSbmBtn = findViewById(R.id.sbm_btn);
//        mSbmBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                submitRequest();
//            }
//        });

        question = new ArrayList<>();
        option1 = new ArrayList<>();
        option2 = new ArrayList<>();
        option3 = new ArrayList<>();
        option4 = new ArrayList<>();
        ans = new ArrayList<>();



        sliderViewPager = findViewById(R.id.slider_home_viewPager);

        final SliderAdapter sliderAdapter = new SliderAdapter(getApplicationContext(),question,offer_id, option1 ,option2 ,option3, option4, ans);
        sliderViewPager.setAdapter(sliderAdapter);

        mRootRef.collection("question_list").whereEqualTo("cat",cat)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                question.add(document.getString("question"));
                                option1.add(document.getString("option1"));
                                option2.add(document.getString("option2"));
                                option3.add(document.getString("option3"));
                                option4.add(document.getString("option4"));
                                ans.add(document.getString("ans"));

                                sliderAdapter.notifyDataSetChanged();

                                mProgress.dismiss();

//                                Log.d("D_DATA", document.getId() + " => " + document.getData());

                            }
                        } else {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }

    private void submitRequest() {

    }
}
