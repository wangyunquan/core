package com.buswe.core.dao.jpa;

import com.buswe.core.web.Filterable;
import com.buswe.core.web.MatchType;
import com.buswe.core.web.PropertyFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.*;
import java.util.Collection;

public class QueryHelper {


    public static <T> Specification<T> fetch(final String property) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                root.fetch(property);
                return null;
            }

        };

    }

    public static <T> Specification<T> filterable(Filterable filterable) {
        return bySearchFilter(filterable.getFilters());
    }


    public static <T> Specification<T> bySearchFilter(final Collection<PropertyFilter> filters) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                Specifications<T> specification = null;
                if (filters != null && filters.size() > 0) {
                    for (PropertyFilter filter : filters) {
                        /**
                         * 如果没有or
                         */
                        if (!filter.getField().contains(PropertyFilter.OR_SEPARATOR)) {
                            Specification<T> criterion = filter(filter.getField(),
                                    filter.getType(), filter.getValue());
                            if (specification == null) {
                                specification = Specifications.where(criterion);
                            } else {
                                specification = Specifications.where(specification).and(
                                        criterion);
                            }
                        }
                        /**
                         * 有or
                         */
                        else {
                            for (String param : StringUtils.splitByWholeSeparator(filter.getField(), PropertyFilter.OR_SEPARATOR)) {
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
                if (specification == null)
                    return null;
                return specification.toPredicate(root, query, cb);
            }
        };
    }


    public static <T> Specification<T> filter(final String name,
                                              final MatchType matchType, final Object value) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                //TODO 将Value转换为对应的类型
                Path expression = getPath(root, name);
                switch (matchType) {
                    case EQ:
                        return builder.equal(expression, value);
                    case LIKE:
                        return builder.like(expression, "%" + value + "%");
                    case GT:
                        return builder.greaterThan(expression, (Comparable) value);
                    case LT:
                        return builder.lessThan(expression, (Comparable) value);
                    case GE:
                        return builder.greaterThanOrEqualTo(expression, (Comparable) value);
                    case LE:
                        return builder.lessThanOrEqualTo(expression, (Comparable) value);
                    case IN:
                        return builder.in(expression).value(value);
                    default:
                        return builder.disjunction();
                }
            }

        };


    }

    public static Path getPath(Root<?> root, String property) {
        //获取 path
        Path path = root;
        if (property.contains(".")) {
            for (String name : property.split("\\.")) {

                path = path.get(name);
                if (path.getModel() == null && path.getParentPath() == root) {
                    if (root.getModel().getAttribute(name).isCollection()) {
                        path = root.join(name);
                    }
                }
            }
        } else {
            path = root.get(property);
        }
        return path;
    }
}
