package com.example.pumpkinonlinedisk.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

import com.example.pumpkinonlinedisk.bean.Flag;

public class Util {

	//����رշ���
	public static void close(InputStream in, OutputStream out,
			DataInputStream datains, DataOutputStream dataout, Socket socket) {
		try {
			if (datains != null)
				datains.close();
			if (dataout != null)
				dataout.close();
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//��鵱ǰ�����Ƿ����Ϊid��Flag���Ǿͷ�������Ķ���û�з���null
	public static Flag getStart(LinkedList<Flag> list, int id) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == id) {
				list.get(i).setDownload(true);
				return list.get(i);
			}
		}
		return null;
	}
}
