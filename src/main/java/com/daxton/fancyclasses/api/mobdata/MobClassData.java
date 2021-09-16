package com.daxton.fancyclasses.api.mobdata;

import java.util.HashMap;
import java.util.Map;

public class MobClassData {

	Map<String, Integer> exp_Map = new HashMap<>();

	public MobClassData(){

	}

	public Map<String, Integer> getExp_Map() {
		return exp_Map;
	}

	public void setExp_Map(Map<String, Integer> exp_Map) {
		this.exp_Map = exp_Map;
	}
}
