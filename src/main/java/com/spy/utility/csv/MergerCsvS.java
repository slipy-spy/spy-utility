package com.spy.utility.csv;

import com.spy.utility.rw.ReadFile;

/**
 * 合并csv文件
 * 
 * @author shipy
 */
public class MergerCsvS {

	public static void main(String[] args) {
		String path = "/Users/shipy/Desktop/A智能助手/智能助手20170908/邹晓栋/";
		String fName = "ZNZSSCM_JTQSG--今日强势股.csv";

		StringBuffer sb = new StringBuffer();
		ReadFile.read1(path + fName, sb);

		String str = sb.toString();
		System.out.println(sb.toString());
		str.indexOf("\n");
	}
}
