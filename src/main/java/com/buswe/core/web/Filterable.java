package com.buswe.core.web;

import java.util.List;


public interface Filterable {
	
	public List<PropertyFilter>  getFilters();
    String filterableColumns();
    public void setFilters(List<PropertyFilter> list);
}
