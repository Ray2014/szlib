package com.app4joy.szlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

public class SzlibActivity extends Activity {

	private EditText b_key; // 搜索关键字
	private CheckBox b_type_book; // 图书类型
	private CheckBox b_type_elecbook; // 电子书类型
	private CheckBox b_type_qikan; // 期刊类型
	private CheckBox b_type_sound; // 电子音像类型
	private Spinner b_index; // 检索途径
	private RadioButton b_cirtype_local; // 预借
	private RadioButton b_cirtype_l; // 外借
	private RadioButton b_cirtype_r; // 阅览
	private RadioButton b_cirtype_all; // 全部

	private Button btn_query; // 查询按钮
	private Button btSearchClear;

	private String v_value = ""; // 搜索关键字
	private String v_tablearray = ""; // 要查询的库
	private String v_index = "";
	private String v_startpubyear = "";
	private String v_endpubyear = "";
	private String cirtype = "";
	private String v_publisher = "";
	private String v_author = "";
	private String v_book = "";
	private String v_qikan = "";
	private String v_elecbook = "";
	private String v_sound = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 使用友盟错误报告
		MobclickAgent.onError(this);
		setContentView(R.layout.main);
		initViews();
		// 检查更新
		UmengUpdateAgent.update(this);

		// 用户反馈
		UMFeedbackService.enableNewReplyNotification(this,
				NotificationType.AlertDialog);
	}

	private void initViews() {
		b_key = (EditText) findViewById(R.id.v_value);
		b_type_book = (CheckBox) findViewById(R.id.b_type_book);
		b_type_elecbook = (CheckBox) findViewById(R.id.b_type_elecbook);
		b_type_qikan = (CheckBox) findViewById(R.id.b_type_qikan);
		b_type_sound = (CheckBox) findViewById(R.id.b_type_sound);
		b_index = (Spinner) findViewById(R.id.b_index_spinner);
		b_cirtype_local = (RadioButton) findViewById(R.id.cirtype1);
		b_cirtype_l = (RadioButton) findViewById(R.id.cirtype2);
		b_cirtype_r = (RadioButton) findViewById(R.id.cirtype3);
		b_cirtype_all = (RadioButton) findViewById(R.id.cirtype4);
		btn_query = (Button) findViewById(android.R.id.button1);
		btSearchClear = (Button) findViewById(R.id.btSearchClear);
		b_index.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					v_index = "title";
					break;
				case 1:
					v_index = "all";
					break;
				case 2:
					v_index = "subject";
					break;
				case 3:
					v_index = "classno";
					break;
				case 4:
					v_index = "author";
					break;
				case 5:
					v_index = "isbn";
					break;
				case 6:
					v_index = "callno";
					break;
				case 7:
					v_index = "publisher";
					break;

				default:
					v_index = "title";
					break;
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {
				v_index = "title";

			}

		});

		// 查询按钮事件处理
		btn_query.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v_value = b_key.getText().toString();
				if ("".equals(v_value)) {
					b_key.setError(getString(R.string.edit_key));
					return;
				}

				if (b_type_book.isChecked()) {
					v_book = "on";
					v_tablearray += "bibliosm%2C";
				}
				if (b_type_elecbook.isChecked()) {
					v_elecbook = "on";
					v_tablearray += "apabibibm%2C";
				}
				if (b_type_qikan.isChecked()) {
					v_qikan = "on";
					v_tablearray += "serbibm%2C";
				}
				if (b_type_sound.isChecked()) {
					v_sound = "on";
					v_tablearray += "mmbibm%2C";
				}
				if (b_cirtype_local.isChecked()) {
					cirtype = "local_reserve";
				}
				if (b_cirtype_l.isChecked()) {
					cirtype = "cirtype_l";
				}
				if (b_cirtype_r.isChecked()) {
					cirtype = "cirtype_r";
				}
				if (b_cirtype_all.isChecked()) {
					cirtype = "";
				}

				Intent intent = new Intent();
				intent.putExtra("para", getPara());
				intent.setClass(SzlibActivity.this, ShowQueryActivity.class);
				startActivity(intent);
			}

		});

		b_key.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					btSearchClear.setVisibility(View.VISIBLE);
				} else {
					btSearchClear.setVisibility(View.GONE);
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		// 清除按钮事件处理
		btSearchClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				b_key.setText("");
			}
		});

	}

	private String getPara() {
		String b_type = "";
		b_type += ("".equals(v_book)) ? "" : "&v_book=" + v_book;
		b_type += ("".equals(v_qikan)) ? "" : "&v_qikan=" + v_qikan;
		b_type += ("".equals(v_elecbook)) ? "" : "&v_elecbook=" + v_elecbook;
		b_type += ("".equals(v_sound)) ? "" : "&v_sound=" + v_sound;
		return "v_tablearray=" + v_tablearray + b_type

		+ "&v_index=" + v_index + "&v_value=" + v_value + "&cirtype=" + cirtype
				+ "&v_startpubyear=" + v_startpubyear + "&v_endpubyear="
				+ v_endpubyear + "&v_publisher=" + v_publisher + "&v_author="
				+ v_author + "&sortfield=score&sorttype=desc";
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(1,1,1,"反馈").setIcon(R.drawable.menu_modify_name);
		
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			UMFeedbackService.openUmengFeedbackSDK(this);
			break;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int pid = android.os.Process.myPid();
			android.os.Process.killProcess(pid);
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

}