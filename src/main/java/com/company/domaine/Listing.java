package com.company.domaine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.naming.spi.DirStateFactory.Result; 

public class Listing {

	public Map<String, List<String>> getHistory() {
		return history;
	}

	private final Map<String, List<String>> history = new HashMap<>();

	public Listing() {}
	
	public void addOperation(String compte, String operation) {
		
		if(!this.history.containsKey(compte)) {
			this.history.put(compte, new ArrayList<>());
		}
		this.history.get(compte).add(operation);
	}
	
	public List<String> getOperation(String compte) {
		return this.history.get(compte);
	}

	@Override
	public String toString() {
		return "Listing{" +
				"history=" + history +
				'}';
	}
}
