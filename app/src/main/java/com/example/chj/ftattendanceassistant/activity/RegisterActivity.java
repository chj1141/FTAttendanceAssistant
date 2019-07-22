package com.example.chj.ftattendanceassistant.activity;

import android.content.Intent;
import android.icu.lang.UCharacterEnums;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chj.ftattendanceassistant.R;
import com.example.chj.ftattendanceassistant.network.NetWork;
import com.example.chj.ftattendanceassistant.network.ResultReturn;
import com.example.chj.ftattendanceassistant.utils.EncrypAES;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {

    private final String TAG = "RegisterAcivity";

    private EditText etRegUsername;
    private EditText etRegPassword;
    private EditText etRegPasswordRepeat;
    private EditText etFullname;
    private EditText etPhonenumber;

    private Button   btnSubmit;

    private EncrypAES mAes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        etRegUsername = (EditText)findViewById(R.id.edittext_register_userid);
        etRegPassword = (EditText)findViewById(R.id.edittext_register_password);
        etRegPasswordRepeat = (EditText)findViewById(R.id.edittext_register_password_re);
        etFullname = (EditText)findViewById(R.id.edittext_register_realname);
        etPhonenumber = (EditText)findViewById(R.id.edittext_register_phonenum);

        //初始化
        etRegUsername.setError(null);
        etRegPassword.setError(null);
        etRegPasswordRepeat.setError(null);
        etFullname.setError(null);
        etPhonenumber.setError(null);

        btnSubmit = (Button)findViewById(R.id.button_register_submit);
        btnSubmit.setOnClickListener(this);
    }

    //建立观测者模型
    Observer<ResultReturn> mObserver = new Observer<ResultReturn>() {
        @Override
        public void onCompleted() {
            Log.w(TAG, "onCompleted: ");
            //Toast.makeText(RegisterActivity.this,TAG + "onCompleted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Throwable e) {
            dismissLoadingView();
            Toast.makeText(RegisterActivity.this, "出现未知错误，请重试！" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.w(TAG, "onError: " + e.toString());
        }

        @Override
        public void onNext(ResultReturn resultReturn) {
            Log.w(TAG, "onNext: "+resultReturn.getMsgtype());
            dismissLoadingView();
            if (resultReturn.isSuccess()) {
                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }else{
                if(resultReturn.getMsgtype()==0xF1){
                    Toast.makeText(RegisterActivity.this, "用户名已存在，请重新输入！", Toast.LENGTH_SHORT).show();
                }else if(resultReturn.getMsgtype()==0xF2){
                    Toast.makeText(RegisterActivity.this, "真实姓名或者手机号不正确，请确认！", Toast.LENGTH_SHORT).show();
                }else if(resultReturn.getMsgtype()==0xF3){
                    Toast.makeText(RegisterActivity.this, "真实姓名或者手机号不正确，请确认！", Toast.LENGTH_SHORT).show();
                }else if(resultReturn.getMsgtype()==0xF4){
                    Toast.makeText(RegisterActivity.this, "出现未知错误，请重试！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_register_submit:
                String username = etRegUsername.getText().toString();
                String password = etRegPassword.getText().toString();
                String passwordRe = etRegPasswordRepeat.getText().toString();
                String fullname = etFullname.getText().toString();
                String phonenumber = etPhonenumber.getText().toString();

                mAes = new EncrypAES();

                Log.d(TAG, "onClick: ");

                boolean cancel = false;
                View focusView = null;

                if(TextUtils.isEmpty(username)){
                    etRegUsername.setError("用户名为空！");
                    focusView = etRegUsername;
                    cancel = true;
                }else if (!isLengthValid(username)){
                    etRegUsername.setError("用户名过短");
                    focusView = etRegUsername;
                    cancel = true;
                }

                if (TextUtils.isEmpty(password)){
                    etRegPassword.setError("密码为空！");
                    focusView = etRegPassword;
                    cancel = true;
                }else if (!isLengthValid(password)){
                    etRegPassword.setError("密码过短");
                    focusView = etRegPassword;
                    cancel = true;
                }

                if (!TextUtils.isEmpty(password)&&!isRePasswordCrect(password, passwordRe)){
                    etRegPasswordRepeat.setError("两次密码输入不一致！");
                    focusView = etRegPasswordRepeat;
                    cancel = true;
                }

                if (TextUtils.isEmpty(fullname)){
                    etFullname.setError("真实姓名为空！");
                    focusView = etFullname;
                    cancel = true;
                }

                if (TextUtils.isEmpty(phonenumber)){
                    etPhonenumber.setError("公司注册手机号码为空！");
                    focusView = etPhonenumber;
                    cancel = true;
                }


                if (cancel){
                    focusView.requestFocus();
                }else{
                    //将密码加密后传入数据库中存储
                    String passwordAES = mAes.EncryptorString(password);
                    RegisterUser(username, passwordAES, fullname, phonenumber);
                }
                break;
        }
    }

    private boolean isLengthValid(String temp) {
        //TODO: Replace this with your own logic
        return temp.length() >= 4;
    }

    private boolean isRePasswordCrect(String password,String repassword) {
        //TODO: Replace this with your own logic
        return password.equals(repassword);
    }

    private void RegisterUser(String username, String password, String fullname, String phonenumber){
        showLoadingView();
        mSubscription = NetWork.getRegisiterApi()
                .register(username, password, fullname, phonenumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }
}
