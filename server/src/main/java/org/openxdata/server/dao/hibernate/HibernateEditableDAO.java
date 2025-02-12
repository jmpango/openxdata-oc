package org.openxdata.server.dao.hibernate;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.openxdata.server.admin.model.Editable;
import org.openxdata.server.admin.model.FormDataHeader;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.FormDefVersion;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.admin.model.paging.FilterComparison;
import org.openxdata.server.admin.model.paging.FilterConfig;
import org.openxdata.server.admin.model.paging.PagingLoadConfig;
import org.openxdata.server.dao.EditableDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * Provides a hibernate implementation of the study manager data access interface.
 * 
 * @author daniel
 * @author dagmar@cell-life.org.za
 * @author Ronald
 *
 */
@Repository("studyManagerDAO")
public class HibernateEditableDAO extends BaseDAOImpl<FormDef> implements EditableDAO {
	
	/** The logger*/
	private Logger log = LoggerFactory.getLogger(HibernateEditableDAO.class);

	@Override
	public Boolean hasEditableData(Editable item) {
		Boolean hasData = false;
		if (item != null)
			hasData = checkEditableForData(item);

		return hasData;
	}   
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getResponseData(String formBinding, String[] questionBindings, PagingLoadConfig pagingLoadConfig) {
		StringBuilder sql = new StringBuilder();
        sql.append("select openxdata_form_data_id,");
        sql.append(StringUtils.arrayToCommaDelimitedString(questionBindings));
        sql.append(" from ");
        sql.append(formBinding);
        if (pagingLoadConfig.getSortField() != null && !pagingLoadConfig.getSortField().trim().equals("")) {
            sql.append(" order by ");
            sql.append(pagingLoadConfig.getSortField());
            if (pagingLoadConfig.isSortDescending()) sql.append(" DESC");
        }
        // FIXME... can't filter on date because it hasn't been exported
        StringBuilder filterSql = new StringBuilder();
        List<FilterConfig> filters = pagingLoadConfig.getFilters();
        if (filters != null) {
        	for (FilterConfig filter : filters) {
	        	if (filterSql.length() != 0) {
	        		filterSql.append(" and");
	        	}
		        filterSql.append(getFilterSQL(filter));
        	}
	        if (filterSql.length() != 0) {
	        	sql.append("where");
	        	sql.append(filterSql);
	        }
        }
        log.debug("executing sql: "+sql+" firstResult="+pagingLoadConfig.getOffset()+" maxResults="+pagingLoadConfig.getLimit());
        // execute + limit results for page
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (filters != null) {
        	for (FilterConfig filter : filters) {
        		query.setParameter(filter.getField(), filter.getValue());
        	}
        }
        // FIXME: to support BLOB questions (e.g. IMAGE,VIDEO,SOUND) we will need to add the following code:
        // .addScalar(questionBinding, Hibernate.BLOB)
        // this will require knowing the multimedia types -> error seen: "No Dialect mapping for JDBC type: -4"
        query.setFirstResult(pagingLoadConfig.getOffset());
        query.setFetchSize(pagingLoadConfig.getLimit());
        query.setMaxResults(pagingLoadConfig.getLimit());
        List<Object[]> data = (List<Object[]>)query.list();
		return data;
	}
	
	public String getFilterSQL(FilterConfig config) {
		StringBuilder filterSql = new StringBuilder();
        filterSql.append(config.getField());
        if (config.getComparison() == null) {
        	filterSql.append(" like");
        } else if (config.getComparison() == FilterComparison.EQUAL_TO) {
        	filterSql.append(" =");
        } else if (config.getComparison() == FilterComparison.LESS_THAN) {
        	filterSql.append(" <");
        } else if (config.getComparison() == FilterComparison.GREATER_THAN) {
        	filterSql.append(" >");
        }
        filterSql.append(" :");
        filterSql.append(config.getField());
        return filterSql.toString();
	}

	@Override
	public BigInteger getNumberOfResponses(String formBinding) {
		SQLQuery countQuery = getSession().createSQLQuery("select count(*) from "+formBinding);
	    BigInteger count = (BigInteger)countQuery.uniqueResult();
		return count;
	}

    /**
	 * Builds the SQL for checking if a FormDef has data.
	 * 
	 * @return A prepared SQL Statement.
	 */
	private static String buildFormSQL() {
		String sql = "select fdv.name From form_definition_version as fdv inner join form_definition as fd ON"
				+ " fdv.form_definition_id = fd.form_definition_id"
				+ " inner join form_data as fdt on fdt.form_definition_version_id = fdv.form_definition_version_id "
				+ "WHERE fd.form_definition_id = :id";
		return sql.trim();
	}

	/**
	 * Builds the SQL for checking if a StudyDef has data.
	 * 
	 * @return A prepared SQL Statement.
	 */
	private static String buildStudySQL() {
		String sql = "select fdv.name From form_definition_version as fdv inner join form_definition as fd ON "
				+ " fdv.form_definition_id = fd.form_definition_id"
				+ " inner join form_data as fdt on fdt.form_definition_version_id = fdv.form_definition_version_id "
				+ " inner JOIN study as s on s.study_id = fd.study_id WHERE s.study_id = :id";
		
		return sql.trim();
	}

	/**
	 * Check the given <code>Editable</code> for data.
	 * 
	 * @param item
	 *            <code>Editable</code> item to check for data.
	 * @param sessionFactory
	 *            <code>SessionFactory</code> to use.
	 * @return <code>Boolean True/False</code>
	 *             <code>if(item == null)</code>
	 */
	private Boolean checkEditableForData(Editable item) {
		String SQL = getSqlFormEditableDataCheck(item);
		int editableId = item.getId();
		return runSQLToAscertainDataExistence(SQL, editableId);
	}

	private String getSqlFormEditableDataCheck(Editable item) {
		String SQL = null;
		if (item instanceof StudyDef) {
			SQL = buildStudySQL();
		} else if (item instanceof FormDef) {
			SQL = buildFormSQL();
		} else if (item instanceof FormDefVersion) {
			SQL = "select form_definition_version_id From form_data Where form_definition_version_id = :id";
		}
		return SQL;
	}

	/**
	 * Runs a given <code>SQL</code> statement within a given
	 * <code>sessionFactory</code>.
	 * 
	 * @param SQL
	 *            <code>SQL</code> to run.
	 * @param editableId
	 *            Optional <code>Id</code> for the <code>Editable</code>.
	 * @param sessionFactory
	 *            <code>sessionFactory</code> to create session in which the SQL
	 *            will be run.
	 * 
	 * @return <code> Boolean</code>
	 */
	@SuppressWarnings("unchecked")
	private Boolean runSQLToAscertainDataExistence(String SQL, int editableId) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(SQL);
		query.setInteger("id", editableId);
		query.setFirstResult(0);
		query.setFetchSize(1);
		List<FormDataHeader> items = query.list();
		if (items != null) {
			if (items.size() > 0)
				return true;
		}
		return false;
	} 

}
