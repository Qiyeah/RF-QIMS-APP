package com.zhilian.hzrf_oa.util;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Administrator on 2016/12/3.
 * 用于打开各种文件的类
 */
public class OpenFileIntent {
	//自定义androidIntent类，
	//可用于获取打开以下文件的intent
	//PDF,PPT,WORD,EXCEL,CHM,HTML,TEXT,AUDIO,VIDEO

	//这个不行，可能是因为PDF.apk程序没有权限访问其它APK里的asset资源文件,又或者是路径写错?
	//Intentit = getPdfFileIntent("file:///android_asset/helphelp.pdf");
	//下面这些都OK
	//Intentit = getHtmlFileIntent("/mnt/sdcard/tutorial.html");//SD卡主目录
	//Intentit = getHtmlFileIntent("/sdcard/tutorial.html");//SD卡主目录,这样也可以
	//Intent it= getHtmlFileIntent("/system/etc/tutorial.html");//系统内部的etc目录
	//Intentit = getPdfFileIntent("/system/etc/helphelp.pdf");
	//Intentit = getWordFileIntent("/system/etc/help.doc");
	//Intentit = getExcelFileIntent("/mnt/sdcard/Book1.xls")
	//Intentit = getPptFileIntent("/mnt/sdcard/download/Android_PPT.ppt");//SD卡的download目录下
	//Intentit = getVideoFileIntent("/mnt/sdcard/ice.avi");
	//Intentit = getAudioFileIntent("/mnt/sdcard/ren.mp3");
	//Intentit = getImageFileIntent("/mnt/sdcard/images/001041580.jpg");
	//Intentit = getTextFileIntent("/mnt/sdcard/hello.txt",false);

	//android获取一个用于打开HTML文件的intent
	public static Intent getHtmlFileIntent(String param )
	{
		Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri,"text/html");
		return intent;
	}

	//android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(String param )
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"image/*");
		return intent;
	}



	//android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent( String param )
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"application/pdf");
		return intent;
	}

	//android获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent( String param, boolean paramBoolean)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean)
		{
			Uri uri1 =Uri.parse(param );
			intent.setDataAndType(uri1,"text/plain");
		}
		else
		{
			Uri uri2 =Uri.fromFile(new File(param ));
			intent.setDataAndType(uri2,"text/plain");
		}

		return intent;
	}

	//android获取一个用于打开音频文件的intent
	public static Intent getAudioFileIntent(String param )
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange",0);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"audio/*");
		return intent;
	}

	//android获取一个用于打开视频文件的intent
	public static Intent getVideoFileIntent(String param )
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange",0);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"video/*");
		return intent;
	}

	//android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent( String param )
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"application/x-chm");
		return intent;
	}

	//android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(String param )
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"application/msword");
		return intent;
	}

	//android获取一个用于打开Excel文件的intent
	public static Intent getExcelFileIntent(String param )
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"application/vnd.ms-excel");
		return intent;
	}

	//android获取一个用于打开PPT文件的intent
	public static Intent getPptFileIntent( String param )
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

}
