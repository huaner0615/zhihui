package com.example.zhihui.utils.bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.R.integer;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCacheUtils {
	
//	private HashMap<String, SoftReference<Bitmap>> memoryCache = new HashMap<String, SoftReference<Bitmap>>();
	private LruCache<String, Bitmap> mCache; 
	
	public MemoryCacheUtils(){
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		
		//最近最少使用，通过控制内存不超过最大值
		mCache = new LruCache<String, Bitmap>(maxMemory/8){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				int size = value.getRowBytes()*value.getHeight();
				return size;
			}
		};
		 
		 
	}
	
	public Bitmap getBitmapUtilsFromMemory(String url){
		/*SoftReference<Bitmap> softBitmap = memoryCache.get(url);
		if(softBitmap!=null){
			Bitmap bitmap = softBitmap.get();
		}*/
		return mCache.get(url);
	}
	public void setBitmapToMemory(String url,Bitmap bitmap){
		//SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bitmap); 
		// memoryCache.put(url, soft);
		mCache.put(url, bitmap);
	}

}
