package com.buswe.core.dao.jpa;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import com.buswe.core.web.Filterable;
import com.buswe.core.web.MatchType;
import com.buswe.core.web.PropertyFilter;

public class QueryHelper {

	
	public static <T> Specification<T> fetch(final String property)
	{
		  return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				root.fetch(property);
				return null;
			}
			  
		  };
		
	}
	
	public static <T> Specification<T> filterable(Filterable filterable)
	{
		return bySearchFilter(filterable.getFilters());
	}
	
	public static <T> Specification<T> bySearchFilter(final Collection<PropertyFilter> filters)
	{
		  return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Specifications<T> specification = null;
			 if(filters!=null&&filters.size()>0)
			 {
					for (PropertyFilter filter : filters) {
						/**
						 * 如果没有or
						 */
						if(!filter.getField().contains(PropertyFilter.OR_SEPARATOR))
						{
							Specification<T> criterion = filter(filter.getField(),
									filter.getType(), filter.getValue());
							if(specification==null)
							{
								specification = Specifications.where(criterion);
							}
							else
							{
								specification = Specifications.where(specification).and(
										criterion);
							}
						}  
						/**
						 * 有or
						 */
						else
						{
							for (String param :StringUtils.splitByWholeSeparator(filter.getField(),PropertyFilter.OR_SEPARATOR)) {
								Specification<T> criterion = filter(param,
										filter.getType(), filter.getValue());
								if (specification == null) {
									specification = Specifications.where(criterion);
								} else {
									specification = Specifications.where(specification).or(
											criterion);
								}
							}
						}
						
			 
					}
			 }
			 if(specification==null)
				 return null;
				return specification.toPredicate(root, query, cb);
			}
		};
	}
	
	
 
	public static <T> Specification<T>  filter(final String name,
			final MatchType matchType, final Object value) {
		return new Specification<T> ()
		{
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
			 
				String[] names = StringUtils.split(name, ".");
				Path  expression = root.get(names[0]);
				for (int i = 1; i < names.length; i++) {
					expression = expression.get(names[i]);
				}
				switch (matchType) {
				    case EQ:
				 return builder.equal(expression, value);
					case LIKE:
				     return builder.like(expression, "%" +  value + "%");
					case GT:
					{
						return builder.greaterThan(expression, (Comparable) value);
 				}
					case LT:
						return builder.lessThan(expression, (Comparable)  value);
					case GE:
						return builder.greaterThanOrEqualTo(expression, (Comparable)  value);
					case LE:
						return builder.lessThanOrEqualTo(expression, (Comparable)  value);
//					case NI:
//						return DaoUtils.notin(name, value);
//					case NE:
//						return DaoUtils.ne(name, value);
//					case SQL:
//						break;
//					case EX:
//						//return DaoUtils.exist (name, value);
//					case IN:
//						return DaoUtils.in(name, value);
				default:
					break;
					}
				return null;
			}
			
		};
	}
}
