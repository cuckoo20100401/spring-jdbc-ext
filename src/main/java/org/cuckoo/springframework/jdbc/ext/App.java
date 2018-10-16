package org.cuckoo.springframework.jdbc.ext;

import org.cuckoo.springframework.jdbc.SQLParser;

import net.sf.jsqlparser.JSQLParserException;

public class App 
{
    public static void main( String[] args ) throws JSQLParserException
    {
    	String sql0 = "select * from sys_user";
    	String sql1 = "select id,username,password from sys_user where age > ?";
    	String sql2 = "select m.*,s1.id as s1id,s1.name as s1name from sys_user m left join sys_role s1 on m.sys_role_id = s1.id where m.age > 20 and s1.name = ?";
    	String sql3 = "select m.*,s1.id as s1id,s1.name as s1name,(select title from article where id = m.id) as articleId from sys_user m left join sys_role s1 on m.sys_role_id = s1.id left join city s2 on m.city_id = s2.id where m.age > 20 and s1.name = ? or m.article_id in (select id from article where sort > m.age) order by m.name,m.age desc";
    	String sql4 = "update sys_user set username = ? where id = ?";
    	String sql5 = "ksks";
    	
        SQLParser sqlParser = new SQLParser("PostgreSQL", sql5, 225, 15);
        System.out.println(sqlParser.getPaginationSQL());
        System.out.println(sqlParser.getTotalSQL());
    }
}
