package com.example.chj.ftattendanceassistant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chj.ftattendanceassistant.R;
import com.example.chj.ftattendanceassistant.network.NetWork;
import com.example.chj.ftattendanceassistant.network.ResultReturn;
import com.example.chj.ftattendanceassistant.utils.EncrypAES;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    private final String TAG = "LoginActivity";

    private EditText etLoginUsername;
    private EditText etLoginPassword;
    private EditText etRegister;
    private Button   btnLogin;
    private CheckBox checkBoxRememberPass;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private EncrypAES mAES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //getWindow().setBackgroundDrawableResource(R.drawable.background);
        setContentView(R.layout.activity_login);

        etRegister = (EditText)findViewById(R.id.edittext_login_register);
        etRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        etLoginUsername = (EditText)findViewById(R.id.edittext_login_userid);
        etLoginPassword = (EditText)findViewById(R.id.edittext_login_password);
        btnLogin = (Button)findViewById(R.id.button_login);
        btnLogin.setOnClickListener(this);
        checkBoxRememberPass = (CheckBox)findViewById(R.id.checkbox_remember_pass);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = mPreferences.getBoolean("remember_password",false);
        if (isRemember){
            //将账号密码设置到文本框中
            String username = mPreferences.getString("username","");
            String password = mPreferences.getString("password","");
            etLoginUsername.setText(username);
            etLoginPassword.setText(password);
            checkBoxRememberPass.setChecked(true);
        }

    }

    //建立观察者模式
    Observer<ResultReturn> observer = new Observer<ResultReturn>() {
        @Override
        public void onCompleted() {
            Log.w(TAG, "观察者：" + "onCompleted: ");
        }

        @Override
        public void onError(Throwable e) {
            dismissLoadingView();
            Toast.makeText(LoginActivity.this, "登陆失败，请确定网络连接", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "onError: "+e.toString());
        }

        @Override
        public void onNext(ResultReturn resultReturn) {
            dismissLoadingView();
            //登陆成功 打开主页面
            if(resultReturn.isSuccess()){
                Log.w(TAG, "onNext: "+"登陆成功");
                //记住密码
                mEditor = mPreferences.edit();
                if(checkBoxRememberPass.isChecked()){
                    mEditor.putBoolean("remember_password",true);
                    mEditor.putString("username",etLoginUsername.getText().toString());
                    mEditor.putString("password",etLoginPassword.getText().toString());

                }else{
                    mEditor.clear();
                }
                mEditor.apply();


                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                MainActivity.username = resultReturn.getResult().getUsername();
                MainActivity.fullname = resultReturn.getResult().getFullname();
                MainActivity.groupname = resultReturn.getResult().getGroupname();
                if (resultReturn.getResult().getPost().equals("主任")||
                        resultReturn.getResult().getPost().equals("助理员")||
                        resultReturn.getResult().getPost().equals("处长")){
                    MainActivity.adminFlag = 0;
                    MainActivity.department = resultReturn.getResult().getDepartment();
                }else{
                    MainActivity.adminFlag = 1;
                }

            }else if(!resultReturn.isSuccess()){
                Toast.makeText(LoginActivity.this, "密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login:
                excuteLogin();
                break;
            default:
                break;
        }
    }

    private void excuteLogin(){
        mAES = new EncrypAES();
        String username = etLoginUsername.getText().toString();
        String password = etLoginPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(username)){
            etLoginUsername.setError("用户名为空！");
            focusView = etLoginUsername;
            cancel = true;
        }else if (!isLengthValid(username)){
            etLoginUsername.setError("用户名过短");
            focusView = etLoginUsername;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("密码为空！");
            focusView = etLoginPassword;
            cancel = true;
        }else if (!isLengthValid(password)){
            etLoginPassword.setError("密码过短");
            focusView = etLoginPassword;
            cancel = true;
        }
        if (cancel){
            focusView.requestFocus();
        }else{
            //将密码加密后传入数据库中存储
            String passwordAES = mAES.EncryptorString(password);
            login(username, passwordAES);
            showLoadingView();
        }




    }

    private void login(String username, String password){
        mSubscription = NetWork.getLoginApi()
                .login("login",username, password,null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    private boolean isLengthValid(String temp) {
        //TODO: Replace this with your own logic
        return temp.length() >= 4;
    }
}
