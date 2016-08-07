package com.example.updata.http;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.updata.activity.MainAtivity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * Created by rabook on 2016/8/6.
 */
public class Http {
    private   String url ;
    private String fileName;
    private Handler handler;



    private String boundy = "---------------------------7e036c3b1d0fdc";
    private String prefix = "--";
    private String end = "\r\n";

    public Http(String url, String fileName, Handler handler) {
        this.url = url;
        this.fileName = fileName;
        this.handler = handler;




    }




    public void updata()
    {
        try {
                URL httpUrl = new URL(url);
                HttpURLConnection con = (HttpURLConnection) httpUrl.openConnection();
                con.setRequestMethod("POST");
                con.setReadTimeout(5000);
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" +boundy);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(prefix+boundy+end);
                out.writeBytes("Content-Disposition: form-data;"
                        +"name=\"file\"; filename=\""
                        +getFileName(fileName)+"\""+end);
                out.writeBytes(end);
            FileInputStream fileInputStream = new FileInputStream(new File(fileName));
            int len = 0;
            byte[] bytes = new byte[1024*4];
            while ((len = fileInputStream.read(bytes)) != -1)
            {
                out.write(bytes,0,len);
            }
            out.writeBytes(end);
            out.writeBytes(prefix+boundy+prefix+end);
            out.flush();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder str = new StringBuilder();
            String s;
            while ((s = reader.readLine()) != null)
            {
                str.append(s);
            }

            Log.d("respose","---------------"+str.toString());

            if (reader != null)
            {
                reader.close();
            }


            if (fileInputStream != null)
            {
                fileInputStream.close();
            }

            if (out != null)
            {
                out.close();
            }

            Message message = new Message();
            message.what = MainAtivity.MESSAGE_SUCCESS;
            handler.sendMessage(message);

        } catch (MalformedURLException e) {

            Message message = new Message();
            message.what = MainAtivity.MESSAGE_FAIL;
            handler.sendMessage(message);

            e.printStackTrace();
        } catch (IOException e) {

            Message message = new Message();
            message.what = MainAtivity.MESSAGE_FAIL;
            handler.sendMessage(message);

            e.printStackTrace();
        }

    }



    //获取文件名字
    private String getFileName(String fileName)
    {
        return fileName.substring(fileName.lastIndexOf("/")+1);
    }
}
