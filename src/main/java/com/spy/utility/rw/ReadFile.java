package com.spy.utility.rw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件的读取
 * 
 * @author shipy
 */
public class ReadFile {

	/**
	 * 节点流FileInputStream读取字节流
	 * 
	 * @param filePath
	 * @param sb
	 */
	public static void read1(String filePath, StringBuffer sb) {
		File file = new File(filePath);
		read1(file, sb);
	}

	/**
	 * 节点流FileInputStream读取字节流
	 * 
	 * @param filePath
	 * @param sb
	 */
	public static void read1(File file, StringBuffer sb) {
		// 一般先创建file对象
		FileInputStream fileInput = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			byte[] buffer = new byte[1024];
			fileInput = new FileInputStream(file);
			int byteread = 0;
			// byteread表示一次读取到buffers中的数量。
			while ((byteread = fileInput.read(buffer)) != -1) {
				String str = new String(buffer, "gbk");
				sb.append(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileInput != null) {
					fileInput.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 节点流FileReader读取字符流
	 * 
	 * @param filePath
	 * @param sb
	 */
	public static void read2(String filePath, StringBuffer sb) {
		FileReader reader = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			reader = new FileReader(file);
			char[] buffer = new char[1024];
			int charread = 0;
			while ((charread = reader.read(buffer)) != -1) {
				sb.append(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过BufferedReader读取数据
	 * 
	 * @param filePath
	 * @param sb
	 */
	public static void read3(String filePath, StringBuffer sb) {
		try {
			File file = new File(filePath);
			// 读取文件，并且以utf-8的形式写出去
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String read;
			while ((read = bufferedReader.readLine()) != null) {
				sb.append(read);
			}
			System.out.println(sb.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
