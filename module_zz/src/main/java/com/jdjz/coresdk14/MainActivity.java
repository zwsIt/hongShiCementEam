package com.jdjz.coresdk14;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bluetron.rxretrohttp.exception.ApiException;
import com.bluetron.zhizhi.BuildConstants;
import com.bluetron.zhizhi.domain.bean.response.workstation.OwnMinAppItem;
import com.bluetron.zhizhi.domain.event.login.LogOutEventSDK;
import com.bluetron.zhizhi.domain.event.login.LoginEventSDK;
import com.bluetron.zhizhi.domain.event.minapp.MinappListEvent;
import com.bluetron.zhizhi.domain.router.Navigation;
import com.bluetron.zhizhi.home.presentation.HomeSDK;
import com.bluetron.zhizhi.login.presentation.LoginUserActivity;
import com.bluetron.zhizhi.login.presentation.LoginUserSDK;
import com.bluetron.zhizhi.setting.presentation.LogoutUserSDK;
import com.jdjz.common.event.OpenMinappEvent;
import com.jude.utils.JUtils;
import com.supcon.mes.module_zz.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnLogout, btnMinapplist, btnLct, btnGcbj, btnQst, btnScbb;
    EditText tvUrl, tvName, tvPwd;
    EditText tvMinapplist;
    List<OwnMinAppItem> minappList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initView();
        initClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        btnLogout = findViewById(R.id.btn_logout);
        tvUrl = findViewById(R.id.tv_url);
        tvName = findViewById(R.id.tv_name);
        tvPwd = findViewById(R.id.tv_pwd);
        btnMinapplist = findViewById(R.id.btn_minapplist);
        tvMinapplist = findViewById(R.id.tv_minapplist);
        btnLct = findViewById(R.id.btn_lct);
        btnGcbj = findViewById(R.id.btn_gcbj);
        btnQst = findViewById(R.id.btn_qst);
        btnScbb = findViewById(R.id.btn_scbb);


    }

    private void initClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tchl", "login");
                LoginUserSDK.getInstance().userLogin(tvName.getText().toString(), tvPwd.getText().toString(), tvUrl.getText().toString());
                //Intent intent = new Intent(getApplication(), LoginUserActivity.class);
                //startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.class,"splash",Toast.LENGTH_SHORT).show();
                Log.i("tchl", "logout");
                LogoutUserSDK.getInstance().userLogout();
                //Intent intent = new Intent(getApplication(), LoginUserActivity.class);
                //startActivity(intent);
            }
        });

        btnMinapplist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("tchl", "getMinappList");
                tvMinapplist.setText("");
                HomeSDK.getInstance().getMinppListSDK();
            }
        });

        //流程图
        btnLct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<minappList.size();i++){
                    if(minappList.get(i).getApptype() == 1){
                        if(minappList.get(i).getAppname().equals("流程图")){
                            Navigation.navigateToTFAppList(minappList.get(i).getAppname());

                        }
                    }
                }
            }
        });

        //过程报警
        btnGcbj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<minappList.size();i++){
                    if(minappList.get(i).getApptype() == 2){
                        if (minappList.get(i).getAppname().equals("过程报警")){
                            //BuildConstants.isInit = true;
                            //BuildConstants.appId = minappList.get(i).getId();
                            //Navigation.navigateToH5App(minappList.get(i).getAppurl(), minappList.get(i).getAppname(),minappList.get(i).getId());

                            BuildConstants.isInit = true;
                            BuildConstants.appId = minappList.get(i).getAppId();
                            Navigation.navigateToAlarmMinapp(minappList.get(i).getAppurl()
                                    , minappList.get(i).getAppname(), minappList.get(i).getAppserverbaseurl()
                                    , minappList.get(i).getAppiconurl());
                        }
                    }
                }
            }
        });
        //趋势图
        btnQst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<minappList.size();i++){
                    if(minappList.get(i).getApptype() == 2){
                        if(minappList.get(i).getAppname().equals("趋势图")){
                           // Navigation.navigateToH5App(minappList.get(i).getAppurl(), minappList.get(i).getAppname(),minappList.get(i).getId());

                            Navigation.navigateToTrendMinapp(minappList.get(i).getAppurl()
                                    , minappList.get(i).getAppname(), minappList.get(i).getAppserverbaseurl()
                                    , minappList.get(i).getAppiconurl());
                        }
                    }
                }
            }
        });


        //生产报表
        btnScbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<minappList.size();i++){
                    if(minappList.get(i).getApptype() == 1){
                        if(minappList.get(i).getAppname().equals("生产报表")){
                            //Navigation.navigateToTFApp(minappList.get(i).getAppname());
                            Navigation.navigateToTFAppList(minappList.get(i).getAppname());
                        }
                    }
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(OpenMinappEvent event) {
        Navigation.navigateToTrendMinapp(event.getAppUrl()
                , event.getAppName(),event.getAppServerBaseUrl()
                , event.getAppIconUrl());
        /*Intent intent10 = new Intent(this, sonBaseMinAppWebViewJS3.class);
        intent10.putExtra("url",event.getUrl() );

        startActivity(intent10);*/
    }

    //登录,登出,获取minappist时出错返回的内容和code
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(ApiException ex) {
        JUtils.Toast(ex.getCode() + " " + ex.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LoginEventSDK ex) {
        JUtils.Toast("login Success");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LogOutEventSDK ex) {
        JUtils.Toast("logout Success");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MinappListEvent minappListEvent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < minappListEvent.getList().size(); i++) {
            JUtils.Log("@@" + minappListEvent.getList().get(i).getAppname() + " " + minappListEvent.getList().get(i).getAppname());
            sb.append(minappListEvent.getList().get(i).getAppname() + " " + minappListEvent.getList().get(i).getApptype() + " \n");
        }
        minappList = minappListEvent.getList();
        tvMinapplist.setText(sb.toString());
        //JUtils.Toast("minapp list size:"+minappListEvent.getList().size());
    }
}
