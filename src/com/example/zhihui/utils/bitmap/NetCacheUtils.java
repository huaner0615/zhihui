package com.example.zhihui.utils.bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.lidroid.xutils.bitmap.download.Downloader;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

public class NetCacheUtils {
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
		mLocalCacheUtils=localCacheUtils;
		mMemoryCacheUtils = memoryCacheUtils;
	}
	public void getBitmapFromNet(ImageView imageView, String url) {
		BitmapTask bitmapTask = new BitmapTask();
		bitmapTask.execute(imageView ,url);
		
	}
	/**
	 * 
	 * AsyncTask 是线程池与Handler的封装
	 * 第一个泛型：传参的参数类型，与doInBackground一致
	 * 第二个泛型：更新进度的参数类型 与opProgress一致
	 * 第三个泛型：返回结果的参数类型与 onPreExecute,onPostExecute一致
	 * */
	class BitmapTask extends AsyncTask<Object, Integer, Bitmap>{
		private ImageView imageView;
		private String url;
		//主线程运行,预加载
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		//子线程运行，异步加载逻辑在此方法中处理
		@Override
		protected Bitmap doInBackground(Object... param) {
			imageView = (ImageView) param[0];
			url = (String) param[1];
			
			imageView.setTag(url);//将imageView与URL绑定一起
			return download(url);
		}
		//主线程运行，更新进度
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		//主线程运行，更新主界面
		@Override
		protected void onPostExecute(Bitmap result) {
			if(result!=null){
				//判断当前图片是否就是imageView图片
				String bindUrl = (String) imageView.getTag();
				if(bindUrl.equals(url)){
					imageView.setImageBitmap(result);
					System.out.println("网络下载成功");
					
					//将图片保存在本地
					mLocalCacheUtils.setBitmapToLocal(result, url);
					//将图片保存在内存卡
					mMemoryCacheUtils.setBitmapToMemory(url, result);
				}
				
			}
			
		}
		
	}
	public Bitmap download(String url) {
		HttpsURLConnection connection = null;
		try {
			connection = (HttpsURLConnection) (new URL(url).openConnection());
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestMethod("GET");
			
			connection.connect();
			
			int responseCode = connection.getResponseCode();
			if(responseCode==200){
				InputStream inputStream = connection.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				return bitmap;
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {

			if(connection!=null){
				connection.disconnect();
			}
		}
		return null;
		
	}
	
	
}
