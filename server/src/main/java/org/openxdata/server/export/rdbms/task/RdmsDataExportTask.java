package org.openxdata.server.export.rdbms.task;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.Validate;
import org.openxdata.server.admin.model.FormData;
import org.openxdata.server.admin.model.FormDefVersion;
import org.openxdata.server.admin.model.Setting;
import org.openxdata.server.admin.model.SettingGroup;
import org.openxdata.server.dao.RdmsExporterDAO;
import org.openxdata.server.dao.jdbc.JdbcRdmsExporterDAO;
import org.openxdata.server.export.ExportConstants;
import org.openxdata.server.export.rdbms.engine.DataQuery;
import org.openxdata.server.export.rdbms.engine.RdmsEngine;
import org.openxdata.server.export.rdbms.engine.TableQuery;
import org.openxdata.server.service.DataExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class represents the task that handles the export of collected data from XML to RDBMS
 * @author Tumwebaze Charles
 * @author dagmar@cell-life.org.za
 */
@Component("rdmsDataExportTask")
public class RdmsDataExportTask {
    
	@Autowired
	private DataExportService dataExportService;
	
    private static Logger log = LoggerFactory.getLogger(RdmsDataExportTask.class);
    
    private String serverName= "";
    private String portNumber = "";
    private String databaseName= "";
    private String dbUsername = "";
    private String dbPassword = "";
    
    /** cache of form definitions */
    private Hashtable<Integer, FormDefVersion> formDefCache = new Hashtable<Integer, FormDefVersion>();
    /** cache of SQL to create tables */
    private Hashtable<FormDefVersion, List<TableQuery>> tableQueryCache = new Hashtable<FormDefVersion, List<TableQuery>>();
    /** dao used to create exported tables and insert/update exported data  */
    RdmsExporterDAO exporter;
    /** contains all the date related settings (required to format submitted data) */
    private static SettingGroup dateSettingGroup;

    /** thread pool to manage the direct invocation of the task (without quartz) */
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(5,10, 3600, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    
    /**
	 * Exports the specified FormData
	 * @param formData FormData to exports
	 */
	public void exportFormData(FormData formData) {
        Validate.notNull(formData);
        FormDefVersion formDefVersion = getFormDefVersion(formData);

        tpe.execute(new ExportFormDataThread(formData, formDefVersion));
	}
	
	 /**
	 * Deletes exported data related to the specified FormData
	 * @param formData FormData to exports
	 */
	public void deleteFormData(FormData formData) {
        Validate.notNull(formData);
        FormDefVersion formDefVersion = getFormDefVersion(formData);

        tpe.execute(new DeleteFormDataThread(formData, formDefVersion));
	}

	/**
	 * Gets the FormDefVersion for the FormData
	 * 
	 * If the FormDefVersion is not in the cache it is loaded and added
	 * to the cache.
	 * 
	 * @param formDefVersion
	 * @return
	 */
	private FormDefVersion getFormDefVersion(FormData formData) {
		FormDefVersion formDefVersion = formDefCache.get(formData.getFormDefVersionId());
        if (formDefVersion == null) {
            formDefVersion = dataExportService.getFormDefVersion(formData.getFormDefVersionId());
            formDefCache.put(formDefVersion.getId(), formDefVersion);
        }
		return formDefVersion;
	}
	
	/**
	 * Gets the TableQuery objects for the formDefVersion.
	 * 
	 * If the objects are not in the cache they are created and added
	 * to the cache.
	 * 
	 * @param formDefVersion
	 * @return
	 */
	private List<TableQuery> getTableQeuries(FormDefVersion formDefVersion) {
		List<TableQuery> tables = tableQueryCache.get(formDefVersion);
		if (tables == null) {
		    // if the tables weren't already cached
		    tables = RdmsEngine.getStructureSql(formDefVersion.getXform());
		    tableQueryCache.put(formDefVersion, tables);
		}
		return tables;
	}
	
	/**
	 * Runnable class to handle executing the Task outside of Quartz
	 * @author dagmar@cell-life.org.za
	 */
	class ExportFormDataThread implements Runnable {
	    FormData formData;
	    FormDefVersion formDefVersion;
	    public ExportFormDataThread(FormData formData, FormDefVersion formDefVersion) {
	        this.formData = formData;
	        this.formDefVersion = formDefVersion;
	    }
        @Override
		public void run() {
            exportFormData(formData, formDefVersion);
        }
	}
	
	/**
	 * Runnable class to handle executing the Task outside of Quartz
	 * @author simon@cell-life.org
	 */
	class DeleteFormDataThread implements Runnable {
	    FormData formData;
	    FormDefVersion formDefVersion;
	    public DeleteFormDataThread(FormData formData, FormDefVersion formDefVersion) {
	        this.formData = formData;
	        this.formDefVersion = formDefVersion;
	    }
        @Override
		public void run() {
            deleteFormData(formData, formDefVersion);
        }
	}
	
	// easy to test method
	protected void exportFormData(FormData formData, FormDefVersion formDefVersion) {
	    try {
    	    boolean newData = false;
            if (formDefVersion != null) {
                 //creating the structure
                List<TableQuery> tables = getTableQeuries(formDefVersion);
                for (TableQuery table : tables) {
                	if (!exporter.tableExists(table.getTableName())) {
                		String sqlStatement = table.getSql();
                		log.debug("Creating table '"+table+"'. SQL="+sqlStatement);
                		exporter.executeSql(sqlStatement);
                		newData = true; // if we just inserted the table, there is no data
                	}
                }
                
                
                // check if this an insert or update (only check the parent table)
                // note: only check if we didn't just create the table
                if (!newData) {
                    for (TableQuery table : tables) {
                        newData = !exporter.dataExists(formData.getId(), table.getTableName());
                        log.debug("Is data with id "+formData.getId()+" in table '"+table.getTableName()+"' new? "+newData);
                    }
                }
                
                // inserting or updating the data
                List<DataQuery> dataSQL = RdmsEngine.getDataSql(formDefVersion.getXform(), formData, !newData);
                exporter.executeSql(dataSQL);
            }
            
            // export successful if we are still here...
            dataExportService.setFormDataExported(formData, ExportConstants.EXPORT_BIT_RDBMS);
        } catch(Exception ex) {
            log.error("Exception caught while attempting export of form data with id '"+formData.getId()+"'", ex);
        }
	}

	protected void deleteFormData(FormData formData, FormDefVersion formDefVersion) {
		try {
			if (formDefVersion != null) {
				List<TableQuery> tables = getTableQeuries(formDefVersion);
				for (TableQuery table : tables) {
					int rows = exporter.deleteData(formData.getId(), table.getTableName());
					log.debug(rows + " deleted from table " + table.getTableName());
					exporter.deleteTableIfEmtpy(table.getTableName());
				}
			}
		} catch (Exception ex) {
			log.error(
					"Exception caught while attempting to delete form data with id '"
							+ formData.getId() + "'", ex);
		}
	}

	protected void setRdmsExporterDAO(RdmsExporterDAO exporter) {
	    this.exporter = exporter;
	}
	
	protected void setDataExportService(DataExportService dataExportService) {
		this.dataExportService = dataExportService;
	}
	
	public void init() {
		exporter = new JdbcRdmsExporterDAO(getConnectionURL(), databaseName);
		dateSettingGroup = dataExportService.getDateSettings();
	}
	
	public static String getDateSetting(String name, String defaultValue) {
		if (dateSettingGroup != null) {
			List<Setting> settings = dateSettingGroup.getSettings();
			for (Setting s : settings) {
				if (s.getName().equalsIgnoreCase(name)) {
					log.debug("Found "+name+" setting="+s.getValue());
					return s.getValue();
				}
			}
		}
		log.warn("Couldn't find any setting with name '"+name+"'. Using default value="+defaultValue);
		return defaultValue;
	}
	
	public static String getSubmitDateSetting() {
		return getDateSetting("submitDateFormat", "yyyy-MM-dd");
	}
	
	public static String getSubmitDateTimeSetting() {
		return getDateSetting("submitDateTimeFormat", "yyyy-MM-dd hh:mm:ss a");
	}
		
	private String getConnectionURL(){
		return "jdbc:mysql://"+this.serverName+":"+this.portNumber+"/"+this.databaseName+"?user="+
				this.dbUsername+"&password="+this.dbPassword+"&autoReconnect=true";
	}

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
