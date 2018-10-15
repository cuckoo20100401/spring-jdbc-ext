package org.cuckoo.springframework.jdbc;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.SelectUtils;

public class SQLParser {

	public void test() throws JSQLParserException {
		
		String sql = "select * from sys_user";
		
		Statement statement = CCJSqlParserUtil.parse(sql);
		if (statement instanceof Select) {
			Select selectStatement = (Select) statement;
			System.out.println(selectStatement.toString());
		}
	}
	
}
