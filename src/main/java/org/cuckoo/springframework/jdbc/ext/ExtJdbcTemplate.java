package org.cuckoo.springframework.jdbc.ext;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cuckoo.springframework.jdbc.EntityPersistHelper;
import org.cuckoo.springframework.jdbc.PageInfo;
import org.cuckoo.springframework.jdbc.SQLParser;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import net.sf.jsqlparser.JSQLParserException;

public class ExtJdbcTemplate extends JdbcTemplate {
	
	private static final Log log = LogFactory.getLog(ExtJdbcTemplate.class);
	
	private String databaseProductName;
	
	public ExtJdbcTemplate(DataSource dataSource) {
		super(dataSource);
		this.databaseProductName = this.getDatabaseProductName();
	}
	
	public <T> int save(EntityPersistHelper<T> entityPersistHelper) {
		
		String sql = entityPersistHelper.getInsertSQL();
		Object[] args = entityPersistHelper.getInsertArgs();
		
		log.debug("SQL[args]: "+sql+" --"+Arrays.asList(args));
		
    	return super.update(sql, args);
    }
    
    public <T> int update(EntityPersistHelper<T> entityPersistHelper) {
    	
    	String sql = entityPersistHelper.getUpdateSQL();
		Object[] args = entityPersistHelper.getUpdateArgs();
		
		log.debug("SQL[args]: "+sql+" --"+Arrays.asList(args));
		
		return super.update(sql, args);
    }
	
	public <T> PageInfo<T> find(String sql, Object[] args, int pageNum, int pageSize, RowMapper<T> rowMapper) {
		
		SQLParser sqlParser = null;
		try {
			sqlParser = new SQLParser(databaseProductName, sql, pageNum, pageSize);
		} catch (JSQLParserException e) {
			throw new DataAccessException(sql, e) {
				private static final long serialVersionUID = 729049149151878652L;
			};
		}
		
		log.debug("PaginationSQL: "+sqlParser.getPaginationSQL());
		log.debug("TotalSQL: "+sqlParser.getTotalSQL());
		
		List<T> list = super.query(sqlParser.getPaginationSQL(), args, rowMapper);
		long total = super.queryForObject(sqlParser.getTotalSQL(), args, Long.class);
		
		return new PageInfo<>(pageNum, pageSize, list, total);
	}
	
	public PageInfo<Map<String, Object>> find(String sql, Object[] args, int pageNum, int pageSize) {
		
		SQLParser sqlParser = null;
		try {
			sqlParser = new SQLParser(databaseProductName, sql, pageNum, pageSize);
		} catch (JSQLParserException e) {
			throw new DataAccessException(sql, e) {
				private static final long serialVersionUID = 729049149151878653L;
			};
		}
		
		log.debug("PaginationSQL: "+sqlParser.getPaginationSQL());
		log.debug("TotalSQL: "+sqlParser.getTotalSQL());
		
		List<Map<String, Object>> list = super.queryForList(sqlParser.getPaginationSQL(), args);
		long total = super.queryForObject(sqlParser.getTotalSQL(), args, Long.class);
		
		return new PageInfo<>(pageNum, pageSize, list, total);
	}

	public String getDatabaseProductName() {
		String databaseProductName = null;
		try {
			Connection conn = this.getDataSource().getConnection();
			DatabaseMetaData databaseMetaData = conn.getMetaData();
			databaseProductName = databaseMetaData.getDatabaseProductName();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return databaseProductName;
	}
}
