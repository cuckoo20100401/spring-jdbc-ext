package org.cuckoo.springframework.jdbc.ext;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.cuckoo.springframework.jdbc.EntityPersistHelper;
import org.cuckoo.springframework.jdbc.PageInfo;
import org.cuckoo.springframework.jdbc.ext.entity.SysUser;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import net.sf.jsqlparser.JSQLParserException;

public class ExtJdbcTemplateTest {

	public static void main(String[] args) {
		//save();
		//update();
		//find1();
		//find2();
		//find3();
	}
	
	public static void save() {
		SysUser entity = new SysUser();
		entity.setId("abc28");
		entity.setNickname("Joy");
		entity.setAge(30);
		entity.setCreateDate(LocalDate.now());
		entity.setCreateTime(LocalDateTime.now());
		entity.setPrice(new BigDecimal("99999999.315"));
		int affected = DBUtils.getExtJdbcTemplate().save(EntityPersistHelper.build(entity));
		System.out.println("affected: "+affected);
	}
	
	public static void update() {
		SysUser dbEntity = findById("abc81");
		if (dbEntity != null) {
			dbEntity.setAge(35);
			dbEntity.setPrice(new BigDecimal("99999999.315"));
			int affected = DBUtils.getExtJdbcTemplate().update(EntityPersistHelper.build(dbEntity));
			System.out.println("affected: "+affected);
		}
	}
	
	public static void find1() {
		String sql = "SELECT * FROM sys_user";
		try {
			PageInfo<SysUser> pageInfo = DBUtils.getExtJdbcTemplate().find(sql, null, 1, 20, BeanPropertyRowMapper.newInstance(SysUser.class));
			pageInfo.getList().forEach(sysUser -> {
				System.out.println(sysUser.getId()+"\t"+sysUser.getNickname()+"\t"+sysUser.getAge()+"\t"+sysUser.getCreateDate()+"\t"+sysUser.getPrice());
			});
		} catch (DataAccessException | JSQLParserException e) {
			e.printStackTrace();
		}
	}
	
	public static void find2() {
		String sql = "SELECT * FROM sys_user";
		try {
			PageInfo<SysUser> pageInfo = DBUtils.getExtJdbcTemplate().find(sql, null, 1, 20, new RowMapper<SysUser>() {
				@Override
				public SysUser mapRow(ResultSet rs, int rowNum) throws SQLException {
					SysUser entity = new SysUser();
					entity.setId(rs.getString("id"));
					entity.setNickname(rs.getString("nickname"));
					entity.setAge(rs.getInt("age"));
					entity.setCreateDate(rs.getObject("create_date", LocalDate.class));
					entity.setCreateTime(rs.getObject("create_time", LocalDateTime.class));
					entity.setPrice(rs.getBigDecimal("price"));
					return entity;
				}
			});
			pageInfo.getList().forEach(sysUser -> {
				System.out.println(sysUser.getId()+"\t"+sysUser.getNickname()+"\t"+sysUser.getAge()+"\t"+sysUser.getCreateDate()+"\t"+sysUser.getPrice());
			});
		} catch (DataAccessException | JSQLParserException e) {
			e.printStackTrace();
		}
	}
	
	public static void find3() {
		String sql = "SELECT * FROM sys_user";
		try {
			PageInfo<Map<String, Object>> pageInfo = DBUtils.getExtJdbcTemplate().find(sql, null, 1, 20);
			pageInfo.getList().forEach(row -> {
				System.out.println(row.get("id")+"\t"+row.get("nickname")+"\t"+row.get("age")+"\t"+row.get("create_date")+"\t"+row.get("create_time")+"\t"+row.get("price"));
			});
		} catch (DataAccessException | JSQLParserException e) {
			e.printStackTrace();
		}
	}
	
	public static SysUser findById(String id) {
		try {
			String sql = "SELECT * FROM sys_user WHERE id = ?";
			SysUser dbEntity = DBUtils.getExtJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(SysUser.class), id);
			return dbEntity;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

}
