package com.app4joy.szlib.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app4joy.szlib.R;
import com.app4joy.szlib.ShowDetailActivity;
import com.app4joy.szlib.vo.BookInfo;

public class QueryInfoAdapter extends BaseAdapter {
	private List<BookInfo> list;
	private Activity context;
	public QueryInfoAdapter(Activity context,List<BookInfo> list) {
		this.list = list;
		this.context = context;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = context.getLayoutInflater().inflate(R.layout.listitem,
					null);
		}

		TextView t_title = (TextView) convertView
				.findViewById(R.id.b_title);
		TextView t_author = (TextView) convertView
				.findViewById(R.id.b_author);
		TextView t_detail = (TextView) convertView
				.findViewById(R.id.b_detail);
		TextView t_text = (TextView) convertView.findViewById(R.id.b_text);

		t_title.setText(list.get(position).getTitle());
		t_author.setText(list.get(position).getAuthor());
		t_detail.setTag(list.get(position).getDetail());
		t_text.setText(list.get(position).getText());
		
		//详细信息点击事件
		t_detail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String url = "http://www.szlib.gov.cn/Search/"+v.getTag();
				Intent intent = new Intent();
				intent.putExtra("url", url);
				intent.setClass(context, ShowDetailActivity.class);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	public void addItem(BookInfo bookInfo) {
		list.add(bookInfo);
	}

}
