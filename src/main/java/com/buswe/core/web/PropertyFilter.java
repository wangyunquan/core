package com.buswe.core.web;

public class PropertyFilter {

	public static final String FILTER_STRING="filter_";
	public static final String FILTER_SIGN="_";
	public static final String OR_SEPARATOR = "_OR_";
	  private String field;
	  private Object value;
	  private MatchType type;
	  
	  public PropertyFilter(String name, Object value)
	  {
	    this.field = name;
	    this.value = value;
	    this.type = MatchType.EQ;
	  }
	  
	  public PropertyFilter(String name, MatchType matchType, Object value)
	  {
	    this.field = name;
	    this.value = value;
	    this.type = matchType;
	  }

	public String getField() {
		return field;
	}

	public Object getValue() {
		return value;
	}

	public MatchType getType() {
		return type;
	}
	  
	  
}
