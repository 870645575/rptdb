package com.ldw.myweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ldw.Controler.Excel_POI;
import com.ldw.Controler.Map_API;
import com.ldw.Dao.App;
import com.ldw.Enterties.depotEntity;

/**
 * Servlet implementation class myservlet
 */
@WebServlet("/myservlet")
public class myservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public myservlet() {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	int count = 0;
	int count2 = 0;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		count++;
		System.out.println("doGet" + count + request.getRequestURI());
		App app = new App();
		ArrayList<depotEntity> dd = app.SelectData();
		JSONArray array = JSONArray.parseArray(JSON.toJSONString(dd));
		request.setAttribute("dd", array);
		request.getRequestDispatcher("").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		count++;
		System.out.println("doPost" +count2+ request.getServletPath());
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		App app = new App();
		// --------------------------------删除----------------------------------
		if (request.getServletPath().equals("/delete")) {
			int deleID = Integer.parseInt(request.getParameter("deleID"));
			System.out.println("deleID" + deleID);
			ArrayList<depotEntity> dd = app.SelectData();
			if (deleID >= 0 && deleID < dd.size()) {
				app.DeleteData(dd.get(deleID));
				request.setAttribute("al", "删除成功");
			} else {
				request.setAttribute("al", "删除失败");
			}
			// --------------------------------修改----------------------------------
		} else if (request.getServletPath().equals("/edit")) {
			int deleID = Integer.parseInt(request.getParameter("deleID"));
			System.out.println("edit" + deleID);
			ArrayList<depotEntity> dd = app.SelectData();
			depotEntity d1 = create(request);
			System.out.println(d1);
			if (d1 != null) {
				request.setAttribute("al", "修改成功");
				app.UpdateData(d1, dd.get(deleID));
			} else {
				request.setAttribute("al", "修改失败");
			}
			// --------------------------------插入----------------------------------
		} else if (request.getServletPath().equals("/index")) {
			try {
				depotEntity d = create(request);
				System.out.println(d);
				app.AddData(d);
				request.setAttribute("al", "插入成功");
			} catch (Exception e) {
				request.setAttribute("al", "插入失败");
				System.err.println(e.getMessage());
			}
		} // --------------------------------上传----------------------------------
		else if (request.getServletPath().equals("/upload")) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			factory.setSizeThreshold(1024 * 500);// 设置内存的临界值为500K
			File linshi = new File("tmp");// 当超过500K的时候，存到一个临时文件夹中
			factory.setRepository(linshi);
			upload.setSizeMax(1024 * 1024 * 5);// 设置上传的文件总的大小不能超过5M
			try {
				List<FileItem> items = upload.parseRequest(request);
				String fileName = "";
				boolean isempty = false;
				for (FileItem item : items) {
					fileName = item.getName();
					long sizeInBytes = item.getSize();
					System.out.println(fileName);
					System.out.println(sizeInBytes);
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
					InputStream in = item.getInputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					ServletContext servletContext = this.getServletContext();
					if (fileName.isEmpty()) {
						isempty = true;
						break;
					}
					fileName = servletContext.getRealPath("/WEB-INF") + "\\" + fileName;
					//System.out.println(fileName);
					OutputStream out = new FileOutputStream(new File(fileName));
					while ((len = in.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					out.close();
					in.close();
					if (!isempty) {
						String s = upload(fileName);
						request.setAttribute("al", s);
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
		// doGet(request, response);
		response.sendRedirect("index");
	}

	public static depotEntity create(HttpServletRequest request) {
		depotEntity d = null;
		String name = request.getParameter("dname");
		String area = request.getParameter("addr");
		double capacity = Double.parseDouble(request.getParameter("capa"));
		String l = request.getParameter("lon");
		String w = request.getParameter("lat");
		String coordinate = "";
		if (l != "" && w != "") {
			coordinate = l + "," + w;
		}
		String da = request.getParameter("txtEndDate");
		if (da == null || da.equals("")) {
			if (!Map_API.Cord_check(coordinate)) {
				d = new depotEntity(name, area, capacity);
			} else {
				d = new depotEntity(name, area, capacity, coordinate);
			}
		} else {
			Date createTime = Date.valueOf(da);
			if (!Map_API.Cord_check(coordinate)) {
				d = new depotEntity(name, area, capacity, createTime);
			} else {
				d = new depotEntity(name, area, capacity, coordinate, createTime);
			}
		}
		return d;
	}

	public static String upload(String excel_file_path) {
		String string = "";
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(excel_file_path));
			//System.out.println(excel_file_path);
			Excel_POI.read(inputStream);
			string = "数据跟新成功";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			string = e.toString();
		} catch (IOException e) {
			e.printStackTrace();
			string = e.toString();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return string;
	}
}
