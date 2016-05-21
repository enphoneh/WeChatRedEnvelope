package com.pinglantingyu.rob;



import com.pinglantingyu.rob.UI.RoundImageView;
import com.pinglantingyu.rob.UI.XiuYiXiuView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private XiuYiXiuView mXiuView = null;
	private  RoundImageView mPhoto = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	//View组件初始化
	private  void initView(){
		mXiuView = (XiuYiXiuView) findViewById(R.id.wv);
		mPhoto = (RoundImageView) findViewById(R.id.my_photo);
		mPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showXiuAnim();
				openServiceSetting();
			}
		});
		
	}
	//显示咻一咻动画
	private void showXiuAnim(){
			// 执行动画
			mXiuView.start();

		}
	
	//打开辅助功能
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
}
