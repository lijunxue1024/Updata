package com.example.updata.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.view.View;
import android.widget.Toast;
import com.example.updata.AndroidPhoto.AndroidPhotoUrl;
import com.example.updata.R;
import com.example.updata.http.Http;

public class MainAtivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private final int SELECT_PIC_KITKAT = 1;
    private final int SELECT_PIC = 2;

    public static final int MESSAGE_FAIL = 0;
    public static final int MESSAGE_SUCCESS = 1;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case MESSAGE_FAIL:
                    Toast.makeText(MainAtivity.this,"上传文件错误！",Toast.LENGTH_SHORT).show();
                    break;


                case MESSAGE_SUCCESS:
                    Toast.makeText(MainAtivity.this,"上传文件成功！",Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;


            }
        }
    };




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.updata_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMethod();
            }
        });
    }


         //startActivityForResult回调
         @Override
         protected void onActivityResult(int requestCode, int resultCode, Intent data) {

             switch (requestCode) {

                 case SELECT_PIC_KITKAT:
                     /**
                      *使用原来的文件名字.
                      */

                     String url_save_original_name = "http://yourip:8080/AndroidInputPhoto/GetPhotoAtionTest";
                     httpAction(data,url_save_original_name);
                     break;

                 case  SELECT_PIC:
                     /**
                      * 使用随机生成的数字文件名字
                      */

                     String url_save_random_name = "http://yourip:8080/AndroidInputPhoto/GetPhotoAction.do";
                     httpAction(data,url_save_random_name);

                     break;



                 default:
                     break;


             }

             }

private void selectMethod()
{
    /**
     * 4.4以前的版本使用随机生成的文件名字      4.4 以后的办本使用 原来的名字
     */
    Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.setType("image/jpeg");
    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){
        startActivityForResult(intent, SELECT_PIC_KITKAT);
    }else{
        startActivityForResult(intent, SELECT_PIC);
    }
}



private void httpAction(Intent data,String url_save_name)
{
    Uri uri = data.getData();

    /**
     *     获取 处理后的图片 绝对地址
     */

    String PathAndfileName = AndroidPhotoUrl.getPath(this, uri);
    new Thread() {
        @Override
        public void run() {
            super.run();
            Http http = new Http(url_save_name, PathAndfileName, handler);
            http.updata();
        }
    }.start();

}



}
