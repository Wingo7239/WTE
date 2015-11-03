package com.yw.service.imp;

import java.io.Serializable;
import java.util.Iterator;

import com.yw.baisc.BasicService;
import com.yw.domain.Contains;
import com.yw.domain.Cuisine;
import com.yw.domain.Steps;
import com.yw.domain.Type;
import com.yw.service.inter.CuisineServiceInter;

import net.sf.json.JSONObject;

public class CuisineService extends BasicService implements CuisineServiceInter {

	
	public Serializable addCuisine(Cuisine cuisine) {
		
//		cuisine.setCid((Integer) add(cuisine));
//		
//		Iterator i = cuisine.getContainses().iterator();
//		while(i.hasNext()){
//			
//		}
//		
//				
//		i = cuisine.getStepses().iterator();
//		while(i.hasNext()){
//			add((Steps)i.next());
//		}
//
//		
//		i = cuisine.getTypes().iterator();
//		while(i.hasNext()){
//			add((Type)i.next());
//		}
		
		
		return null;
	}

	public void deleteCuisine(Cuisine cuisine) {
		// TODO Auto-generated method stub
		
	}
}
