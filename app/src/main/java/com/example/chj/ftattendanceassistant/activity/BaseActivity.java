package com.example.chj.ftattendanceassistant.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chj.ftattendanceassistant.R;

import rx.Subscription;


/**
 * Created by chenghj on 2018/8/12.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Subscription mSubscription;

    //得到一个Subscription对象之后,在Activity销毁的时候去取消订阅以防内存泄漏:
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    //解绑图像，防止内存泄漏
    protected void unbindDrawables(View view) {
        if (view != null) {
            //如果为背景图像
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            //如果为照片图像
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(null);
            }
            //如果为ViewGroup并且不是AdapterView
            if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                if (!(view instanceof AbsSpinner) && !(view instanceof AbsListView)) {
                    ((ViewGroup) view).removeAllViews();
                }
            }
        }
    }

    protected View inflateSubView(int subId, int inflateId) {
        View noNetSubTree = findViewById(inflateId);
        //说明是第一次
        if (noNetSubTree == null) {
            //ViewStub是一个轻量级的View，占用资源少
            ViewStub viewStub = (ViewStub) findViewById(subId);
            noNetSubTree = viewStub.inflate();
        }
        noNetSubTree.setVisibility(View.VISIBLE);
        return noNetSubTree;
    }


    //显示正在加载中
    protected void showLoadingView() {
        View view = inflateSubView(R.id.activity_loading_stub,
                R.id.activity_loading_subTree);
        if (view != null) {
            LoadingView loadingView = (LoadingView) view.findViewById(R.id.loading_view);
            if (loadingView != null) {
                loadingView.showLoading(true, R.string.loading_busy, 0);
            }
        }
    }


    //隐藏loadingview
    protected void dismissLoadingView() {
        View view = findViewById(R.id.activity_loading_subTree);
        if (view != null) {
            LoadingView loadingView = (LoadingView) view.findViewById(R.id.loading_view);
            if (loadingView != null) {
                loadingView.hidenLoading();
            }
        }
    }


    /**
     * 网络错误时显示
     * @param id
     */
    protected void showNetErroView(int id) {
        View view = inflateSubView(R.id.activity_net_error_stub,
                R.id.activity_net_error_subTree);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            //注意这里是fragment_net_error_subTree
            //add By SuS
            View refresh = view.findViewById(R.id.activity_net_error_subTree);
            TextView txtView = (TextView) view.findViewById(R.id.error_saying_bg_textview);
            if (txtView != null) {
                txtView.setText(id);
            }
            if (refresh != null) {
                //点击进行刷新
                refresh.setOnClickListener(this);
            }
        }
    }

    protected void dismissNetErroView() {
        View view = findViewById(R.id.activity_net_error_subTree);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 内容为空的时候显示
     */
    protected void showContentEmptyView() {
        View view = inflateSubView(R.id.activity_empty_stub,
                R.id.activity_empty_subTree);
        view.setVisibility(View.VISIBLE);
    }

    protected void dismissContentEmptyView() {
        View view = findViewById(R.id.activity_empty_subTree);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }





}
