package com.fabriciolribeiro.ldf.entities;

import java.util.List;

public class Result {
	private List<Grouping> data;

	public Result(List<Grouping> data) {
		this.data = data;
	}
	
	public Result() {
		
	}

	public List<Grouping> getData() {
		return data;
	}

	public void setData(List<Grouping> data) {
		this.data = data;
	}
	
	public void addGrouping(Grouping grouping) {
		this.data.add(grouping);
	}
	
	public void join(Result result) {
		this.data.addAll(result.getData());
	}
	
}