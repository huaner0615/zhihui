package com.example.zhihui.domain;

import java.util.ArrayList;

import android.R.integer;

public class NewsMenuData {
	public int retcode;
	public ArrayList<String> extend;
	public ArrayList<NewsData> data;
	
	public class NewsData{
		public String id;
		public String title;
		public int type ;
		public ArrayList<NewsTabData> children;
		@Override
		public String toString() {
			return "NewsData [title=" + title + ", children=" + children + "]";
		}
		
	}
	public class NewsTabData{ 
		public String id;
		public String title;
		public String url;
		public int type;
		@Override
		public String toString() {
			return "NewsTabData [title=" + title + "]";
		}
	}
	@Override
	public String toString() {
		return "NewsMenuData [data=" + data + "]";
	}
	
}
