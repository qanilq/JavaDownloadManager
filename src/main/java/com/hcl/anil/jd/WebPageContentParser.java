package com.hcl.anil.jd;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebPageContentParser {

	private String pageContent;

	enum TagLength {
		imgTagLength(4), anchorTagLength(2);

		private int length;

		private TagLength(int length) {
			this.length = length;
		}

		public int length() {
			return length;
		}
	}

	public WebPageContentParser(String pageContent) {
		this.pageContent = pageContent;
	}

	List<URL> getURLList() {
		List<URL> urlList = new ArrayList<>();
		if (null == pageContent || pageContent.equals("")) {
			return urlList;
		}

		return null;
	}

	List<String> getImgTagList() {
		List<String> imgTagList = new ArrayList<>();
		if (null == pageContent || pageContent.equals("")) {
			return imgTagList;
		}

		String temp = pageContent;
//		int j=0;
		while (temp.indexOf("<img") != -1) {
			int index = temp.indexOf("<img");
			String imgTag = getImgTag(index,temp);
			temp = temp.substring(index+1);
			imgTagList.add(imgTag);
		}
		return imgTagList;
	}

	public String getImgTag(int index,String tempPageContent) {
		int imgBeginIndex = index;
		int imgEndIndex = tempPageContent.indexOf(">",imgBeginIndex);
		String imgTag = tempPageContent.substring(imgBeginIndex,imgEndIndex+1);
		return imgTag;

	}
	// public static void main(String[] args) {
	// String s ="123456789";
	// // String s1 ="abc";
	// // System.out.println(s1.indexOf("b"));
	// int beginIndex = s.indexOf("4");
	// int endIndex = s.indexOf("6");
	// System.out.println(beginIndex);
	// System.out.println(endIndex);
	// String subString = s.substring(beginIndex,endIndex);
	// System.out.println(subString);
	// }
}
