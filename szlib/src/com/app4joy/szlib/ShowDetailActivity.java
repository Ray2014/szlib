package com.app4joy.szlib;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app4joy.szlib.adapter.QueryDetailAdapter;
import com.app4joy.szlib.adapter.QueryDetailAdapter.TableCell;
import com.app4joy.szlib.adapter.QueryDetailAdapter.TableRow;
import com.app4joy.szlib.biz.QueryDetail;
import com.app4joy.szlib.vo.BookOut;
import com.app4joy.szlib.vo.DetailInfo;
import com.umeng.analytics.MobclickAgent;

public class ShowDetailActivity extends Activity {

	private static final int msg_error = 0;
	private static final int msg_success = 1;
	private ProgressBar progressBar;
	private TextView textView;

	private Context context;
	private String url;

	List<BookOut> bOuts; // 外借馆藏
	List<DetailInfo> bIns;// 在馆馆藏

	private TextView title_Ins;
	private TextView title_Outs;
	private View layout_ins;
	private View layout_outs;
	private ListView list_ins;
	private ListView list_outs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = ShowDetailActivity.this;
		setContentView(R.layout.showdetail);
		url = getIntent().getStringExtra("url");
		System.out.println("url:" + url);
		progressBar = (ProgressBar) findViewById(android.R.id.progress);
		textView = (TextView) findViewById(android.R.id.text1);

		layout_ins = findViewById(R.id.layout_ins);
		layout_outs = findViewById(R.id.layout_outs);
		
		title_Ins = (TextView) findViewById(R.id.title_Ins);
		title_Outs = (TextView) findViewById(R.id.title_Outs);
		
		list_ins = (ListView) findViewById(R.id.list_ins);
		list_outs = (ListView) findViewById(R.id.list_outs);

		new Thread(new MyRunnable()).start();
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progressBar.setVisibility(View.GONE);
			switch (msg.what) {
			case msg_error:
				textView.setVisibility(View.VISIBLE);
				textView.setText(R.string.msg_error);
				break;
			case msg_success:
				/**************在馆馆藏**************/
				if (bIns.size() <= 0) {
					layout_ins.setVisibility(View.GONE);
				} else {
					layout_ins.setVisibility(View.VISIBLE);
					title_Ins.setText("在馆馆藏: ( " + bIns.size()+" )");

					ArrayList<TableRow> tableIns = new ArrayList<TableRow>();
					
					// 定义标题  
					TableCell[] titles = new TableCell[5];
					int width = ShowDetailActivity.this.getWindowManager()
							.getDefaultDisplay().getWidth()
							/ titles.length;
					titles[0] = new TableCell("条码号", 180,
							LayoutParams.FILL_PARENT, TableCell.STRING);
					titles[1] = new TableCell("索书号", 160,
							LayoutParams.FILL_PARENT, TableCell.STRING);
					titles[2] = new TableCell("所在地", 220,
							LayoutParams.FILL_PARENT, TableCell.STRING);
					titles[3] = new TableCell("馆藏状态", width,
							LayoutParams.FILL_PARENT, TableCell.STRING);
					titles[4] = new TableCell("流通类别", 130,
							LayoutParams.FILL_PARENT, TableCell.STRING);
					
					//添加标题
					tableIns.add(new TableRow(titles));

					for (int i = 0; i <bIns.size(); i++){
						DetailInfo info = bIns.get(i);
						TableCell[] cells = new TableCell[5];
						cells[0] = new TableCell(info.getBarCode(),
									titles[0].width, LayoutParams.FILL_PARENT,
									TableCell.STRING);
						cells[1] = new TableCell(info.getGetNum(),
								titles[1].width, LayoutParams.FILL_PARENT,
								TableCell.STRING);
						cells[2] = new TableCell(info.getLocation(),
								titles[2].width, LayoutParams.FILL_PARENT,
								TableCell.STRING);
						cells[3] = new TableCell(info.getState(),
								titles[3].width, LayoutParams.FILL_PARENT,
								TableCell.STRING);
						cells[4] = new TableCell(info.getType(),
								titles[4].width, LayoutParams.FILL_PARENT,
								TableCell.STRING);
						tableIns.add(new TableRow(cells));
					}
						
					
					QueryDetailAdapter adapterIns = new QueryDetailAdapter(ShowDetailActivity.this,tableIns);
					list_ins.setAdapter(adapterIns);
				}
				
				/****************外借馆藏****************/
				if (bOuts.size() <= 0) {
					layout_outs.setVisibility(View.GONE);
				} else {
					layout_outs.setVisibility(View.VISIBLE);
					title_Outs.setText("外借馆藏: ( " + bOuts.size()+" )");
					
					
					ArrayList<TableRow> tableOuts = new ArrayList<TableRow>();
					
					// 定义标题  
					TableCell[] titlesOuts = new TableCell[5];
					int width = ShowDetailActivity.this.getWindowManager()
							.getDefaultDisplay().getWidth()
							/ titlesOuts.length;
					titlesOuts[0] = new TableCell("条码号", 180,
							LayoutParams.FILL_PARENT, TableCell.STRING);
					titlesOuts[1] = new TableCell("索书号", 160,
							LayoutParams.FILL_PARENT, TableCell.STRING);
					titlesOuts[2] = new TableCell("馆藏状态", width,
							LayoutParams.FILL_PARENT, TableCell.STRING);
					titlesOuts[3] = new TableCell("流通类别", 150,
							LayoutParams.FILL_PARENT, TableCell.STRING);
					titlesOuts[4] = new TableCell("应还日期", 110,
							LayoutParams.FILL_PARENT, TableCell.STRING);
					
					//添加标题
					tableOuts.add(new TableRow(titlesOuts));
					
					//添加数据
					for (int i = 0; i <bOuts.size(); i++){
						BookOut out = bOuts.get(i);
						TableCell[] cells = new TableCell[5];
						cells[0] = new TableCell(out.getBarCode(),
								titlesOuts[0].width, LayoutParams.FILL_PARENT,
									TableCell.STRING);
						cells[1] = new TableCell(out.getGetNum(),
								titlesOuts[1].width, LayoutParams.FILL_PARENT,
								TableCell.STRING);
						cells[2] = new TableCell(out.getState(),
								titlesOuts[2].width, LayoutParams.FILL_PARENT,
								TableCell.STRING);
						cells[3] = new TableCell(out.getType(),
								titlesOuts[3].width, LayoutParams.FILL_PARENT,
								TableCell.STRING);
						cells[4] = new TableCell(out.getBackDate(),
								titlesOuts[4].width, LayoutParams.FILL_PARENT,
								TableCell.STRING);
						//添加数据
						tableOuts.add(new TableRow(cells));
					}
					
					QueryDetailAdapter adapterOuts = new QueryDetailAdapter(ShowDetailActivity.this,tableOuts);
					list_outs.setAdapter(adapterOuts);
				}


			
				break;
			default:
				break;
			}
		}

	};

	class MyRunnable implements Runnable {

		public void run() {
			Message msg = mHandler.obtainMessage();
			try {
				QueryDetail queryDetail = new QueryDetail(context, url);
				bOuts = queryDetail.queryBookOut();
				bIns = queryDetail.queryBookOnLib();
				msg.what = msg_success;
			} catch (Exception e) {
				MobclickAgent.reportError(context,e.getMessage()) ;
				msg.what = msg_error;
			}
			msg.sendToTarget();
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
