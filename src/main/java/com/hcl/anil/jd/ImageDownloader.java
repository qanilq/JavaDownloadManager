package com.hcl.anil.jd;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class ImageDownloader {

	Logger logger = Logger.getLogger(ImageDownloader.class);
	//url to connect
	String url;
	
	URL urlToConnect;
	
	public ImageDownloader() {
	}
	
	public ImageDownloader(String url){
		this.url= url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPageContent() throws IOException{
		
		StringBuffer pageContent = new StringBuffer();
		if(url==null){
			throw new MalformedURLException("Please Sepcicy URL to Connect");
		}
		if(null != url){
			urlToConnect = new URL(url);
		}
		HttpURLConnection httpConnection = (HttpURLConnection) urlToConnect.openConnection();
		InputStream is = httpConnection.getInputStream();
		int c;
		while( (c=is.read()) !=-1){
			pageContent.append((char)c);
		}
		return pageContent.toString();
	}
	
	public abstract void downloadImages(String url);
	
	public void getImages(String url) throws IOException{
		System.out.println("Connecting :"+url);
		Document doc = Jsoup.connect(url).get();
		Elements imgs = doc.select("img");
		System.out.println("Images:");
		for (Element img : imgs) {
			String imageURL = img.absUrl("src");
			System.out.println("============================================");
			System.out.println(imageURL);
			getImage(imageURL);
			System.out.println("Download complete");
			System.out.println("============================================");
		}
		
	}
	public void getImage(String imageURL){
		int fileNameIndex = imageURL.lastIndexOf("/");
		String fileName = imageURL.substring(fileNameIndex+1,imageURL.length());
		if(imageURL ==null || imageURL.equals("")) {
			System.out.println("Error downloading image :"+imageURL);
			return;
		}
		try {
			URL url = new URL(imageURL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			InputStream in = con.getInputStream();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
			for(int b; (b = in.read()) != -1;){
				bos.write(b);
			}
			bos.close();
			in.close();
		} catch (IOException ioe) {
			System.out.println("Error downloading image :"+imageURL);
			logger.debug("Error downloading image "+ioe.getMessage());
			System.out.println("============================================");
		}
	}

	
}
