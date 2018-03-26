package example.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;

import example.model.TableDataResponse;
import example.model.TableDataResponse.ColumnData;
import example.model.TableDataResponse.TableInfo;
import example.util.Log;

public class DB {

	public static final String DB_NAME = "BlueTooth.db";

	public static final String ACCELERATION = "ACCELERATION";
	public static final String ANGLE = "ANGLE";
	public static final String lidu = "lidu";

	public static final String CREATE_ACCELERATION = "create table if not exists ACCELERATION(" + "date text, "
			+ "time integer, " + "acceleration real, " + "PRIMARY KEY(date,time)" + ")";

	public static final String CREATE_ANGLE = "create table if not exists ANGLE(" + "date text, " + "time integer, "
			+ "angle_x real, " + "angle_y real, " + "angle_z real, " + "PRIMARY KEY(date,time)" + ")";

	public static final String CREATE_LIDU = "create table if not exists lidu(" + "date text, " + "time integer, "
			+ "lidu real, " + "PRIMARY KEY(date,time)" + ")";

	private static volatile DB instance;

	private DB() {
		try {
			SqliteHelper sqliteHelper = new SqliteHelper(DB.DB_NAME);
			List<String> createSql = new ArrayList<>();

			createSql.add(CREATE_ACCELERATION);
			createSql.add(CREATE_ANGLE);
			createSql.add(CREATE_LIDU);

			sqliteHelper.executeUpdate(createSql);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static DB getInstance() {

		if (instance == null) {
			synchronized (DB.class) {
				if (instance == null)
					instance = new DB();
			}

		}

		return instance;

	}

	public void insert(String tableName, TableDataResponse tableDataResponse) {

		try {
			SqliteHelper sqliteHelper = new SqliteHelper(DB.DB_NAME);

			List rows = tableDataResponse.rows;
			int count = rows.size();
			List<TableInfo> titles = tableDataResponse.tableInfos;
			List<String> insertSql = new ArrayList<>();

			for (int i = 0; i < count; i++) {

				List<ColumnData> row = (List) rows.get(i);
				String sql = null;

				if (tableName.equals(ANGLE)) {
					sql = "insert into " + tableName + " values('" + row.get(0).value + "'," + row.get(1).value + ","
							+ row.get(2).value + ","+ row.get(3).value + "," + row.get(4).value + ")";
				} else {
					sql = "insert into " + tableName + " values('" + row.get(0).value + "'," + row.get(1).value + ","
							+ row.get(2).value + ")";
				}
				Log.i("sql: " + sql);

				insertSql.add(sql);
			}

			sqliteHelper.executeUpdate(insertSql);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		System.out.println("test");
		DB.connect();
	}

	public static void connect() {
		try {
			// 0 连接SQLite的JDBC
			// String sql = "jdbc:sqlite:Carddddddddddd.db";
			String sql = "jdbc:sqlite:app.db";
			Class.forName("org.sqlite.JDBC");

			// 1 建立一个数据库名zieckey.db的连接，如果不存在就在当前目录下创建之
			Connection conn = DriverManager.getConnection(sql);
			Statement stat = conn.createStatement();

			// 2 创建一个表tbl1，录入数据
			stat.executeUpdate("drop table if exists tbl1;");
			stat.executeUpdate("create table if not exists tbl1(name varchar(20), salary int);");// 创建一个表，两列
			stat.executeUpdate("insert into tbl1 values('ZhangSan',8000);"); // 插入数据
			stat.executeUpdate("insert into tbl1 values('LiSi',7800);");
			stat.executeUpdate("insert into tbl1 values('WangWu',5800);");
			stat.executeUpdate("insert into tbl1 values('ZhaoLiu',9100);");
			ResultSet rs = stat.executeQuery("select * from tbl1;"); // 查询数据
			System.out.println("创建表结构录入数据操作演示：");
			while (rs.next()) { // 将查询到的数据打印出来
				System.out.print("name = " + rs.getString("name") + ", "); // 列属性一
				System.out.println("salary = " + rs.getString("salary")); // 列属性二
			}
			rs.close();

			// 3 修改表结构，添加字段 address varchar(20) default 'changsha';
			stat.executeUpdate("alter table tbl1 add column address varchar(20) not null default 'changsha'; ");// 创建一个表，两列
			stat.executeUpdate("insert into tbl1 values('HongQi',9000,'tianjing');"); // 插入数据
			stat.executeUpdate("insert into tbl1(name,salary) values('HongQi',9000);"); // 插入数据
			rs = stat.executeQuery("select * from tbl1;"); // 查询数据
			System.out.println("表结构变更操作演示：");
			while (rs.next()) { // 将查询到的数据打印出来
				System.out.print("name = " + rs.getString("name") + ", "); // 列属性一
				System.out.print("name = " + rs.getString("name") + ", "); // 列属性二
				System.out.println("address = " + rs.getString("address")); // 列属性三
			}
			rs.close();

			conn.close(); // 结束数据库的连接

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
