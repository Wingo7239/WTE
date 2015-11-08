package com.yw.service.inter;

import java.io.Serializable;

import com.yw.baisc.BasicServiceInter;
import com.yw.domain.Cuisine;

import net.sf.json.JSONObject;

public interface CuisineServiceInter extends BasicServiceInter{

	public Serializable addCuisine(JSONObject jsonobj);
	public void updateCuisine(JSONObject jsonobj);
	public void deleteCuisine(JSONObject jsonobj);
}
