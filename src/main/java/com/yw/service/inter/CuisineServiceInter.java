package com.yw.service.inter;

import java.io.Serializable;

import com.yw.baisc.BasicServiceInter;
import com.yw.domain.Cuisine;

public interface CuisineServiceInter extends BasicServiceInter{

	public Serializable addCuisine(Cuisine cuisine);
}
