package com.buswe.core.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

public class FilterableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return Filterable.class.equals(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		FilterColum filterColum = getParamAnnotation(methodParameter);
		Filterable filterable=new FilterableRequest();
		
		 if(filterColum==null)
		 {}
		 else
		 {
		 
			 HttpServletRequest request=	 (HttpServletRequest)webRequest.getNativeRequest();
			    Map<String, Object> filterParamMap = WebUtils.getParametersStartingWith(request, PropertyFilter.FILTER_STRING);
			    List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();
			    for (Map.Entry<String, Object> entry : filterParamMap.entrySet())
			    {
			      String filterName = (String)entry.getKey();
			      String value = (String)entry.getValue();
			      if (StringUtils.isNotBlank(value))
			      {
			        String matchTypeStr = StringUtils.substringBefore(filterName, PropertyFilter.FILTER_SIGN);
			        String realFilterName = StringUtils.substringAfter(filterName, PropertyFilter.FILTER_SIGN);
			        PropertyFilter filter = new PropertyFilter(realFilterName, (MatchType)Enum.valueOf(MatchType.class, matchTypeStr), value);
			        request.setAttribute(filterName.replace(".", PropertyFilter.FILTER_SIGN), value);
			        filterList.add(filter);
			      }
			    }
			    filterable.setFilters(filterList);
		 }
		
		return filterable;
	}

	private FilterColum getParamAnnotation(MethodParameter methodParameter) {
		if (methodParameter.hasParameterAnnotation(FilterColum.class)) {
		 
			FilterColum defaults = methodParameter.getParameterAnnotation(FilterColum.class);
			
			return defaults;
		}
		return null;
	}

}
