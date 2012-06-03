package com.app4joy.szlib.biz;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.util.Log;

import com.app4joy.szlib.vo.BookInfo;

public class Query {

	private static final String tag = "szlib";
	private String url;
	private Context context;

	public Query(Context context, String para) {
		url = "http://szlib.gov.cn/Search/searchshow.jsp?" + para;
		this.context = context;
		Log.i(tag, "Query--url:"+url);
	}

	/**
	 * 获取记录总数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getTotal() throws Exception {
		int total = 0;
		String path = this.url + "&pageNum=1";
		Document doc = Jsoup.parse(HttpUtil.getHtml(context,path));
		Element num = doc.select("strong.num").first();
		total = Integer.parseInt(num.text());
		return total;
	}

	/**
	 * 从网页中读取查询的图书信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<BookInfo> queryBook(int pageNum) throws Exception {
		List<BookInfo> list = new ArrayList<BookInfo>();
		String path = this.url + "&pageNum=" +pageNum;
		Document doc = Jsoup.parse(HttpUtil.getHtml(context,path));
		Element resultlist = doc.select("ol.resultlist").first();
		Elements books = resultlist.getElementsByTag("li");
		int count = 1;
		for (Element book : books) {
			BookInfo info = new BookInfo();
			info.setTitle(count+"、"+book.getElementsByClass("title").text());
			info.setAuthor(book.getElementsByClass("author").text());
			info.setPublisher(book.getElementsByClass("publisher").text());
			info.setDates(book.getElementsByClass("dates").text());
			info.setDetail(book.getElementsByClass("link2").attr("href"));
			info.setText(book.getElementsByClass("text").text());
			list.add(info);
			count++;
		}
		return list;
	}
}
