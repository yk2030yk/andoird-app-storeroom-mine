package com.example.mine;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MemoFragment extends Fragment {
	MyMemoCustomAdapter adapter = null;
	ArrayList<MemoData> list = new ArrayList<MemoData>();
	String LIST_ID = "1";

	ListView listView;
	EditText et;
	Button btn;

	SQLiteDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("method", "onCreate");
		SQLiteOpenHelper helper = new MySQLiteHelper(getActivity().getApplicationContext(), "my.db", null, 1);
		db = helper.getWritableDatabase();
		adapter = new MyMemoCustomAdapter(getActivity(), R.layout.item_memo_mine, list);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("method", "onCreateView");
		return inflater.inflate(R.layout.memo_fragment, null);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("method", "onStart");
		listView = (ListView) getActivity().findViewById(R.id.memo_fragment_list);
		et = (EditText) getActivity().findViewById(R.id.memo_fragment_edit);
		btn = (Button) getActivity().findViewById(R.id.memo_fragment_editbtn);

		listView.setAdapter(adapter);
		initData();

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = et.getText().toString();
				if (!str.equals("")) {
					addData(str);
					initData();
					et.setText("");
					int lastnum = listView.getCount();// Listのアイテム数を取得
					listView.setSelection(lastnum);
					InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				final int pos = position;
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				alert.setTitle("削除してよろしいですか?");
				alert.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d("method", "dialog cancel");
					}
				});
				alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteData(adapter.getItem(pos).id);
						initData();
						Log.d("method", "dialog ok");
					}
				});
				alert.show();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("method", "onResume");
	}

	void initData() {
		adapter.clear();
		String[] columns = { "ID", "MEMO", "CHECKED", "DEL", "LISTID" };
		String where = "LISTID=?";
		String[] whereArgs = { String.valueOf(LIST_ID) };
		Cursor cursor = db.query("MEMO_TABLE", columns, where, whereArgs, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("ID"));
			String name = cursor.getString(cursor.getColumnIndex("MEMO"));
			int checked = cursor.getInt(cursor.getColumnIndex("CHECKED"));
			int del = cursor.getInt(cursor.getColumnIndex("DEL"));
			int tid = cursor.getInt(cursor.getColumnIndex("LISTID"));
			adapter.add(new MemoData(id, name, checked, del, tid, 1));
		}
		if (cursor.getCount() == 0) {
			addData("メモしてください。");
			initData();
		}
		cursor.close();

		// チャット上に画像を載せるテストデータ
		// adapter.add(new MemoData(-1, "picture", -1, -1, -1, 2));
		// adapter.add(new MemoData(-1, "picture", -1, -1, -1, 3));

		adapter.notifyDataSetChanged();
	}

	void addData(String str) {
		ContentValues values = new ContentValues();
		values.put("MEMO", str);
		values.put("LISTID", LIST_ID);
		db.insert("MEMO_TABLE", null, values);
	}

	// idのデータを削除
	void deleteData(int id) {
		String where = "ID=?";
		String[] whereArgs = { String.valueOf(id) };
		db.delete("MEMO_TABLE", where, whereArgs);
	}

	// idのデータをstrに変更
	void editData(int id, String str) {
		ContentValues values = new ContentValues();
		values.put("MEMO", str);
		String where = "ID=?";
		String[] params = { String.valueOf(id) };
		db.update("MEMO_TABLE", values, where, params);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public class MyMemoCustomAdapter extends ArrayAdapter<MemoData> {
		Context mycontext;
		LayoutInflater layoutInflater;
		int viewResourceId;
		ArrayList<MemoData> dataList;

		public MyMemoCustomAdapter(Context context, int viewResourceId, ArrayList<MemoData> list) {
			super(context, viewResourceId, list);
			mycontext = context;
			this.viewResourceId = viewResourceId;
			layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dataList = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			MemoData memo = dataList.get(position);
			switch (memo.memoKind) {
			case 1:
				view = layoutInflater.inflate(viewResourceId, null);
				break;
			case 2:
				view = layoutInflater.inflate(R.layout.item_memo_other, null);
				break;
			case 3:
				view = layoutInflater.inflate(R.layout.item_memo_other, null);
				break;
			default:
				break;
			}

			LinearLayout layout = (LinearLayout) view.findViewById(R.id.item_memo_layout);

			switch (memo.memoKind) {
			case 1:
				layout.setGravity(Gravity.RIGHT);
				TextView value = (TextView) view.findViewById(R.id.item_memo_value);
				value.setText(memo.getMemo());

				break;
			case 2:
				layout.setGravity(Gravity.LEFT);
				final ImageView image2 = (ImageView) view.findViewById(R.id.item_memo_image);
				image2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.box));
				image2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						image2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.box_open));
					}
				});

				break;
			case 3:
				layout.setGravity(Gravity.LEFT);
				ImageView image3 = (ImageView) view.findViewById(R.id.item_memo_image);
				presentAnimation(image3);

				break;
			default:
				break;
			}

			// なぜかフラグメント自体の画面更新をしないと動かない...
			getActivity().invalidateOptionsMenu();

			return view;
		}
	}

	// プレゼントを持ってくるアニメーション
	void presentAnimation(ImageView iv) {
		// キャラのアニメーション
		iv.setImageResource(R.drawable.present_animation);
		AnimationDrawable anim = (AnimationDrawable) iv.getDrawable();
		anim.setOneShot(false);
		anim.start();
		// 横から入ってくるアニメーション
		TranslateAnimation translate = new TranslateAnimation(400, 0, 0, 0);
		translate.setDuration(3000);
		translate.setRepeatCount(Animation.INFINITE);
		iv.startAnimation(translate);
	}

}
