package com.spy.utility.csv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.spy.utility.file.TraverseDir;

/**
 * 合并csv文件
 * 
 * @author shipy
 */
public class MergerCsvs {

	public static void main(String[] args) {
		// String path = "/Users/shipy/Desktop/A智能助手/智能助手20170908/邹晓栋/";
		String path = "/Users/shipy/Desktop/A智能助手/智能助手20170908";
		String fName = "ZNZSSCM_JTQSG--今日强势股.csv";

		// StringBuffer sb = new StringBuffer();
		// ReadFile.read1(path + fName, sb);
		//
		// String str = sb.toString();
		// System.out.println(str);
		// System.out.println("===================");
		// int indexOf = str.indexOf("\n");
		// String substring = str.substring(indexOf);
		// System.out.println(substring);
		// System.out.println("===================");

		List<File> list = new ArrayList<File>();
		File folder = new File(path);
		if (folder.exists()) {
			TraverseDir.getDirectory(folder, "csv", list);
		}
		for (File file : list) {
			System.out.println(file.getName());
		}
	}
}
