package org.cuckoo.springframework.jdbc;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.util.SelectUtils;

public class SQLParser {
	
	private String paginationSQL;
	private String totalSQL;
	
	public void test1(String sql) throws JSQLParserException {
		
		Statement statement = CCJSqlParserUtil.parse(sql);
		if (statement instanceof Select) {
			Select selectStatement = (Select) statement;
			PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
			
			List<SelectItem> selectItems = new ArrayList<>();
			plainSelect.setSelectItems(selectItems);
			
			SelectVisitor iii = new SelectVisitorAdapter();
			SetOperationList aaa = new SetOperationList();
			
			selectStatement.getSelectBody().accept(iii);
			
		}
	}

	public void test(String sql) throws JSQLParserException {
		
		String srcSql = null;
		String srcSqlSelectItems = null;
		String srcSqlFromItem = null;
		String srcSqlJoins = null;
		String srcSqlWhere = null;
		String srcSqlOrderBy = null;
		
		// parse
		Statement statement = CCJSqlParserUtil.parse(sql);
		if (statement instanceof Select) {
			Select selectStatement = (Select) statement;
			PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
			//srcSql
			srcSql = selectStatement.toString();
			//srcSqlSelectItems
			StringBuilder sb = new StringBuilder();
			List<SelectItem> selectItems = plainSelect.getSelectItems();
			for (SelectItem selectItem: selectItems) {
				if (sb.length() == 0) {
					sb.append(selectItem.toString());
				} else {
					sb.append(", ").append(selectItem.toString());
				}
			}
			srcSqlSelectItems = sb.toString();
			//srcSqlFromItem
			srcSqlFromItem = plainSelect.getFromItem().toString();
			//srcSqlJoins
			List<Join> joins = plainSelect.getJoins();
			for (Join join: joins) {
				if (srcSqlJoins == null) {
					srcSqlJoins = join.toString();
				} else {
					srcSqlJoins += " "+join.toString();
				}
			}
			//srcSqlWhere
			srcSqlWhere = plainSelect.getWhere().toString();
			//srcSqlOrderBy
			List<OrderByElement> orderByElements = plainSelect.getOrderByElements();
			for (OrderByElement orderByElement: orderByElements) {
				if (srcSqlOrderBy == null) {
					srcSqlOrderBy = orderByElement.toString();
				} else {
					srcSqlOrderBy += ","+orderByElement.toString();
				}
			}
			//paging
			//plainSelect.setLimit(limit);
		}
		
		// build
		this.totalSQL = new StringBuilder()
				.append("SELECT COUNT(*) FROM")
				.append(srcSqlFromItem)
				.append(srcSqlJoins)
				.append(srcSqlWhere)
				.append(srcSqlOrderBy)
				.toString();
	}
	
}
