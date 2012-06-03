package com.app4joy.szlib.vo;

public class BookInfo {

	private String title;   //标题
	private String author;	// 作者
	private String publisher;//出版社
	private String dates;	//出版日期
	private String detail;	//详细
	private String text; // 说明
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "BookInfo [title=" + title + ", author=" + author
				+ ", publisher=" + publisher + ", dates=" + dates + ", detail="
				+ detail + ", text=" + text + "]";
	}
	
	
	
	
}
