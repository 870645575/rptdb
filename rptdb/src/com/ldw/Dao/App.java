package com.ldw.Dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.ldw.Enterties.depotEntity;

public class App {
	private static String USERNAMR = "rptdb";
	private static String PASSWORD = "rptdb1234";
	private static String DRVIER = "oracle.jdbc.OracleDriver";
	private static String URL = "jdbc:oracle:thin:@10.37.0.5:1521:ygwdb";

	Connection connection = null;
	PreparedStatement pstm = null;
	ResultSet rs = null;

	public Connection getConnection()  {
		try {
			Properties prop=new Properties();
			InputStream in = App.class.getClassLoader().getResourceAsStream("jdbc.properties");//读取文件	
			prop.load(in);
			USERNAMR = prop.getProperty("userName");
			PASSWORD = prop.getProperty("pwd");
			URL = prop.getProperty("url");
			DRVIER = prop.getProperty("driver");
			Class.forName(DRVIER);
			connection = DriverManager.getConnection(URL, USERNAMR, PASSWORD);
		}catch (Exception ee) {		
			System.out.println("配置文件出错，启动测试数据库");
			 try{
					Class.forName(DRVIER);
					connection = DriverManager.getConnection(URL, USERNAMR, PASSWORD);							
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("class not find !", e);
			} catch (SQLException e) {
				throw new RuntimeException("get connection error!", e);
		}
			System.out.println(ee.getMessage());
			// throw new RuntimeException("get jdbc.properties file error!", ee);
			 }
		return connection;
	}

	public void AddData(depotEntity d) {
		DeleteData(d);
		connection = getConnection();
		String sqlStr = "insert into t_depot_area values(?,?,?,?,?)";
		try {
			pstm = connection.prepareStatement(sqlStr);
			pstm.setString(1, d.getName());
			pstm.setString(2, d.getArea());
			pstm.setDouble(3, d.getCapacity());
			pstm.setString(4, d.getCoordinate());
			pstm.setDate(5, d.getCreateTime());
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ReleaseResource();
		}
	}

	public void UpdateData(depotEntity d1, depotEntity d2) {
		connection = getConnection();
		String sql = "update t_depot_area set " + "d_name=?, d_area=?, d_capacity=?,  d_coordinate=?,  d_date=?  "
				+ "where d_name=? and d_area=? and d_capacity=? and d_coordinate=? and d_date=?";
		try {
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, d1.getName());
			pstm.setString(2, d1.getArea());
			pstm.setDouble(3, d1.getCapacity());
			pstm.setString(4, d1.getCoordinate());
			pstm.setDate(5, d1.getCreateTime());
			pstm.setString(6, d2.getName());
			pstm.setString(7, d2.getArea());
			pstm.setDouble(8, d2.getCapacity());
			pstm.setString(9, d2.getCoordinate());
			pstm.setDate(10, d2.getCreateTime());
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ReleaseResource();
		}
	}

	public ArrayList<depotEntity> SelectData() {
		connection = getConnection();
		String sql = "select * from t_depot_area";
		ArrayList<depotEntity> dd = new ArrayList<depotEntity>();
		depotEntity depotEntity = null;
		try {
			pstm = connection.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				String name = rs.getString("d_name");
				String area = rs.getString("d_area");
				double capacity = rs.getDouble("d_capacity");
				String coordinate = rs.getString("d_coordinate");
				Date createTime = rs.getDate("d_date");
				depotEntity = new depotEntity(name, area, capacity, coordinate, createTime);
				dd.add(depotEntity);
				// System.out.println(depotEntity.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ReleaseResource();
		}
		return dd;
	}

	public void DeleteData(String d_name) {
		connection = getConnection();
		String sqlStr = "delete from t_depot_area where d_name=?";
		try {
			pstm = connection.prepareStatement(sqlStr);
			pstm.setString(1, d_name);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ReleaseResource();
		}
	}

	public void DeleteData(depotEntity d) {
		connection = getConnection();
		String sqlStr = "delete from t_depot_area where d_name=? and d_area=? and "
				+ "d_capacity=? and d_coordinate=? and d_date=?";
		try {
			pstm = connection.prepareStatement(sqlStr);
			pstm.setString(1, d.getName());
			pstm.setString(2, d.getArea());
			pstm.setDouble(3, d.getCapacity());
			pstm.setString(4, d.getCoordinate());
			pstm.setDate(5, d.getCreateTime());
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ReleaseResource();
		}
	}

	public void ReleaseResource() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstm != null) {
			try {
				pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
