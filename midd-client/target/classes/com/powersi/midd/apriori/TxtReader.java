package com.powersi.midd.apriori;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//本程序用于读取矩阵型的记录数据，并转换为List<List<String>>格式数据
public class TxtReader {

	public List<List<String>> getRecord() {
		List<List<String>> record = new ArrayList<List<String>>();
		try {
			String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )
//			File file = new File("simple.txt");
			InputStream inputStream =this.getClass().getClassLoader().getResourceAsStream("simple.txt");
			if (inputStream!=null) {
				InputStreamReader read = new InputStreamReader(
						inputStream, encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				while ((lineTXT = bufferedReader.readLine()) != null) {//读一行文件
					String[] lineString = lineTXT.split(" ");
					List<String> lineList = new ArrayList<String>();
					for (int i = 0; i < lineString.length; i++) {//处理矩阵中的T、F、YES、NO
							lineList.add(lineString[i]);
					}
					record.add(lineList);
				}
				read.close();
			} else {
				System.out.println("not file！");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容操作出错");
			e.printStackTrace();
		}
		return record;
	}
}
