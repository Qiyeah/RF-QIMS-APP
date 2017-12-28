package com.zhilian.hzrf_oa.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.common.BusinessContant;
import com.zhilian.hzrf_oa.entity.OpinionBeen;
import com.zhilian.api.StrKit;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 填写意见的适配器
 */
public class OpinionAdapter extends BaseAdapter {
    private List<OpinionBeen> list = null;
    private Context mContext;
    private Handler pic_hdl = new PicHandler();
    ImageView signimg;
    private LayoutInflater mInflater;
    private BusinessContant bc=new BusinessContant();
    public OpinionAdapter(List<OpinionBeen> list, Context context){
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.opinion_item_layout, null);
        }
        // 意见
        TextView opinion = (TextView) convertView.findViewById(R.id.opinion);
        opinion.setText(list.get(position).getOpinion());
        signimg=(ImageView)convertView.findViewById(R.id.signimg);
        TextView sign=(TextView)convertView.findViewById(R.id.sign);
        if (StrKit.notBlank(list.get(position).getSignimg())) {
           /* bc.setImaUrl(list.get(position).getSignimg());
            Thread t = new LoadPicThread();
            t.start();*/
            bc.setImaUrl(list.get(position).getSignimg());
            Thread t = new DownPicThread();
            t.start();
            String[] imgurls=bc.getImaUrl().split("/");
            String imgurl=imgurls[imgurls.length-1];
            File file = new File(bc.SAVEPATH);
            if (!file.exists()) {
                file.mkdir();
            }
            String path = bc.SAVEPATH+imgurl;
            Bitmap bm = BitmapFactory.decodeFile(path);
            signimg.setImageBitmap(bm);
            sign.setVisibility(View.GONE);

        }else{
            signimg.setVisibility(View.GONE);

            sign.setText(list.get(position).getSign());
        }
        TextView time=(TextView)convertView.findViewById(R.id.time);
        time.setText(list.get(position).getDate());


        return convertView;
    }



    class DownPicThread extends Thread{
        @Override
        public void run(){
            URL url;
            try {
                url = new URL(bc.SIGNIMG+bc.getImaUrl());
                InputStream is = url.openStream();
                byte[] arr = new byte[1];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(baos);
                int n = is.read(arr);
                while (n > 0) {
                    bos.write(arr);
                    n = is.read(arr);
                }
                bos.close();
                String[] imgurls=bc.getImaUrl().split("/");
                String imgurl=imgurls[imgurls.length-1];
                String path = "/mnt/sdcard/mypic_data/"+imgurl;
                File file = new File(path);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.close();

                is.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    class LoadPicThread extends Thread{
        @Override
        public void run(){

            Bitmap img = getUrlImage(bc.SIGNIMG+bc.getImaUrl());
            Message msg = pic_hdl.obtainMessage();
            msg.what = 0;
            msg.obj = img;
            pic_hdl.sendMessage(msg);
        }
    }

    class PicHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            //String s = (String)msg.obj;
            //ptv.setText(s);
            Bitmap myimg = (Bitmap)msg.obj;
            signimg.setImageBitmap(myimg);
        }

    }

//加载图片
    public Bitmap getUrlImage(String url) {
        Bitmap img = null;
        try {
            URL picurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection)picurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(true);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            img = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }


}
