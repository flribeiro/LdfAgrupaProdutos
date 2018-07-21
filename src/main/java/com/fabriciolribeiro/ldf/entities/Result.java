package com.fabriciolribeiro.ldf.entities;

import java.util.ArrayList;
import java.util.List;

public class Result {
	private List<Grouping> data;

	public Result(List<Grouping> data) {
		this.data = data;
	}
	
	public Result() {
		this.data = new ArrayList<>();
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
	
	public String toString() {
		String representation = "Object Result with " + this.data.size() + " Grouping objects: ";
		for (Grouping p: this.data) {
			representation += p.toString() + " || ";
		}
		return representation;
	}

	public void clear() {
		this.data.clear();
	}
	
}
