package com.example.madforumapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    private TextView registersignin;

    private Button register;

    private CheckBox check;

    private EditText name,email,password,passwordcheck,phone;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser();

                if(validate() && registerUser())
                {
                    //input data to database
                    final String user_name= name.getText().toString().trim();
                    final String user_email=email.getText().toString().trim();
                    String user_password=password.getText().toString().trim();
                    final String user_phone;
                    user_phone = phone.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                user User=new user(

                                        user_name,
                                        user_email,
                                        user_phone



                                );
                                FirebaseDatabase.getInstance().getReference("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(User);

                                sendEmail();

                               // Toast.makeText(signup.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(signup.this,MainFeed.class));


                            }else
                            {
                                Toast.makeText(signup.this,"Registration failed",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });


                }



            }
        });

    }

    private void setupUIViews(){

        registersignin=findViewById(R.id.regsignin);

        check=findViewById(R.id.checkBox);

        name=findViewById(R.id.nametxt);
        email=findViewById(R.id.emailtxt);
        password=findViewById(R.id.passwordtxt);
        passwordcheck=findViewById(R.id.passwordchecktxt);
        phone=findViewById(R.id.etphone);

        register=findViewById(R.id.signupbtn);
    }



    @Override
    protected void onResume() {
        super.onResume();





        registersignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(signup.this,act2.class);

                startActivity(i);
            }
        });

    }

    private Boolean validate()
    {
        Boolean result=false;

        String nam=name.getText().toString();
        String ema=email.getText().toString();
        String pass=password.getText().toString();
        String Cpass=passwordcheck.getText().toString();
        String phon=phone.getText().toString();

        if(nam.isEmpty() || ema.isEmpty() || pass.isEmpty() || Cpass.isEmpty() || phon.isEmpty())
        {
            Context context=getApplicationContext();
            LayoutInflater inflater=getLayoutInflater();
            View customToastroot=inflater.inflate(R.layout.emptyfield_toast,null);
            Toast customToast=new Toast(context);

            customToast.setView(customToastroot);
            customToast.setDuration(Toast.LENGTH_LONG);
            customToast.show();

        }
        else if (!pass.equals(Cpass)){
           passwordcheck.setError("비밀번호가 일치하지 않습니다.");
           passwordcheck.requestFocus();
           return false;
        }
        else{

            result=true;
        }

        return result;
    }

    private boolean registerUser()
    {
        Boolean result=false;

        String nam=name.getText().toString();
        String ema=email.getText().toString();
        String pass=password.getText().toString();
        String phon=phone.getText().toString();

        if(pass.length() > 0 && pass.length() < 6)
        {
            password.setError("비밀번호 길이는 최소 6자 이상입니다.");
            password.requestFocus();
            return false;
        }

        if(phon.length() > 0 && phon.length() < 10)
        {
            phone.setError("유효한 연락처를 입력하세요");
            phone.requestFocus();
            return false;
        }
        if(check.isChecked()==false)
        {
            check.setError("약관에 동의해주세요");
            check.requestFocus();
            return false;
        }else{

            result=true;
        }

        return result;
    }

    private void sendEmail()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful())
                    {
                        Toast.makeText(signup.this,"회원가입 성공! 이메일을 확인해주세요. ",Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(signup.this,act2.class));
                    }else{
                        Toast.makeText(signup.this,"가입 확인 이메일이 전송되지 않았습니다.ㅜ",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
