package com.ldw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

import com.ldw.Controler.Excel_POI;
import com.ldw.Dao.App;
import com.ldw.Enterties.depotEntity;

/**************************
 * 这个主要用于调试，不用可以删掉，对项目没有影响
 ************************************/
public class Main {

	private static ArrayList<depotEntity> dd=null;
	private static App app =null;
	private static Scanner in=new Scanner(System.in);
	public static void main(String[] args) {
		app = new App();
		dd=app.SelectData();
		while (true) {
			in=new Scanner(System.in);
			dd=app.SelectData();
			System.out.println(""
					+ "1.增加记录\n"
					+ "2.查看记录\n"
					+ "3.删除记录\n"
					+ "4.修改记录\n"
					+ "5.导入记录\n"
					+ "6.退出\n");
			int fun=0;
			try {
				 fun=in.nextInt();
			} catch (Exception e) {
				continue;
			}

			switch (fun) {
			case 1:
				add(dd);
				break;
			case 2:
				print(dd);
				break;
			case 3:
				del(dd);
				break;
			case 4:
				edit(dd);
				break;
			case 5:
				upload();
				break;
			default:
				in.close();
				System.exit(0);
				break;
			}
		}
	}
	public static void print(ArrayList<depotEntity> dd) {
		for (int i = 0; i < dd.size(); i++) {
			String string = dd.get(i).toString();
			System.out.println(i+" "+string);
		}
	}
	public static void upload() {
		 InputStream inputStream = null; 
		    try {
		      inputStream = new FileInputStream(new File("rtpdb.xls")); 
		      System.out.println("rtpdb.xls");
		      Excel_POI.read(inputStream); 
		    } catch (FileNotFoundException e) { 
		      e.printStackTrace(); 
		    } catch (IOException e) { 
		      e.printStackTrace(); 
		    }finally{ 
		      try { 
		        if(inputStream != null){ 
		          inputStream.close(); 
		        } 
		      } catch (IOException e) { 
		        e.printStackTrace(); 
		      } 
		    }
	}
	public static void edit(ArrayList<depotEntity> dd) {
		print(dd);
		in.reset();
		System.out.println("请输入要修改的编号");
		int index=in.nextInt();
		if (index<dd.size()) {
			System.out.println("确定修改用户"
					+dd.get(index)+ " y/n？");
			String tmp1=in.next();
			if (tmp1.equals("y")) {				
				System.out.println("请输入用户名");
				String name1=in.next();
				System.out.println("请输入地址");
				String add1=in.next();
				System.out.println("请输入容量");
				int cap1=in.nextInt();
				depotEntity a1=new depotEntity(name1,add1,cap1);
				app.UpdateData(a1 ,dd.get(index));
			}			
		}
}
	public static void add(ArrayList<depotEntity> dd) {
			System.out.println(""
					+ "1.快速录入\n"
					+ "2.完整记录\n");
			int fun=in.nextInt();
			try {
				 fun=in.nextInt();
			} catch (Exception e) {
				add(dd);
			}
			switch (fun) {
			case 2:
				System.out.println("请输入用户名");
				String name=in.next();
				System.out.println("请输入地址");
				String add=in.next();
				System.out.println("请输入容量");
				int cap=in.nextInt();
				System.out.println("请输入经纬度");
				String cood=in.next();			
				int date1=0;
				System.out.println("请输入注册年份");
				while (date1-1900>9999||date1-1900<1900) {
					date1=in.nextInt();					
				}
				date1-=1900;
				int date2=0;
				System.out.println("请输入注册月份");
				while (date2-1>11||date2-1<0) {
					date2=in.nextInt();
				}
				date2-=1;
				System.out.println("请输入注册日期");
				int date3=0;
				while (date3>31||date1<1) {
					date3=in.nextInt();
				}
				@SuppressWarnings("deprecation") depotEntity a=new depotEntity(name,add,cap,cood,new Date(date1,date2,date3));
				app.AddData(a);
				break;
			default:
				in.reset();
				System.out.println("请输入用户名");
				String name1=in.next();
				System.out.println("请输入地址");
				String add1=in.next();
				System.out.println("请输入容量");
				int cap1=in.nextInt();
				depotEntity a1=new depotEntity(name1,add1,cap1);
				app.AddData(a1);
				break;
			}
	}
	public static void del(ArrayList<depotEntity> dd) {
		System.out.println(""
				+ "1.快速删除\n"
				+ "2.搜索删除\n");
		int fun=0;
		try {
			 fun=in.nextInt();
		} catch (Exception e) {
			del(dd);
		}
		switch (fun) {
		case 2:
			System.out.println("请输入要删除的用户名");
			String name=in.next();
			System.out.println("确定删除"
					+name+ "y/n？");
			String tmp=in.next();
			if (tmp.equals("y")){
				app.DeleteData(name);
			}			
			break;
		default:
			print(dd);
			System.out.println("请输入要删除的编号");
			int index=in.nextInt();
			if (index<dd.size()) {
				System.out.println("确定删除用户"
						+dd.get(index)+ " y/n？");
				String tmp1=in.next();
				if (tmp1.equals("y")) {
					app.DeleteData(dd.get(index));
				}			
			}
			break;
		}

}
}
