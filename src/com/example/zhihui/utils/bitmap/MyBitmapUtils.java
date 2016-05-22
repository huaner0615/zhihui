package com.example.zhihui.utils.bitmap;

import com.example.zhihui.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyBitmapUtils {
	
	//网络缓存
	private NetCacheUtils netCacheUtils;
	//本地缓存
	private LocalCacheUtils localCacheUtils;
	//内存卡缓存
	private MemoryCacheUtils memoryCacheUtils;
	
	public MyBitmapUtils(){
		
		localCacheUtils = new LocalCacheUtils();
		netCacheUtils= new NetCacheUtils(localCacheUtils,memoryCacheUtils);
		memoryCacheUtils = new MemoryCacheUtils();
	}
	
	public void display(ImageView imageView, String url) {
		// TODO Auto-generated method stub
		
		imageView.setImageResource(R.drawable.news_pic_default);
		
		Bitmap bitmap = memoryCacheUtils.getBitmapUtilsFromMemory(url);
		//从内存卡加载
		if(bitmap!=null){
			imageView.setImageBitmap(bitmap);
			return;
		}
		
		
		
		//从本地缓存加载
		bitmap = localCacheUtils.getBitmapFromLocal(url);
		if(bitmap!=null){
			imageView.setImageBitmap(bitmap);
			
			//给内存设置图片
			memoryCacheUtils.setBitmapToMemory(url, bitmap);
			
			return;
		}
		//从网络缓存加载
		netCacheUtils.getBitmapFromNet(imageView,url);
	}

	
}
