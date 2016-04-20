package com.example.pumpkinonlinedisk.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DownAndUpLoadInfo implements Serializable{
	/*
	 * id id��
	 * name �ļ�����
	 * finished ��ǰ�ϴ������س���
	 * isStart �������ʼ�����ò��ܵ����
	 */
	
	private int id;
	//�ļ�����
	private String name;
	//�ļ���С
	private int total;
	
	private long filesize;
 
	private static String URL;

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public DownAndUpLoadInfo() {
		super();
	}

	public DownAndUpLoadInfo(String name, long filesize ,int total,String URL,int a) {
		super();
		this.name = name;
		this.filesize = filesize;
		this.total = total;
		this.URL = URL;
		this.aa = a;
	}
	private int aa; //1���� 2����

	public int getAa() {
		return aa;
	}

	public void setAa(int aa) {
		this.aa = aa;
	}
	private int finished;
	private boolean isStart=false;
	//�ж��Ƿ��ڱ����Ѿ����ˣ��еĻ��Ͳ���������
	private boolean localhad = false;
	
	public boolean isLocalhad() {
		return localhad;
	}

	public void setLocalhad(boolean localhad) {
		this.localhad = localhad;
	}

	//�ж����ļ������ļ���
	/**
	 * 0Ϊ�ļ���1Ϊ�ļ���
	 * Ĭ��Ϊ�ļ�
	 */
	private int type = 0;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	
	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
 
}
