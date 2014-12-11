package com.example.demo_httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView tv;
    private Button button;
    private HttpClient client;
    private HttpResponse httpResponse;
    private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv=(TextView)findViewById(R.id.textView1);
		button=(Button)findViewById(R.id.button1);
		
		handler=new Handler(){
			public void handleMessage(android.os.Message msg){
				tv.setText((CharSequence)msg.obj);
			};
		};
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(){
					@Override
					public void run() {
						HttpGet httpGet=new HttpGet("http://www.baidu.com");
						client=new DefaultHttpClient();
						
						InputStream inputStream=null;
							try {
								httpResponse=client.execute(httpGet);
								
								if(httpResponse.getStatusLine().getStatusCode()==200){
									HttpEntity entity=httpResponse.getEntity();
								inputStream=entity.getContent();
								BufferedReader reader=new BufferedReader(
										new InputStreamReader(inputStream));
								StringBuilder builder=new StringBuilder();
								   String text="";
								 while((text=reader.readLine())!=null){
									 builder.append(text);
								 }
								 
								 Message msg=Message.obtain();
								 msg.obj=builder.toString();
								 handler.sendMessage(msg);
								 reader.close();
								}
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				}.start();
				
			}
		});
	}



}
