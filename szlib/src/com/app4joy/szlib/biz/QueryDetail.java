package com.app4joy.szlib.biz;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.util.Log;

import com.app4joy.szlib.vo.BookOut;
import com.app4joy.szlib.vo.DetailInfo;

public class QueryDetail {

	private static final String tag = "szlib";
	private Document doc;
	public QueryDetail(Context context,String url) throws Exception{
		this.doc = Jsoup.parse(HttpUtil.getHtml(context, url));
		Log.i(tag, "QueryDetail--url:"+url);
	}
	
	/**
	 * 查询馆藏情况
	 * @return
	 * @throws Exception
	 */
	public List<String> queryLib() throws Exception{
		List<String> libList = new ArrayList<String>();
		Element ul = doc.select("ul.sidenav").first();
		Elements li = ul.getElementsByTag("li");
		for(Element lib:li){
			libList.add(lib.text());
		}
		return libList;
	}
	
	/**
	 * 查询已经借出书籍
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public List<BookOut> queryBookOut() throws Exception {
		List<BookOut> list = new ArrayList<BookOut>();
		Element table = doc.select("div#borrowed").first();
		if(table!=null){
			Elements trs = table.select("tr").not("thead");
			if (trs!=null) {
				int count = 0;
				for (Element row:trs) {
					Elements tds = row.select("td");
					if (count==0) {
						count++;
						continue;
					}
					BookOut bookOut = new BookOut();
					bookOut.setBarCode(tds.get(0).text().trim());
					bookOut.setGetNum(tds.get(1).text().trim());
					bookOut.setState(tds.get(2).text().trim());
					bookOut.setType(tds.get(4).text().trim());
					bookOut.setBackDate(tds.get(5).text().trim());
					list.add(bookOut);
				}
			}
		}
		return list;
	}
	
	/**
	 * 查询在馆图书
	 * @param doc
	 * @return
	 */
	public  List<DetailInfo> queryBookOnLib(){
		List<DetailInfo> list = new ArrayList<DetailInfo>();
		Elements tablesDivs = doc.select("div.main2").select("div").not("#borrowed");
		int i = 0;
		if (tablesDivs!=null) {
			for(Element table:tablesDivs){
				Elements trs = table.select("tr");
				if (i==0) {
					i++;
					continue;
				}
				if (trs!=null) {
					int j=0;
					for (Element row:trs) {
						if (j==0) {
							j++;
							continue;
						}
						Elements tds = row.select("td");
						DetailInfo info = new DetailInfo();
						info.setBarCode(tds.get(0).text());
						info.setGetNum(tds.get(1).text());
						info.setLocation(tds.get(2).text());
						info.setState(tds.get(3).text());
						info.setType(tds.get(5).text());
						list.add(info);
						}
				}
			}
		}
		return list;
	}
	
}
