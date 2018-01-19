package com.example.asus.zlzjqrcode.utils;

public class Goods {
	private String areas;
	private String count;
	private String yonghu;

	
	public Goods() {
		super();
	}

	public Goods(String areas, String count, String yonghu) {
		super();
		this.areas = areas;
		this.count = count;
		this.yonghu = yonghu;

	}

	public String getAreas() {
		return areas;
	}

	public void setAreas(String areas) {
		this.areas = areas;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getYonghu() {
		return yonghu;
	}

	public void setYonghu(String yonghu) {
		this.yonghu = yonghu;
	}
}
