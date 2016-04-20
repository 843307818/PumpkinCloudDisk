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

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Register extends Activity {
	//�������
	EditText register_username;
	EditText register_userpassword;
	Button register_register;
	ImageButton land_return;
	
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_layout);
		context = this;
		initView();
	}
	
	private void initView() {
		land_return = (ImageButton) findViewById(R.id.land_return);
		land_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		register_username = (EditText) findViewById(R.id.register_username);
		register_userpassword = (EditText) findViewById(R.id.register_userpassword);
		register_register = (Button) findViewById(R.id.register_register);
		register_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//����ע�᷽��
				register(register_username.getText().toString(), register_userpassword.getText().toString());
				
			}
		});
	}
	
	// �����������ע����Ϣ
	public void register(final String username, final String userpassword) {
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
					dataout.writeUTF("5" + " " + username + " " + userpassword);

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
								Toast.makeText(context, "ע��ɹ�", Toast.LENGTH_SHORT).show();
								finish();
							} else if (result == 2 ) {
								Toast.makeText(context, "�û����Ѿ�����", Toast.LENGTH_SHORT).show();
								register_userpassword.setText("");
							} else {
								Toast.makeText(context, "ע��ʧ��", Toast.LENGTH_SHORT).show();
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
