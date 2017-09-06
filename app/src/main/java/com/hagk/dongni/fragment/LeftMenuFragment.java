package com.hagk.dongni.fragment;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hagk.dongni.activity.LoginActivity;
import com.hagk.dongni.activity.MainActivity;
import com.hagk.dongni.R;
import com.hagk.dongni.bean.UserInfo;
import com.hagk.dongni.bean.UserInfo.UserBean;
import com.hagk.dongni.http.UserObserver;
import com.hagk.dongni.http.UserService;
import com.hagk.dongni.lib.SlidingMenu;
import com.hagk.dongni.pager.ContactPager;
import com.hagk.dongni.pager.IndexPager;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.PrefUtils;
import com.hagk.dongni.view.CustomImageView;

/**
 * 侧边栏
 */
public class LeftMenuFragment extends BaseFragment implements
        View.OnClickListener {
    private RelativeLayout message, alert, phq9,
            course,contact,setting;// 中间的相对布局选项对象
    private CustomImageView picture;
    View view;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public View initViews() {
        view = View.inflate(mActivity, R.layout.fragment_left, null);
        message = (RelativeLayout) view.findViewById(R.id.rl_left_message);
        alert = (RelativeLayout) view.findViewById(R.id.rl_left_alert);
        phq9 = (RelativeLayout) view.findViewById(R.id.rl_left_phq9);
        course = (RelativeLayout) view.findViewById(R.id.rl_left_course);
        contact = (RelativeLayout) view.findViewById(R.id.rl_left_contact);
        setting = (RelativeLayout) view.findViewById(R.id.rl_left_setting);

        picture = (CustomImageView) view.findViewById(R.id.picture);

        // 设置监听事件
        message.setOnClickListener(this);
        alert.setOnClickListener(this);
        phq9.setOnClickListener(this);
        course.setOnClickListener(this);
        contact.setOnClickListener(this);
        setting.setOnClickListener(this);

        picture.setOnClickListener(this);

        broadcastReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConstantValue.LOGIN_ACTION);
        mActivity.registerReceiver(broadcastReceiver,filter);

        return view;
    }

    public class MyReceiver extends BroadcastReceiver {
        //自定义一个广播接收器
        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("pos",0);
            setCurrentMenuPager(pos);
            toggleSlidingMenu();
        }
        public MyReceiver(){
            System.out.println("MyReceiver");
            //构造函数，做一些初始化工作，本例中无任何作用
        }

    }

    // 获取用户的头像,描述信息,nickname等信息
    @Override
    public void initData() {
        // 获取网络请求接口
//		service = UserService.getUserService();
//		observer = service.getObserver();
//		getJsonResult();// 请求网络数据
    }

    /*private void getJsonResult() {
        String username = PrefUtils.getUsername(mActivity, "username");
        Observable<UserInfo> observable = observer.getResult(username);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.v("contactpager", "数据加载完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mActivity, "请检查您的网络", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        userBean = userInfo.getUserinfo();
                        Glide.with(mActivity).load(userBean.getUserimage())
                                // .override(dip2px(100), dip2px(70)) // 重新绘制图片大小
                                .override(dip2px(44), dip2px(44)) // 图片大小设置为这个像素会加载错误时的图片
                                .placeholder(R.mipmap.icon) // 默认图片和加载错误的图片
                                .error(R.mipmap.icon).into(icon);
                        System.out.println("nickname:" + userBean.getNickname());
                        nickname.setText(userBean.getNickname());
                        status.setText(userBean.getStatus());
                        describe.setText(userBean.getDescribe());
                    }
                });
    }
*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left_message:
                setMenuPager(0);
                break;
            case R.id.rl_left_alert:
                setMenuPager(1);
                break;
            case R.id.rl_left_phq9:
                setMenuPager(2);
                break;
            case R.id.rl_left_course:
                setMenuPager(3);
                break;
            case R.id.rl_left_contact:
                setMenuPager(4);
                break;
            case R.id.rl_left_setting:
                setMenuPager(5);
                break;
            case R.id.picture:
                setMenuPager(6);
                //个人信息pager
                break;
        }
    }

    /**
     * 处理munepager事件，显示指定的页面，关闭侧边栏
     */
    public void setMenuPager(int pos) {
        if(PrefUtils.getToken(mActivity) == null){
            Intent intent = new Intent(mActivity, LoginActivity.class);
            intent.putExtra("pos",pos);
            mActivity.startActivity(intent);
        }else{
            setCurrentMenuPager(pos);
            toggleSlidingMenu();
        }
    }

    /**
     * 设置当前菜单的页签详情页
     */
    private void setCurrentMenuPager(int pos) {
        MainActivity main = (MainActivity) mActivity;
        ContentFragment content = main.getContentFragment();

        ViewPager viewPager = content.getmViewPager();

        if (viewPager != null) {
            viewPager.setCurrentItem(0, false);
        }

        IndexPager pager = content.getIndexPager();
        pager.setCurrentMenuPager(pos);

    }

    /**
     * 关闭侧边栏
     */
    public void toggleSlidingMenu() {
        MainActivity main = (MainActivity) mActivity;
        SlidingMenu slidemenu = main.getSlidingMenu();
        slidemenu.toggle();// 切换状态，显示隐藏，隐藏显示
    }

}
