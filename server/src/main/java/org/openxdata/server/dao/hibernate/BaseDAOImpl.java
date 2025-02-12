package org.openxdata.server.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openxdata.server.admin.model.Editable;
import org.openxdata.server.admin.model.User;
import org.openxdata.server.admin.model.paging.FilterComparison;
import org.openxdata.server.admin.model.paging.FilterConfig;
import org.openxdata.server.admin.model.paging.PagingLoadConfig;
import org.openxdata.server.admin.model.paging.PagingLoadResult;
import org.openxdata.server.dao.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;

abstract class BaseDAOImpl<T extends Editable> extends GenericDAOImpl<T, Long> implements BaseDAO<T> {

    @Autowired
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
    
	@Override
	public List<T> searchByPropertyEqual(String property, Object value){
		Search search = new Search();
		search.addFilterEqual(property, value);
		return search(search);
	}

	@Override
    @SuppressWarnings("unchecked")
	public T searchUniqueByPropertyEqual(String property, Object value) {
        Search search = new Search();
        search.addFilterEqual(property, value);
        return (T) searchUnique(search);
    }
	
	@Override
	public PagingLoadResult<T> findAllByPage(PagingLoadConfig pagingLoadConfig, String defaultSortField) {
		Search userSearch = getSearchFromLoadConfig(pagingLoadConfig, defaultSortField);
	    SearchResult<T> result = searchAndCount(userSearch);
	    return getPagingLoadResult(pagingLoadConfig, result);
	}
	
	@SuppressWarnings("unchecked")
    protected PagingLoadResult<User> findAllUsersByPage(PagingLoadConfig loadConfig, boolean mapped, Class<T> entityClass, Integer id) {
		// filter query
        StringBuilder filterQuery = new StringBuilder();
        for (FilterConfig filter : loadConfig.getFilters()) {
        	filterQuery.append(" and user.");
        	filterQuery.append(filter.getField());
        	filterQuery.append(" like '%");
        	filterQuery.append(filter.getValue());
        	filterQuery.append("%'");
        }
        StringBuilder whereQuery = new StringBuilder();
        whereQuery.append(" where item.id = :itemId");
        if (mapped) {
        	whereQuery.append(" and user in elements(item.users)");
        } else {
        	whereQuery.append(" and user not in elements(item.users)");
        }
        whereQuery.append(filterQuery);
        Query query = getSession().createQuery(
					"select distinct user from "+entityClass.getSimpleName()+" as item, User as user" +
					 whereQuery + " order by user.name");
        Query countQuery = getSession().createQuery(
				"select count(user) from "+entityClass.getSimpleName()+" as item, User as user" +
				 whereQuery);
		query.setInteger("itemId", id);
		countQuery.setInteger("itemId", id);
		// paging
		query.setFirstResult(loadConfig.getOffset());
        query.setFetchSize(loadConfig.getLimit());
        query.setMaxResults(loadConfig.getLimit());
        
        // execute
	    List<User> result = query.list();
	    Long count = (Long)countQuery.uniqueResult();
	    
	    // convert to PagingLoadResult
	    PagingLoadResult<User> pagingResult = new PagingLoadResult<User>(result, loadConfig.getOffset(), result.size(), count.intValue());
	    
	    return pagingResult;
	}
	
	/**
	 * @param entityClass the class to search for
	 * @param loadConfig
	 * @return the configured Search
	 */
	public Search getSearchFromLoadConfig(PagingLoadConfig loadConfig, String defaultSortField) {

		Search search = new Search();
		
		// sorting
		String sortField = loadConfig.getSortField();
		sortField = (sortField == null ? defaultSortField : sortField);
		if (sortField != null && loadConfig.isSortDescending() != null) {
			search.addSort(sortField, loadConfig.isSortDescending());
		}
		
		// filtering
		List<FilterConfig> filterConfigs = loadConfig.getFilters();
		if (filterConfigs != null) {
			Filter[] filters = new Filter[filterConfigs.size()];
			for (int i=0, j=filterConfigs.size(); i<j; i++) {
				FilterConfig filter = filterConfigs.get(i);
				if (filter.getComparison() == FilterComparison.LIKE) {
					filters[i] = Filter.ilike(filter.getField(), "%" + filter.getValue() + "%");
				} else if (filter.getComparison() == FilterComparison.GREATER_THAN) {
					filters[i] = Filter.greaterThan(filter.getField(), filter.getValue());
				} else if (filter.getComparison() == FilterComparison.LESS_THAN) {
					filters[i] = Filter.lessThan(filter.getField(), filter.getValue());
				} else if (filter.getComparison() == FilterComparison.EQUAL_TO) {
					filters[i] = Filter.equal(filter.getField(), filter.getValue());
				}
			}
			search.addFilterAnd(filters); 
		}

		// paging
		search.setMaxResults(loadConfig.getLimit());
		search.setFirstResult(loadConfig.getOffset());

		return search;
	}
	
	protected PagingLoadResult<T> getPagingLoadResult(PagingLoadConfig loadConfig, SearchResult<T> searchResult) {
		List<T> list = searchResult.getResult();
		int totalNum = searchResult.getTotalCount();
		int offset = loadConfig == null ? 0 : loadConfig.getOffset();
		return new PagingLoadResult<T>(list, offset, list.size(), totalNum);
	}
}
