package com.spy.utility.rw;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 向文件中写入数据
 * 
 * @author shipy
 */
public class WriteFile {

	/**
	 * 向文件中写入数据（字节流FileOutputStream）
	 * 
	 * @param filePath
	 * @param content
	 */
	public static void write1(String filePath, String content) {
		FileOutputStream fop = null;
		File file;
		try {
			file = new File(filePath);
			fop = new FileOutputStream(file);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 向文件中写数据（FileReader）【注意使用FileReader（“path”，true）可以往文件后面追加内容，否则就直接覆盖了】
	 * 
	 * @param filePath
	 * @param data
	 */
	public static void write2(String filePath, String content) {
		try {
			File file = new File(filePath);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			fileWritter.write(content);
			fileWritter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 采用BufferedWriter向文件中写数据
	 * 
	 * @param filePath
	 * @param content
	 */
	public static void write3(String filePath, String content) {
		try {
			File file = new File(filePath);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.flush();
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
