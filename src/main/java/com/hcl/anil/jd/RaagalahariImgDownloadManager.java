package com.hcl.anil.jd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RaagalahariImgDownloadManager extends ImageDownloader{
	private static String gallerySelector = ".galleryname"; 
	private int numberOfGalleries;
	
	public RaagalahariImgDownloadManager() {
		
	}
	
	RaagalahariImgDownloadManager(String url,int numberOfGalleries){
		this.numberOfGalleries = numberOfGalleries;
	}
	
	@Override
	public void downloadImages(String galleryUrl) {
		try {
			HashSet<String> galleryUrls = getGalleryLinks(galleryUrl, gallerySelector);
			int i=0;
			for (String url : galleryUrls) {
				if(i >= numberOfGalleries){
					return;
				}
				downloadImagesFromGallery(url);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void downloadImagesFromGallery(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			//First Page link processing
			List<String> bigImageUrls = getBigImageUrls(doc);
//			System.out.println(bigImageUrls);
			getImages(bigImageUrls);
			HashSet<String> otherPageLinks = getOtherPages(doc);
			for (String link : otherPageLinks) {
				doc = Jsoup.connect(link).get();
				bigImageUrls = getBigImageUrls(doc);
				getImages(bigImageUrls);
//				System.out.println(bigImageUrls);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void getImages(List<String> bigImageUrls) {
		for (String link : bigImageUrls) {
			try {
				getImages(link);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public boolean checkGalleryHasLinks(String galleryUrl) {
		Document doc = null;
		boolean hasLinks = true;
		
		try {
			doc = Jsoup.connect(galleryUrl).get();
			//First Page link processing
			List<String> bigImageUrls = getBigImageUrls(doc);
//			System.out.println(galleryUrl +" has "+bigImageUrls.size() +"big image links");
			if(bigImageUrls.size()==0) {
				hasLinks = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hasLinks;
	}
	
	private HashSet<String> getOtherPages(Document doc){
		//get other links
		Elements elements = doc.select(".otherPage");
		HashSet<String> pages = removeDuplicateLinks(elements);
		return pages;
	}

	private HashSet<String> removeDuplicateLinks(Elements elements) {
		HashSet pagesSet = new HashSet<>();
		for (Element element : elements) {
			pagesSet.add(element.absUrl("href"));
		}
		return pagesSet;
	}
	
	public  HashSet<String> getPageLinksBasedOnCSS(String url,String cssSelector) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements elements = doc.select(cssSelector);
		HashSet pagesSet = new HashSet<>();
		for (Element element : elements) {
			pagesSet.add(element.absUrl("href"));
		}
		return pagesSet;
	}

	public HashSet<String> getOtherPageLinks(String url, String cssSelector) throws IOException{
		return getPageLinksBasedOnCSS(url, cssSelector);
	}
	public HashSet<String> getGalleryLinks(String url, String cssSelector) throws IOException{
		return getPageLinksBasedOnCSS(url, cssSelector);
	}
	
	private List<String> getBigImageUrls(Document doc) {
		Elements elements = doc.select("a>img");
		List<String> bigImageUrls = new ArrayList<>();
		for (Element img : elements) {
			if(img.toString().contains("jpg")){
				String imageUrl = img.parent().absUrl("href");
//				if(imageUrl.contains("image")){
				bigImageUrls.add(imageUrl);
			}
		}
		return bigImageUrls;
	}
	
//	public HashSet<String> getGalleryLinks(String url) throws IOException{
//		Document doc = Jsoup.connect(url).get();
//		Elements elements = doc.select(".galleryname");
//		HashSet<String> pages = removeDuplicateLinks(elements);
//		return pages;
//	}
	
	/**
	public static void main(String[] args) {
//		String url = "http://www.raagalahari.com/actress/35261/heroine-charmme-kaur-in-black-saree.aspx";
//		String url ="http://www.raagalahari.com/actress/45321/tamil-actress-monal-gajjar-hd-wallpapers.aspx";
		RaagalahariImgDownloadManager downloadManager = new RaagalahariImgDownloadManager();
		String urlG = "http://www.raagalahari.com/stars/profile/1149/charmi.aspx";
		HashSet<String> links;
		try {
			links = downloadManager.getGalleryLinks(urlG,".galleryname");
//
			System.out.println("total galleries"+links.size());
//			System.out.println(links);
//			String link1= null;
			for (String link : links) {
				downloadManager.downloadImagesFromGallery(link);
				System.out.println("=======================******************************************======================");
			}
//				link1 = link;
//				try{
////					System.out.println("trying to check :"+link);
//					boolean isGalleryHasLinks = downloadManager.checkGalleryHasLinks(link);
//					if(isGalleryHasLinks==false){
//						System.out.println("No link in the gallery: "+link);
//					}
//				}catch(Exception e){
////					e.printStackTrace();
//					System.err.println("Not able to connect: "+link);
//				}
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		String url ="http://www.raagalahari.com/actress/10441/charmi-at-santosham-11th-anniversary-awards-press-meet.aspx";
//		String url ="http://www.raagalahari.com/actress/1919/charmi.aspx";
		
//	}
	}**/
	public static void main(String[] args) {
		String urlG = "http://www.raagalahari.com/stars/profile/1149/charmi.aspx";
		RaagalahariImgDownloadManager downloadManager = new RaagalahariImgDownloadManager(urlG,1);
		downloadManager.downloadImages(urlG);
	}
}
