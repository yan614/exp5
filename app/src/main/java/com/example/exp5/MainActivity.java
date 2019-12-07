package com.example.exp5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class MainActivity extends AppCompatActivity {
private Button button;
private TextView textview;
private SeekBar seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        textview=findViewById(R.id.textview);
        seekbar=findViewById(R.id.seekbar);


       final Handler handler=new Handler(){    //使用匿名内部类
            @Override
            public void handleMessage(Message msg){
                textview.setText(msg.arg1+"");
                seekbar.setMax(100);//进度条最大值
                seekbar.setProgress(msg.arg1);//arg1用于携带int整数型数据，传送简单数据
            }
        };
        //实现Java的Runnable接口，并重写run()方法，在run()方法中实现耗时运算。
       final Runnable updateThread = new Runnable(){
            public void run(){
                int i = 0;
                while (i<=100){
                    Message msg = new Message();
                    msg.arg1 = i;
                    handler.sendMessage(msg);  //在线程中每秒产生一个数字，然后通过Hander.sendMessage(Message)将消息发送给主线程,
                    //在主线程Handler.handleMessage()中接收并处理该消息
                    i+=1;
                    try{
                        Thread.sleep(100);//xiumian100haomiao
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                Message msg = handler.obtainMessage(); //发送新的消息。从消息池拿来一个msg，不需要new
                msg.arg1 = -1;
                handler.sendMessage(msg);
            }

       };
      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //创建Thread对象，将Runnable对象传递给Thread对象
              Thread workThread=new Thread(null,updateThread,"WorkThread");
              //最后调用Thread.start()方法启动线程，当run()返回时，该线程即结束
              workThread.start();
          }
      });

    }
}
