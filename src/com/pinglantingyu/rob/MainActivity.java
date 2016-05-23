package com.pinglantingyu.rob;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.pinglantingyu.rob.UI.RoundImageView;
import com.pinglantingyu.rob.UI.XiuYiXiuView;

public class MainActivity extends Activity {

	private XiuYiXiuView mXiuView = null;
	private RoundImageView mPhoto = null;
	private TextView mTextView = null;
	private final static String TAG = "RobMoneyMain";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mXiuView.stop();	
		mXiuView.clearAnimation();//为省电，后台执行不显示动画
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isAccessibilitySettingsOn(this)) {
			mXiuView.start();		//若service开启，则执行动画
			mTextView.setText(getResources().getString(R.string.Finding));
		}else{
			mXiuView.stop();	
			mXiuView.clearAnimation();
			mTextView.setText(getResources().getString(R.string.Push));
		}

	}

	// View组件初始化
	private void initView() {
		mXiuView = (XiuYiXiuView) findViewById(R.id.wv);
		mPhoto = (RoundImageView) findViewById(R.id.my_photo);
		mTextView = (TextView)findViewById(R.id.tv1);
		mPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// showXiuAnim();
				openServiceSetting();
				isAccessibilitySettingsOn(MainActivity.this);
			}
		});

	}


	// 打开辅助功能
	private void openServiceSetting() {
		try {
			Intent intent = new Intent(
					android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
			startActivity(intent);
			Toast.makeText(this, "找到金手指服务，开启即可", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 判断Service是否在运行
	private boolean isAccessibilitySettingsOn(Context mContext) {
		int accessibilityEnabled = 0;
		final String service = "com.pinglantingyu.rob/com.pinglantingyu.rob.RobMoney";
		boolean accessibilityFound = false;
		try {
			accessibilityEnabled = Settings.Secure.getInt(mContext
					.getApplicationContext().getContentResolver(),
					android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
			Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
		} catch (SettingNotFoundException e) {
			Log.e(TAG,
					"Error finding setting, default accessibility to not found: "
							+ e.getMessage());
		}
		TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(
				':');

		if (accessibilityEnabled == 1) {
			Log.v(TAG, "***ACCESSIBILIY IS ENABLED*** -----------------");
			String settingValue = Settings.Secure.getString(mContext
					.getApplicationContext().getContentResolver(),
					Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
			if (settingValue != null) {
				TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
				splitter.setString(settingValue);
				while (splitter.hasNext()) {
					String accessabilityService = splitter.next();

					Log.v(TAG, "-------------- > accessabilityService :: "
							+ accessabilityService);
					if (accessabilityService.equalsIgnoreCase(service)) {
						Log.v(TAG,
								"We've found the correct setting - accessibility is switched on!");
						return true;
					}
				}
			}
		} else {
			Log.v(TAG, "***ACCESSIBILIY IS DISABLED***");
		}

		return accessibilityFound;
	}
}
