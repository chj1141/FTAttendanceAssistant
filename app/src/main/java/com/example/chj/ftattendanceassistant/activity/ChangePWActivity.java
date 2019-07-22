package com.example.chj.ftattendanceassistant.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chj.ftattendanceassistant.R;
import com.example.chj.ftattendanceassistant.network.NetWork;
import com.example.chj.ftattendanceassistant.network.ResultReturn;
import com.example.chj.ftattendanceassistant.utils.EncrypAES;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChangePWActivity extends AppCompatActivity {

    private EditText mEditTextOldPassword;
    private EditText mEditTextNewPassword;
    private EditText mEditTextConfirmPassword;

    private Button mButtonChangepw;
    private EncrypAES mAes;

    private final String TAG = "ChangePWActivity";

    private Observer<ResultReturn> mObserverChangepw = new Observer<ResultReturn>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            try {
                Log.w(TAG, "onError: "+e.toString());
            }catch (Exception ee){
                Log.w(TAG, "onError: " + ee.toString());
            }
        }

        @Override
        public void onNext(ResultReturn resultReturn) {
            Log.w(TAG, "onNext: ");
            Log.w(TAG, "onNext: "+resultReturn.getMsg());
            if (resultReturn.getMsgtype() == 0x51){
//                new SweetAlertDialog(getApplication(),SweetAlertDialog.SUCCESS_TYPE)
//                        .setConfirmText("确定")
//                        .setTitleText("密码修改成功！")
//                        .show();
                //finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepw);

        mEditTextOldPassword = (EditText) findViewById(R.id.edittext_old_password);
        mEditTextNewPassword = (EditText)findViewById(R.id.edittext_new_password);
        mEditTextConfirmPassword = (EditText)findViewById(R.id.edittext_confirm_password);
        mButtonChangepw = (Button)findViewById(R.id.button_changepw);
        mButtonChangepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changPassword();
                Log.w(TAG, "onClick: ");
            }
        });

        mAes=new EncrypAES();


    }

    private void changPassword(){

        mEditTextOldPassword.setError(null);
        mEditTextNewPassword.setError(null);
        mEditTextConfirmPassword.setError(null);

        String oldPassword = mEditTextOldPassword.getText().toString();
        String newPassword = mEditTextNewPassword.getText().toString();
        String confirmPassword = mEditTextConfirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(oldPassword)) {
            mEditTextOldPassword.setError("请输入密码！");
            focusView = mEditTextOldPassword;
            cancel = true;
        } else if (!isLengthValid(oldPassword)) {
            mEditTextOldPassword.setError("密码长度过短！");
            focusView = mEditTextOldPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(newPassword)) {
            mEditTextNewPassword.setError("请输入密码！");
            focusView = mEditTextNewPassword;
            cancel = true;
        } else if (!isLengthValid(newPassword)) {
            mEditTextNewPassword.setError("密码长度过短！");
            focusView = mEditTextNewPassword;
            cancel = true;
        }else if(isNewPasswordRepeat(oldPassword,newPassword)){
            mEditTextNewPassword.setError("新密码不能和原始密码一致！");
            focusView = mEditTextNewPassword;
            cancel = true;
        }


        if (TextUtils.isEmpty(confirmPassword)) {
            mEditTextConfirmPassword.setError("请输入密码！");
            focusView = mEditTextConfirmPassword;
            cancel = true;
        } else if (!isLengthValid(confirmPassword)) {
            mEditTextConfirmPassword.setError("密码长度过短！");
            focusView = mEditTextConfirmPassword;
            cancel = true;
        }else if(!isNewPasswordRepeat(newPassword,confirmPassword)){
            mEditTextConfirmPassword.setError("两次密码输入必须一致！");
            focusView = mEditTextConfirmPassword;
            cancel = true;
        }

        //若有错误将焦点移至错误处
        if (cancel) {

            //focusView.requestFocus();
        } else {
            String passwordNewAES = mAes.EncryptorString(newPassword);
            String passwordOldAES = mAes.EncryptorString(oldPassword);
            NetWork.getLoginApi()
                    .login("changepw","惊鸿一剑",passwordOldAES,passwordNewAES)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserverChangepw);
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

    private boolean isNewPasswordRepeat(String password,String newpassword) {
        //TODO: Replace this with your own logic
        return password.equals(newpassword);
    }

}
