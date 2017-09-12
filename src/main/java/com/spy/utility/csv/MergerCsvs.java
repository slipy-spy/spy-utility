package com.spy.utility.csv;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.spy.utility.file.TraverseDir;
import com.spy.utility.rw.ReadFile;
import com.spy.utility.rw.WriteFile;

/**
 * 合并csv文件
 * 
 * @author shipy
 */
public class MergerCsvs {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public static void main(String[] args) {
		// String path = "/Users/shipy/Desktop/A智能助手/智能助手20170908/邹晓栋/";
		// String fName = "ZNZSSCM_JTQSG--今日强势股.csv";

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

		String path = "/Users/shipy/Desktop/A智能助手/智能助手";
		Date date = new Date();
		String format = sdf.format(date);
		path += format + "/";
		String f1 = "邹晓栋";
		String f2 = "潘明旺";
		String path1 = path + f1;
		String path2 = path + f2;
		merger(path1, path1 + ".csv");
		merger(path2, path2 + ".csv");
	}

	public static void merger(String path, String newFile) {
		List<File> list = new ArrayList<File>();
		File folder = new File(path);
		if (folder.exists()) {
			TraverseDir.getDirectory(folder, "csv", list);
		}
		StringBuffer sb = null;
		for (File file : list) {
			System.out.println(file.getName());
			StringBuffer sb2 = new StringBuffer();
			ReadFile.read1(file, sb2);
			if (sb == null) {
				sb = new StringBuffer();
				sb.append(sb2);
			} else {
				int indexOf = sb2.toString().indexOf("\n");
				sb.append(sb2.toString().substring(indexOf + 1));
			}
		}
		System.out.println(sb.toString());
		File ffff = new File(newFile);
		if (ffff.exists()) {
			ffff.delete();
		}
		WriteFile.write1(newFile, sb.toString());
	}
}
