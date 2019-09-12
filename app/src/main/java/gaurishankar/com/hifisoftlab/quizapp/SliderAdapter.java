package gaurishankar.com.hifisoftlab.quizapp;

/**
 * Created by Gauri Shankar on 9/4/2019.
 */
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SliderAdapter extends PagerAdapter {
    private Context context;
    private List<String> img_url;
    private List<String> offer_id;
    private List<String> option1_data;
    private List<String> option2_data;
    private List<String> option3_data;
    private List<String> option4_data;
    private List<String> ans;
    private FirebaseFirestore mRootRef;
    private FirebaseAuth mAuth;

    public SliderAdapter(Context context, List<String> img_url, List<String> offer_id, List<String> option1_data, List<String> option2_data, List<String> option3_data, List<String> option4_data, ArrayList<String> ans) {
        this.context = context;
        this.img_url = img_url;
        this.offer_id = offer_id;
        this.option1_data = option1_data;
        this.option2_data = option2_data;
        this.option3_data = option3_data;
        this.option4_data = option4_data;
        this.ans=ans;
    }

    @Override
    public int getCount() {
        return img_url.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.slider_layout, null);

        mRootRef = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

// Update one field, creating the document if it does not already exist.
        Map<String, Object> data = new HashMap<>();
        data.put("score", 0);

        mRootRef.collection("user_database").document(mAuth.getCurrentUser().getUid())
                .set(data, SetOptions.merge());

        TextView qs = view.findViewById(R.id.question_txt);
        TextView option1 = view.findViewById(R.id.option1_txt);
        TextView option2 = view.findViewById(R.id.option2_txt);
        TextView option3 = view.findViewById(R.id.option3_txt);
        TextView option4 = view.findViewById(R.id.option4_txt);

        qs.setText(img_url.get(position));

        option1.setText(option1_data.get(position));
        option2.setText(option2_data.get(position));
        option3.setText(option3_data.get(position));
        option4.setText(option4_data.get(position));

        final CardView mOption1 = view.findViewById(R.id.option1);
        final CardView mOption2 = view.findViewById(R.id.option2);
        final CardView mOption3 = view.findViewById(R.id.option3);
        final CardView mOption4 = view.findViewById(R.id.option4);

        final int[] score = {0};

        final int[] ans_curr = {0};

        mOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllOption(view ,mOption1, mOption2, mOption3, mOption4);
                mOption1.setCardBackgroundColor(view.getResources().getColor(R.color.colorPrimary));
                ans_curr[0] =1;

            }
        });
        mOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllOption(view ,mOption1, mOption2, mOption3, mOption4);
                mOption2.setCardBackgroundColor(view.getResources().getColor(R.color.colorPrimary));
                ans_curr[0] =2;

            }
        });
        mOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllOption(view ,mOption1, mOption2, mOption3, mOption4);
                mOption3.setCardBackgroundColor(view.getResources().getColor(R.color.colorPrimary));
                ans_curr[0] =3;

            }
        });
        mOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllOption(view ,mOption1, mOption2, mOption3, mOption4);
                mOption4.setCardBackgroundColor(view.getResources().getColor(R.color.colorPrimary));
                ans_curr[0] =4;
            }
        });


        final Button mSbmBtn = view.findViewById(R.id.sbm_btn);
        mSbmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, FinalScoreActivity.class);
////                intent.putExtra("score",String.valueOf(score));
////                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                context.startActivity(intent);
                    mSbmBtn.setText("Submitted");
                    mSbmBtn.setEnabled(false);
                    mOption1.setEnabled(false);
                    mOption2.setEnabled(false);
                    mOption3.setEnabled(false);
                    mOption4.setEnabled(false);


                if (ans_curr[0]==Integer.valueOf(ans.get(position))) {

                    mRootRef.document("user_database/" + mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    long score = document.getLong("score");
                                    score = score + 1;

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("score", score);

                                    mRootRef.collection("user_database").document(mAuth.getCurrentUser().getUid())
                                            .set(data, SetOptions.merge());
                                } else {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });


        final Button mFinishBtn = view.findViewById(R.id.finish_btn);
        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FinalScoreActivity.class);
                intent.putExtra("score",String.valueOf(score[0]));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            }
        });


//        final ImageView img_slide = view.findViewById(R.id.slider_img);
//
//
//        final ProgressBar mProgress = view.findViewById(R.id.progressBarSlider);
//        if (!img_url.get(position).isEmpty()) {
//
//        }
//        if (!offer_id.get(position).isEmpty()){
//
//        }

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);





        return view;
    }
    private void resetAllOption(View view, CardView mOption1, CardView mOption2, CardView mOption3, CardView mOption4) {
        mOption1.setCardBackgroundColor(view.getResources().getColor(R.color.colorOption));
        mOption2.setCardBackgroundColor(view.getResources().getColor(R.color.colorOption));
        mOption3.setCardBackgroundColor(view.getResources().getColor(R.color.colorOption));
        mOption4.setCardBackgroundColor(view.getResources().getColor(R.color.colorOption));

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}