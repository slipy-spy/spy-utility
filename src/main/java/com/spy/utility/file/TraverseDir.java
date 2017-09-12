package com.spy.utility.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 遍历文件夹
 * 
 * @author shipy on 2017年7月12日 下午11:57:37
 */
public class TraverseDir {
	public static void main(String[] args) {
		String fPath = "E:/迅雷下载/";
		File file = new File(fPath);
		getDirectory(file);
	}

	// 递归遍历
	public static void getDirectory(File file) {
		File flist[] = file.listFiles();
		if (flist == null || flist.length == 0) {
			return;
		}
		for (File f : flist) {
			if (f.isDirectory()) {
				// 这里将列出所有的文件夹
				System.out.println("Dir==>" + f.getAbsolutePath());
				getDirectory(f);
			} else {
				// 这里将列出所有的文件
				System.out.println("file==>" + f.getAbsolutePath());
			}
		}
	}

	/**
	 * 递归遍历(提取相应类型的文件)
	 * 
	 * @param file
	 *            初始文件夹
	 * @param suf
	 *            文件后缀名
	 */
	public static void getDirectory(File file, String suf, List<File> list) {
		File flist[] = file.listFiles();
		if (flist == null || flist.length == 0) {
			return;
		}
		for (File f : flist) {
			if (f.isDirectory()) {
				// 这里将列出所有的文件夹
				// System.out.println("Dir==>" + f.getAbsolutePath());
				getDirectory(f, suf, list);
			} else {
				// 这里将列出所有的文件
				// System.out.println("file==>" + f.getAbsolutePath());
				String name = f.getName();
				String[] strs = name.split("\\.");
				if (suf.equalsIgnoreCase(strs[strs.length - 1])) {
					if (list == null) {
						list = new ArrayList<File>();
					}
					list.add(f);
				}
			}
		}
	}
}
