package com.example.zhihui;

import com.example.zhihui.utils.PrefUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

	private RelativeLayout rlFoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		rlFoot = (RelativeLayout) findViewById(R.id.rl_foot);
		
		//旋转
		RotateAnimation toAni = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,0.5f ,Animation.RELATIVE_TO_SELF,0.5f);
		toAni.setDuration(1000);
		toAni.setFillAfter(true);
		//缩放
		ScaleAnimation scAni = new ScaleAnimation(0, 1, 0, 1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		scAni.setDuration(1000);
		scAni.setFillAfter(true);
		//渐变
		AlphaAnimation alAni = new AlphaAnimation(0, 1);
		alAni.setDuration(2000);
		alAni.setFillAfter(true);
		//动画集合
		AnimationSet aniSet = new AnimationSet(true);
		aniSet.addAnimation(toAni);
		aniSet.addAnimation(scAni);
		aniSet.addAnimation(alAni);
		
		rlFoot.startAnimation(aniSet);
		
		aniSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				boolean isGuidShow = PrefUtils.getBoolean("is_guid_show", false, getApplicationContext());
				if(isGuidShow){
					Intent intent = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(getApplicationContext(),GuidShowActivity.class);
					startActivity(intent);
				}
				
				finish();
				
			}
			
		});
		
	}

	

}
