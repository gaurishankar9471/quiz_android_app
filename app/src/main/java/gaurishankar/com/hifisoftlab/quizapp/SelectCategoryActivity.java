package gaurishankar.com.hifisoftlab.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectCategoryActivity extends AppCompatActivity {

    private CardView mSocialScienceBtn, mScienceBtn, mGkBtn, mEnglishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        mSocialScienceBtn = findViewById(R.id.social_science_btn);
        mScienceBtn = findViewById(R.id.science_btn);
        mGkBtn = findViewById(R.id.gk_btn);
        mEnglishBtn = findViewById(R.id.english_btn);

        mSocialScienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.putExtra("cat","social_science");
                startActivity(intent);
            }
        });
        mScienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.putExtra("cat","science");
                startActivity(intent);
            }
        });
        mGkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.putExtra("cat","gk");
                startActivity(intent);
            }
        });
        mEnglishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.putExtra("cat","english");
                startActivity(intent);
            }
        });


    }
}
