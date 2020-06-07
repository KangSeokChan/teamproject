package com.example.madforumapp.Tutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madforumapp.MainFeed;
import com.example.madforumapp.R;
import com.example.madforumapp.act2;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    /*tutorial page 수 = 3*/
    final int numOfPages = 3;

    private ViewPager mMainPage;
    private LinearLayout mDotContainer;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;

    private Button mNext;
    private Button mBack;
    private  Button mSignIn;
    private int mCurrentSliderPage=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent mainFeed=new Intent(MainActivity.this, MainFeed.class);
            mainFeed.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mainFeed.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainFeed);
        }

        mMainPage = findViewById(R.id.mainViewPager);
        mDotContainer = findViewById(R.id.sliderDots);
        mNext = findViewById(R.id.next);
        mBack = findViewById(R.id.back);
        mSignIn = findViewById(R.id.signin);

        sliderAdapter = new SliderAdapter(this);
        mMainPage.setAdapter(sliderAdapter);
        // adding the slider dots
        addDots(0);
        // setting the event listener for the page slider
        mMainPage.addOnPageChangeListener(viewListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // setting the event listeners for the next and back buttons
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mCurrentSliderPage+1 < numOfPages)
                    mMainPage.setCurrentItem(mCurrentSliderPage+1);

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mCurrentSliderPage-1 >= 0)
                    mMainPage.setCurrentItem(mCurrentSliderPage-1);

            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent=new Intent(MainActivity.this, act2.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); 기존에 쌓인 stack 모두 제거*/
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                /*intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 새로 task 생성. intent가 새로 생성한 activity가 이때의 root activity.*/
                startActivity(loginIntent);

                /*Activity Stack 처리하기*/
                //Activity를 그냥 intent생성해서 만들면 계속해서 새로 Activity를 start하고 스택으로 쌓이는 문제가 발생한다.
                //예를 들어서 A, B, C Activity를 A->B->C->A->C->B로 이동하고 뒤로가기를 누르면 다시 화면이 B->C->A->C->B->A 순으로 왔던 순서 반대로 나타난다.
                //그래서 뜨면 안될 화면도 뜨게 된다. (*회원가입 화면이 그대로 다시뜬다던가 삭제한 화면이 뜬다던가 등) 혹은 그냥 다시 뒤로가면 되는 화면인데 새로운 화면을 만들어서 그 위에 쌓아나간다.
                /*여기서 생기는 두가지 문제,
                1. 접근해선 안되는 이전화면으로 돌아가버린다.
                2. 굳이 이전 화면의 Activity를 새로 생성해서 위에 쌓는다. 를 해결해보자.
                1. 접근해선 안되는 이전화면으로 돌아가버리는 문제
                        ->해당 Activity 의 JAVA 소스 파일에서 intent를 생성한 바로 다음에
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                를 추가해준다.
                여기서 FLAG_ACTIIVTY_CLEAR_TASK는 기존에 쌓여있던 task(stack이 모여 형성하는 작업의 단위(?))를 모두 삭제하는 조건(?)을 받는 flag 상수다. Intent 클래스에 statitc final로 선언되어있다.
                즉 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); 는 기존에 쌓여있던 스택을 모두 없앤다.
                이때 FLAG_ACTIVITY_NEW_TASK로 task를 새로 생성한다. 이때 root activity는 intent가 새로 생성한 activity가 된다.

                2. 이전 화면의 Activity로 돌아가야하는 문제
                        -> 돌아가기 이전 Activity의 JAVA 소스 파일에서 intent를 생성한뒤에
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                을 추가해준다.
                FLAG_ACTIVITY_CLEAR_TOP 은 이전 Activity를 새로운 Activity로 지정하고 현재 Activity는 Clear하는 flag상수다. 역시 Intent 클래스에 static final로 선언되어 있다.
                따라서, 현재 Activity를 없애고 이전 화면을 새로운 화면으로 지정하기 때문에 자연스럽게 이전화면으로 돌아간다.

                ** 2.번 문제는 Manifest파일에 android:launchmode="singleTop"을 추가해 주면 비슷하게 문제를 해결할 수 있다.
                자세한건
                http://developer.android.com/guide/components/tasks-and-back-stack.html
                (tasks and back stack에 대한 android document)
                참조.*/
            }
        });

    }

    // adding the dots
    public void addDots(int position){
        mDots = new TextView[3];
        mDotContainer.removeAllViews();

        for (int i=0; i<numOfPages; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;") );
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(Color.WHITE);

            mDotContainer.addView(mDots[i]);
        }

        if(position>=0){
            mDots[position].setTextColor(Color.LTGRAY);
        }
    }

    // adding a listener for slider
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            mCurrentSliderPage=position;

            if(mCurrentSliderPage == 0){
                mNext.setVisibility(View.VISIBLE);
                mNext.setEnabled(true);

                mBack.setVisibility(View.INVISIBLE);
                mBack.setEnabled(false);
            }
            else{
                mBack.setVisibility(View.VISIBLE);
                mBack.setEnabled(true);
            }

            if(mCurrentSliderPage==numOfPages-1){
                mNext.setVisibility(View.INVISIBLE);
                mNext.setEnabled(false);

                mBack.setVisibility(View.VISIBLE);
                mBack.setEnabled(true);

                mSignIn.setVisibility(View.VISIBLE);
                mSignIn.setEnabled(true);
            }
            else{
                mNext.setVisibility(View.VISIBLE);
                mNext.setEnabled(true);

                mSignIn.setVisibility(View.INVISIBLE);
                mSignIn.setEnabled(false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

    private long backKeyPressedTime = 0;
    private Toast toast;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }
}
