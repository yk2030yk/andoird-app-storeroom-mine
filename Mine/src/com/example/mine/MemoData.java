package com.example.mine;

public class MemoData {
	int id;
	String memo;
	String detail=null;
	int checked;
	int del;
	int tid;
	int memoKind=1;
	
	MemoData(int id, String memo, int checked, int del, int tid, int kind){
		this.id=id;
		this.memo=memo;
		this.checked=checked;
		this.del=del;
		this.tid=tid;
		this.memoKind=kind;
	}
	
	void setDetail(String d){
		this.detail=d;
	}
	
	int getId(){
		return id;
	}
	
	String getMemo(){
		return memo;
	}
	
	String getDetail(){
		return detail;
	}
	
	int getChecked(){
		return checked;
	}
	
	int getDel(){
		return del;
	}
	
	int getTid(){
		return tid;
	}
}
