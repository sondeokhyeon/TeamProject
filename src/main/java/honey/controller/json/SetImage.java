package honey.controller.json;

import java.util.List;

import honey.vo.HoneyMain;
import honey.vo.HoneySearchKeyword;
import honey.vo.UrlInfo;

public class SetImage {
	
	public static List<HoneyMain> setImage(List<HoneyMain> honeyMainList, List<UrlInfo> urlInfoList) {

		for (int i = 0; i < honeyMainList.size(); i++) {

			for (int j = 0; j < urlInfoList.size(); j++) {
				if (honeyMainList.get(i).getNo() == urlInfoList.get(j).getBd_No()) {
					honeyMainList.get(i).setLinkTitle(urlInfoList.get(j).getTitle());
					honeyMainList.get(i).setLinkDesc(urlInfoList.get(j).getDescription());
					honeyMainList.get(i).setLinkImage(urlInfoList.get(j).getImage());
					honeyMainList.get(i).setLinkURL(urlInfoList.get(j).getUrlAddr());
					honeyMainList.get(i).setLinkDetailUrl(urlInfoList.get(j).getDetailUrl());
					System.out.println("link있는애: " + honeyMainList.get(i).getLinkImage());
				}
			}
			//String[] imageHref = honeyMainList.get(i).getLinkDesc().split("\"");
			System.out.println("번호: " + i + "getLinkImage: " + honeyMainList.get(i).getLinkImage());
			if (honeyMainList.get(i).getLinkImage() == null) {
				if (honeyMainList.get(i).getContents() != null) {
					boolean image = honeyMainList.get(i).getContents().contains("img src");
					System.out.println("boolean: " + image);
					if (image) {
						String[] temp = honeyMainList.get(i).getContents().split("src=");
						String[] temp1 = temp[1].split(" ");
						String[] temp2 = temp1[0].split("\"");
						honeyMainList.get(i).setLinkImage(temp2[1]);
						System.out.println("temp1[0] =" + temp1[0]);
						System.out.println("temp1[0] =" + temp2[1]);
						System.out.println("이거: " + honeyMainList.get(i).getLinkImage());
					} else if (honeyMainList.get(i).getYoutubeURL() != null){
						System.out.println("youtube 꽂는다.");
						honeyMainList.get(i).setLinkImage("https://img.youtube.com/vi/" + honeyMainList.get(i).getYoutubeURL() + "/mqdefault.jpg");
						System.out.println("꽂힌놈 바로 확인= " +honeyMainList.get(i).getLinkImage());
					} else {
						honeyMainList.get(i).setLinkImage("/TeamProject/upload/MainDefault.jpg");
					}
				}
			}
		}
		return honeyMainList;
	}
	
	
	 public static List<HoneySearchKeyword> setImage2(List<HoneySearchKeyword> honeyMainList, List<UrlInfo> urlInfoList) {

	    for (int i = 0; i < honeyMainList.size(); i++) {

	      for (int j = 0; j < urlInfoList.size(); j++) {
	        if (honeyMainList.get(i).getNo() == urlInfoList.get(j).getBd_No()) {
	          honeyMainList.get(i).setLinkTitle(urlInfoList.get(j).getTitle());
	          honeyMainList.get(i).setLinkDesc(urlInfoList.get(j).getDescription());
	          honeyMainList.get(i).setLinkImage(urlInfoList.get(j).getImage());
	          honeyMainList.get(i).setLinkURL(urlInfoList.get(j).getUrlAddr());
	          honeyMainList.get(i).setLinkDetailUrl(urlInfoList.get(j).getDetailUrl());
	          System.out.println("link있는애: " + honeyMainList.get(i).getLinkImage());
	        }
	      }
	      //String[] imageHref = honeyMainList.get(i).getLinkDesc().split("\"");
	      System.out.println("번호: " + i + "getLinkImage: " + honeyMainList.get(i).getLinkImage());
	      if (honeyMainList.get(i).getLinkImage() == null) {
	        if (honeyMainList.get(i).getContents() != null) {
	          boolean image = honeyMainList.get(i).getContents().contains("img src");
	          System.out.println("boolean: " + image);
	          if (image) {
	            String[] temp = honeyMainList.get(i).getContents().split("src=");
	            String[] temp1 = temp[1].split(" ");
	            String[] temp2 = temp1[0].split("\"");
	            honeyMainList.get(i).setLinkImage(temp2[1]);
	            System.out.println("temp1[0] =" + temp1[0]);
	            System.out.println("temp1[0] =" + temp2[1]);
	            System.out.println("이거: " + honeyMainList.get(i).getLinkImage());
	          } else {
	            honeyMainList.get(i).setLinkImage("/TeamProject/upload/MainDefault.jpg");
	          }
	        }
	      }
	    }
	    return honeyMainList;
	  }
	
}
