package com.example.pumpkinonlinedisk.aty;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.pumpkinonlinedisk.Config;
import com.example.pumpkinonlinedisk.R;
import com.example.pumpkinonlinedisk.util.ServerUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Land extends Activity{
	//������ť���
	private Button landbtn;
	Context context;
	
	EditText land_username;
	EditText land_userpassword;
	ImageButton land_return;
	Button land_register;
	
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.land_layout);
	        context = this;
	        initView();
	    }
	  
	  private void initView() {
		  land_register = (Button) findViewById(R.id.land_register);
		  land_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Land.this,Register.class);
				startActivity(i);
			}
		});
		  land_return = (ImageButton) findViewById(R.id.land_return);
		  land_return.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		  land_username = (EditText) findViewById(R.id.land_username);
		  land_userpassword = (EditText) findViewById(R.id.land_userpassword);
		  landbtn = (Button) findViewById(R.id.land_land);
		  landbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//���õ�¼����
				login(land_username.getText().toString(),land_userpassword.getText().toString());
			}
		});
	  }
	  
	// ����������͵�¼��Ϣ
		public void login(final String username, final String userpassword) {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Socket socket = new Socket(Config.SEVER_PATH, 8081);
						// ���ؾ���������10.0.2.2
						// �õ����������
						OutputStream out = socket.getOutputStream();
						InputStream in = socket.getInputStream();
						/*
						 * ��װ��
						 */
						DataOutputStream dataout = new DataOutputStream(out);
						DataInputStream datain = new DataInputStream(in);

						// �����������ɾ����Ϣ
						dataout.writeUTF("6" + " " + username + " " + userpassword);

						// ��ȡ���������ص�result
						final int result = datain.readInt();
						 
						// �ر���Դ
						ServerUtil.close(in, out, dataout, datain, socket);
						/*
						 * �õ����������ص�result ���Ϊ1���½��ɹ���Ϊ0���½�ʧ��
						 */

						// ���ص����̣߳�����UI
						((Activity) context).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								/*
								 * ���result = 1�����ע��ɹ�
								 * ��� = 0�����ע��ʧ��
								 */
								Log.d("����ע�᷵�صĽ��", String.valueOf(result));
								if (result == 1) {
									Toast.makeText(context, "��¼�ɹ�", Toast.LENGTH_SHORT).show();
									Config.username = username;
									Intent i = new Intent(Land.this,MainActivity.class);
									startActivity(i);
								} else if (result == 0 ) {
									Toast.makeText(context, "�û������������", Toast.LENGTH_SHORT).show();
								} 
							}
						});
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
}

