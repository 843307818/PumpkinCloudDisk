package com.example.pumpkinonlinedisk.bean;

public class Flag {
	/*
	 * isDownload �Ƿ���ͣ
	 * id id��
	 * num �������еĵڼ���
	 */
	private boolean isDownload=false;
	int id;
	int num;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public boolean isDownload() {
		return isDownload;
	}
	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
