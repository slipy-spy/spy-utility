package com.spy.utility.file;

import java.io.File;

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
	private static void getDirectory(File file) {
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
}
