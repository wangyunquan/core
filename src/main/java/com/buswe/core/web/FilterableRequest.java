package com.buswe.core.web;

import java.util.List;

public class FilterableRequest implements Filterable{

	List<PropertyFilter> filters;
	@Override
	public List<PropertyFilter> getFilters() {
		
		return filters;
	}

	@Override
	public String filterableColumns() {
		return null;
	}

	public void setFilters(List<PropertyFilter> filters) {
		this.filters = filters;
	}
	
	

}
