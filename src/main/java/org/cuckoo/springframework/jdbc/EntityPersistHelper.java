package org.cuckoo.springframework.jdbc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.cuckoo.universal.utils.db.StringUtils;

public class EntityPersistHelper<T> {
	
	private T entity;
	private String insertSQL;
	private String updateSQL;
	private Object[] insertArgs;
	private Object[] updateArgs;
	
	public EntityPersistHelper(T entity) {
		this.entity = entity;
	}
	
	public static <T> EntityPersistHelper<T> build(T entity) {
		return new EntityPersistHelper<T>(entity);
	}

	public void generateInsertSQLAndArgs() throws Exception {
		
		String insertSQL = "INSERT INTO {SQLTableName} ({SQLTableColumns}) VALUES ({SQLTableColumnValueWildcards})";
		List<Object> insertArgs = new ArrayList<>();
		
		StringBuilder sqlTableColumns = new StringBuilder();
		StringBuilder sqlTableColumnValueWildcards = new StringBuilder();
		Class<? extends Object> clazz = this.entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
    	for (Field field: fields) {
    		if (field.getType().getModifiers() == 17 || (field.getType().getModifiers() == 1 && field.getType().getName().equals("java.math.BigDecimal"))) {
    			sqlTableColumns.append(", ").append(StringUtils.fromEntityPropertyNameToTableColumnName(field.getName()));
    			sqlTableColumnValueWildcards.append(", ").append("?");
    			insertArgs.add(clazz.getDeclaredMethod(StringUtils.fromEntityPropertyNameToGetMethodName(field.getName())).invoke(this.entity));
    		}
    	}
    	sqlTableColumns = sqlTableColumns.delete(0, 2);
    	sqlTableColumnValueWildcards = sqlTableColumnValueWildcards.delete(0, 2);
    	
    	insertSQL = insertSQL.replace("{SQLTableName}", StringUtils.fromEntityNameToTableName(clazz.getSimpleName()));
    	insertSQL = insertSQL.replace("{SQLTableColumns}", sqlTableColumns);
    	insertSQL = insertSQL.replace("{SQLTableColumnValueWildcards}", sqlTableColumnValueWildcards);
    	this.insertSQL = insertSQL;
    	this.insertArgs = insertArgs.toArray();
	}
	
	public void generateUpdateSQLAndArgs() throws Exception {
		
		String updateSQL = "UPDATE {SQLTableName} SET {SQLTableColumnsAndValueWildcards} WHERE id = ?";
		List<Object> updateArgs = new ArrayList<>();
		
		StringBuilder SQLTableColumnsAndValueWildcards = new StringBuilder();
		Class<? extends Object> clazz = this.entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
    	for (Field field: fields) {
    		if (field.getType().getModifiers() == 17 || (field.getType().getModifiers() == 1 && field.getType().getName().equals("java.math.BigDecimal"))) {
    			if (!field.getName().equals("id")) {
    				SQLTableColumnsAndValueWildcards.append(", ").append(StringUtils.fromEntityPropertyNameToTableColumnName(field.getName())).append(" = ?");
        			updateArgs.add(clazz.getDeclaredMethod(StringUtils.fromEntityPropertyNameToGetMethodName(field.getName())).invoke(this.entity));
    			}
    		}
    	}
    	SQLTableColumnsAndValueWildcards = SQLTableColumnsAndValueWildcards.delete(0, 2);
    	updateArgs.add(clazz.getDeclaredMethod(StringUtils.fromEntityPropertyNameToGetMethodName("id")).invoke(this.entity));
    	
    	updateSQL = updateSQL.replace("{SQLTableName}", StringUtils.fromEntityNameToTableName(clazz.getSimpleName()));
    	updateSQL = updateSQL.replace("{SQLTableColumnsAndValueWildcards}", SQLTableColumnsAndValueWildcards);
    	this.updateSQL = updateSQL;
    	this.updateArgs = updateArgs.toArray();
	}
	
	public String getInsertSQL() {
		if (this.insertSQL == null) {
			try {
				this.generateInsertSQLAndArgs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.insertSQL;
	}
	
	public Object[] getInsertArgs() {
		if (this.insertArgs == null) {
			try {
				this.generateInsertSQLAndArgs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.insertArgs;
	}

	public String getUpdateSQL() {
		if (this.updateSQL == null) {
			try {
				this.generateUpdateSQLAndArgs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.updateSQL;
	}

	public Object[] getUpdateArgs() {
		if (this.updateArgs == null) {
			try {
				generateUpdateSQLAndArgs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.updateArgs;
	}
}
