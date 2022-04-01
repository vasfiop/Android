package com.example.thegame;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUnitTest {
    @Test
    public void jdbc() {
//        添加JDBC驱动程序 添加依赖
        new Thread(new Runnable() {
            @Override
            public void run() {
                JDBC dButil = new JDBC();
                System.out.println(dButil.getList("SELECT * from user"));
            }
        }).start();
    }

    class JDBC {
        // 数据库名字
        private String DBname = "meal";
        // 数据库驱动程序
        private String driver;
        // 数据库连接信息
        private String url;
        // 连接数据库的用户名
        private String username;
        // 连接数据库的密码
        private String password;
        // 连接对象
        private Connection con;
        // 预编译语句对象
        private PreparedStatement pstmt;
        // 查询结果分页时，每页显示记录数。
        public static final long PAGE_REC_NUM = 8;

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public JDBC() {
            driver = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://10.197.47.178:3306/" + DBname;
            username = "root";
            password = "root"; // 数据库密码可变
        }

        // 初始化方法，加载驱动程序，获得数据库的连接对象。
        public void init() {
            try {
                Class.forName(driver);
                System.out.println("数据库加载成功");
                con = DriverManager.getConnection(url, username, password);
                System.out.println("数据库连接成功");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("数据库加载失败");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("数据库连接失败");
            }

        }

        // 数据库操作对象的关闭
        private void close() {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // 为预编译的SQL语句中的占位符?设置值，值被放在字符串数组params中。
        private void setParams(Object[] params) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    try {
                        pstmt.setObject(i + 1, params[i]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

            }
        }

        // 执行更新类（增删改）的SQL语句，SQL语句中含有占位符。
        public int update(String sql, Object[] params) {
            int result = 0;
            init();
            try {
                pstmt = con.prepareStatement(sql);
                setParams(params);
                result = pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                close();
            }
            return result;
        }

        // 执行更新类（增删改）的SQL语句，SQL语句中不含有占位符。
        public int update(String sql) {
            return update(sql, null);
        }

        // 执行返回多条记录的查询操作，结果被封装到List中。
        public List<Map<String, Object>> getList(String sql, Object[] params) {
            List<Map<String, Object>> list = null;
            init();
            try {
                pstmt = con.prepareStatement(sql);
                setParams(params);
                ResultSet rs = pstmt.executeQuery();
                list = getListFromRS(rs);
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close();
            }
            return list;
        }

        public List<Map<String, Object>> getList(String sql) {
            return getList(sql, null);
        }

        // 执行返回至多一条记录的查询操作，结果被封装到Map中。
        public Map<String, Object> getMap(String sql, Object[] params) {
            Map<String, Object> m = null;
            List<Map<String, Object>> l = getList(sql, params);
            if (l != null && l.size() != 0) {
                m = (Map<String, Object>) (l.get(0));
            }
            return m;
        }

        public Map<String, Object> getMap(String sql) {
            return getMap(sql, null);
        }

        // 将结果集中的内容封装到List中
        private List<Map<String, Object>> getListFromRS(ResultSet rs) throws SQLException {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> m = new HashMap<String, Object>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String colName = rsmd.getColumnLabel(i);
                    Object o = rs.getObject(colName);
                    if (o != null) {
                        m.put(colName, o);
                    }
                }
                list.add(m);
            }
            return list;
        }

        // 查询结果分页时，返回分页信息的Map，包括总页数，每页记录数和当前页中的记录。
        public Map<String, Object> getPage(String sql, Object[] params, String curPage) {
            Map<String, Object> page = new HashMap<String, Object>();
            String newSql = sql + " limit " + (Long.parseLong(curPage) - 1) * PAGE_REC_NUM + "," + PAGE_REC_NUM;
            List<Map<String, Object>> pageList = getList(newSql, params);
            sql = sql.toLowerCase();
            String countSql = "";
            if (sql.indexOf("group") >= 0) {
                countSql = "select count(*) as tempNum from (" + sql + ") as temp";
            } else {
                countSql = "select count(*) as tempNum " + sql.substring(sql.indexOf("from"));
            }
            String count_s = (String) getMap(countSql, params).get("tempNum");
            long count = Long.parseLong(count_s);
            long totalPage = 0;
            if (count % PAGE_REC_NUM == 0)
                totalPage = count / PAGE_REC_NUM;
            else
                totalPage = count / PAGE_REC_NUM + 1;
            page.put("list", pageList);
            page.put("totalPage", totalPage);
            page.put("recNum", PAGE_REC_NUM);
            return page;
        }

        public Map<String, Object> getPage(String sql, String curPage) {
            return getPage(sql, null, curPage);
        }
    }
}
