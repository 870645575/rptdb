package com.ldw.Enterties;

import java.io.IOException;
import java.sql.Date;

import com.ldw.Controler.Map_API;

public class depotEntity {

	private String name;
	private String area;
	private double capacity = 0.0;
	private String coordinate;
	private java.sql.Date createTime = null;

	@Override
	public String toString() {

		return name + " " + area + " " + capacity + " " + coordinate + " " + createTime;
	}

	public depotEntity(String name, String area, double capacity, String coordinate, Date createTime) {
		this.name = name;
		this.area = area;
		this.capacity = capacity;
		this.coordinate = coordinate;
		this.createTime = createTime;
	}

	public depotEntity(String name, String area, double capacity) {
		this.name = name;
		this.area = area;
		this.capacity = capacity;
		this.coordinate = Area2Cord(area);
		this.createTime = new Date(System.currentTimeMillis());
	}

	public depotEntity(String name, String area, double capacity, String coordinate) {
		this.name = name;
		this.area = area;
		this.capacity = capacity;
		this.coordinate = coordinate;
		this.createTime = new Date(System.currentTimeMillis());
	}

	public depotEntity(String name, String area, double capacity, Date createTime) {
		this.name = name;
		this.area = area;
		this.capacity = capacity;
		this.coordinate = Area2Cord(area);
		this.createTime = createTime;
	}

	public static String Area2Cord(String string) {
		String s = "0,0";
		try {
			String[] o = Map_API.getCoordinate(string);
			s = "" + o[0] + "," + o[1];
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Date createTime) {
		this.createTime = createTime;
	}

}
