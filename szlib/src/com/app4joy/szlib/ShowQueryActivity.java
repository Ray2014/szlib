package com.app4joy.szlib;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app4joy.szlib.adapter.QueryInfoAdapter;
import com.app4joy.szlib.biz.Query;
import com.app4joy.szlib.vo.BookInfo;
import com.umeng.analytics.MobclickAgent;

public class ShowQueryActivity extends ListActivity implements OnScrollListener {

	private String para;
	private Context context;
	private final int msg_prepare = 0;
	private final int msg_nolist = 1;
	private final int msg_error = 2;
	private final int msg_success = 3;
	private ProgressBar progressBar;
	private TextView textView;
	private ListView listView;
	private List<BookInfo> list;
	private QueryInfoAdapter adapter;
	private int totalsize; // 模拟数据集的条数
	private int pageSize = 20; //ListView每页显示的数量
	private View listview_footer_view;
	private TextView listview_footer_text;
	private ProgressBar moreBar;
	private View layouot_more;
	private TextView resultView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showquery);
		para = getIntent().getStringExtra("para");
		context = ShowQueryActivity.this;

		progressBar = (ProgressBar) findViewById(android.R.id.progress);
		textView = (TextView) findViewById(android.R.id.text1);
		listView = (ListView) findViewById(android.R.id.list);
		resultView = (TextView) findViewById(android.R.id.text2);
		listview_footer_view = getLayoutInflater().inflate(
				R.layout.listview_footer, null);
		layouot_more = listview_footer_view.findViewById(R.id.layout_footer);
		listview_footer_text = (TextView) listview_footer_view
				.findViewById(R.id.listview_footer_hint_textview);
		moreBar = (ProgressBar) listview_footer_view.findViewById(R.id.listview_footer_progressbar);
		layouot_more.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				listview_footer_text.setText(R.string.loading);
				moreBar.setVisibility(View.VISIBLE);
				mHandler.postDelayed(new Runnable() {
					public void run() {
						loadMoreData();
						adapter.notifyDataSetChanged();
						listview_footer_text.setText(R.string.lookmore);
						moreBar.setVisibility(View.GONE);
					}
				},2000);

			}
		});
		listView.addFooterView(listview_footer_view); // 设置列表底部视图
		listView.setOnScrollListener(this);
		new Thread(new myRunnable()).start();
	}

	class myRunnable implements Runnable {

		public void run() {
			Query query = new Query(context, para);
			Message msg = mHandler.obtainMessage(msg_prepare);
			try {
				int total = query.getTotal();
				if (total == 0) {
					msg.what = msg_nolist;
				} else {
					List<BookInfo> list = query.queryBook(total);
					msg.what = msg_success;
					msg.obj = list;
				}
			} catch (Exception e) {
				MobclickAgent.reportError(context,e.getMessage()) ;
				//Log.e(tag, e.getMessage());
				//e.printStackTrace();
				msg.what = msg_error;
			}
			msg.sendToTarget();
		}

	}

	Handler mHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case msg_nolist:
				progressBar.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				textView.setVisibility(View.VISIBLE);
				textView.setText(R.string.msg_nolist);
				break;
			case msg_error:
				progressBar.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				textView.setVisibility(View.VISIBLE);
				textView.setText(R.string.msg_error);
				break;
			case msg_success:
				progressBar.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				textView.setVisibility(View.GONE);
				resultView.setVisibility(View.VISIBLE);
				list = (List<BookInfo>) msg.obj;
				totalsize = list.size(); // 获取记录总数
				resultView.setText("共有 "+ totalsize +" 条记录");
				initializeAdapter(); // 初始化ListView的适配器
				listView.setAdapter(adapter);// 设置ListView的适配器
				break;
			}
		}

	};

	/**
	 * 加载更多数据
	 */
	private void loadMoreData() {
		int count = adapter.getCount(); 
		if (count + pageSize <= totalsize) {
			for (int i = count ; i < count + pageSize; i++) {
				adapter.addItem(list.get(i));
			}
		} else {
			for (int i = count; i < totalsize; i++) {
				adapter.addItem(list.get(i));
			}
		}
	}

	/**
	 * 初始化ListView的适配器
	 */
	private void initializeAdapter() {
		List<BookInfo> books = new ArrayList<BookInfo>();
		if (totalsize>=pageSize) {
			for (int i = 0; i < pageSize; i++) {
				books.add(this.list.get(i));
			}
		}else{
			for (int i = 0; i < totalsize; i++) {
				books.add(this.list.get(i));
			}
		}
		
		adapter = new QueryInfoAdapter(ShowQueryActivity.this,books);
	}


	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		 //如果所有的记录选项等于数据集的条数，则移除列表底部视图  
		 if(totalItemCount == totalsize+1){  
	            listView.removeFooterView(listview_footer_view);  
	            //Toast.makeText(this, "数据全部加载完!", Toast.LENGTH_LONG).show();  
	        }  
	}

	@Override
	protected void onResume() {
		super.onResume();
		 MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
