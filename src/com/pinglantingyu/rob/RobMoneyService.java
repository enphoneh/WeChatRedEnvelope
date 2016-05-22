package com.pinglantingyu.rob;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class RobMoneyService extends AccessibilityService {

	public AccessibilityNodeInfo minfo;
	private boolean mLuckyClicked;
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		
		Log.i("robmoney","AccessibilityEvent is " +event);
		int eventType = event.getEventType();
		
	
		
		switch (eventType) {
		    case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
			List<CharSequence> texts = event.getText();
			if (!texts.isEmpty()) {
				for (CharSequence text : texts) {
					String content = text.toString();
					Log.i("robmoney", "text:"+content);
				  if (content.contains("[微信红包]")) {
						if (event.getParcelableData() != null
								&& 
							event.getParcelableData() instanceof Notification) {
							Notification notification = (Notification) event.getParcelableData();
							PendingIntent pendingIntent = notification.contentIntent;
							try {
								pendingIntent.send();
							} catch (CanceledException e) {
								e.printStackTrace();
							}
							mLuckyClicked = false;
							
						}
					}
				}
			}
			break;
		case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
			String className = event.getClassName().toString();
			Log.i("robmoney","className is " + className);
			if(mLuckyClicked)
			{
				Log.i("robmoney","mLuckyClicked is true" );
			}
			else
			{
				Log.i("robmoney","mLuckyClicked is false" );
			}
		
			if (className.equals("com.tencent.mm.ui.LauncherUI")&&(!mLuckyClicked)) {

				getPacket();// 领取红包
			} else if (className
					.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
				mLuckyClicked = true;
				openPacket();
			// 打开红包
			}
			else if(className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")){
				mLuckyClicked = true;
			//	backLauncher();//返回主页
			}
			break;
		}
	}

	@SuppressLint("NewApi")
	private void openPacket() {
		AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		if (nodeInfo != null) {
			List<AccessibilityNodeInfo> list = nodeInfo
				.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b2c");
			for (AccessibilityNodeInfo n : list) {
				n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			}
			List<AccessibilityNodeInfo> list_old = nodeInfo.
					findAccessibilityNodeInfosByText("拆红包");
				for (AccessibilityNodeInfo n : list_old) {
					n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				}
		    List<AccessibilityNodeInfo> list_new = nodeInfo
						.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b43");
			for (AccessibilityNodeInfo n : list_new) {
						n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					}
			
		}

	}

	@SuppressLint("NewApi")
	private void getPacket() {
		AccessibilityNodeInfo rootNode = getRootInActiveWindow();
		recycle(rootNode);
		if(minfo != null)
		{
		 	minfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        	AccessibilityNodeInfo parent = minfo.getParent();
        	while(parent != null){
        		Log.i("demo", "parent isClick:"+parent.isClickable());
        		if(parent.isClickable()){
        			parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        			break;
        		}
        		parent = parent.getParent();
        	}
		}
	}
	
	int j=0;

	@SuppressLint("NewApi")
	public void recycle(AccessibilityNodeInfo info) {  
	
		
        if (info.getChildCount() == 0) { 
        	
        	if(info.getText() != null){
        		Log.i("robmoney","textinfo "+ info.getText().toString() );
        		if(info.getText().toString().contains("你领取了"))
        		{
        			minfo=null;
        		}
        	
        		if("领取红包".equals(info.getText().toString())){
        			//Log.i("robmoney","info is "+info.getParent().toString());
        			//Log.i("red", "Click"+",isClick:"+info.isClickable());
        			minfo = info;
               
                	
            	}
        	}
        	
        } else {  
            for (int i = 0; i < info.getChildCount(); i++) {  
                if(info.getChild(i)!=null){  
                    recycle(info.getChild(i));  
                }  
            }  
        }  
    }  
	
	
	@Override
	public void onInterrupt() {

	}
	public void backLauncher(){
		
		//延迟两秒跳转
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);  
				mHomeIntent.addCategory(Intent.CATEGORY_HOME);  
				mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  
				                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);  
				startActivity(mHomeIntent); 
				
			}
		},1000);
		
	}

}
