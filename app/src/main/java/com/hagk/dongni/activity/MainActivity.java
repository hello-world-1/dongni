package com.hagk.dongni.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;

import com.hagk.dongni.R;
import com.hagk.dongni.fragment.ContentFragment;
import com.hagk.dongni.fragment.LeftMenuFragment;
import com.hagk.dongni.lib.SlidingMenu;
import com.hagk.dongni.utils.ConstantValue;

public class MainActivity extends SlidingFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_menu);// 设置侧边栏
		SlidingMenu slidingMenu = getSlidingMenu();// 获取侧边栏对象
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸
		slidingMenu.setBehindOffset(ConstantValue.LEFT_MENU_WIDTH);// 设置预留屏幕的宽度
		initFragment();
	}

	/**
	 * 初始化fragment, 将fragment数据填充给布局文件
	 */
	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();// 开启事务

		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				ConstantValue.FRAGMENT_LEFT_MENU);// 用fragment替换framelayout
		transaction.replace(R.id.ly_content, new ContentFragment(),
				ConstantValue.FRAGMENT_CONTENT);
		transaction.commit();// 提交事务
	}

	/**
	 * 获取contentfragment
	 */
	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm
				.findFragmentByTag(ConstantValue.FRAGMENT_LEFT_MENU);
		return leftMenuFragment;
	}

	/**
	 * 获取contentfragment
	 */
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(ConstantValue.FRAGMENT_CONTENT);
		return contentFragment;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
		}
		return false;
	}

	/**
	 * 当按返回键时的提示对话框,采用默认样式
	 */
	protected void dialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("确认退出吗？");

		builder.setTitle("提示");

		builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				MainActivity.this.finish();
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}
}

