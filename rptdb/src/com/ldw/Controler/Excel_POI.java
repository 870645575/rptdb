package com.ldw.Controler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ldw.Dao.App;
import com.ldw.Enterties.depotEntity;

public class Excel_POI {

	public static void read(InputStream inputStream) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		HSSFSheet sheet = workbook.getSheetAt(0);
		App app = new App();
		for (int j = 1; j <= sheet.getLastRowNum(); j++) {
			HSSFRow row = sheet.getRow(j);
			try {
				String name = row.getCell(0).toString();
				String area = row.getCell(1).toString();
				int capacity = (int) Float.parseFloat(row.getCell(2).toString());
				HSSFCell c3 = row.getCell(3);// coordinate
				HSSFCell c4 = row.getCell(4);// createTime
				depotEntity d = null;
				if (c4 == null || c4.toString().equals("null")) {
					if (c3 == null ||!Map_API.Cord_check(c3.toString())) {
						d = new depotEntity(name, area, capacity);
					} else {
						String coordinate = row.getCell(3).toString();
						d = new depotEntity(name, area, capacity, coordinate);
					}
				} else {
					String da = row.getCell(4).toString();
					System.out.println(da);
					Date createTime = Date.valueOf(da);
					System.out.println(createTime.toString());
					if (c3 == null || !Map_API.Cord_check(c3.toString())) {
						d = new depotEntity(name, area, capacity, createTime);
					} else {
						String coordinate = row.getCell(3).toString();
						d = new depotEntity(name, area, capacity, coordinate, createTime);
					}
				}
				app.AddData(d);
			} catch (Exception e) {
				System.err.println("error" + e.getMessage() + e.getStackTrace());
			}
		}

		workbook.close();
	}
}
