package com.revature.banking.orm.utils;

import com.revature.banking.orm.annotation.ColumnInORM;
import com.revature.banking.orm.annotation.DataSourceORM;
import com.revature.banking.orm.annotation.NotIntoDabase;
import com.revature.banking.util.datasource.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CrudORM {

    protected Logger logger = LogManager.getLogger();

    public static String convertTypeFromJaveToPostgresql(Class<?> col_type) {

        if (col_type.isAssignableFrom(String.class)) {
            return "varchar";
        } else if (col_type.isAssignableFrom(Double.class)) {
            return "float8";
        } else if (col_type.isAssignableFrom(LocalDateTime.class)) {
            return "timestamp";
        }
        return null;
    }

    public <T> List<T> readTable(T newData, Map<String, Map<String, String>> whereOderBy, Class<T> cls) {
        List<T> appUserORMList = new LinkedList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            Class<?> aClass = newData.getClass();
            DataSourceORM tableAnnotation = aClass.getAnnotation(DataSourceORM.class);
            String tableName = tableAnnotation.TableName();
            String schema = tableAnnotation.Schema();
            List<Field> fields = ReflectionORM.getFieldNamesAndValues(aClass);

            StringBuilder sql = new StringBuilder();
            sql.append(" select * from ").append(schema).append(".").append(tableName).append(" ");

            if (whereOderBy.containsKey("where") && whereOderBy.get("where").size() > 0) {
                sql.append("where ");
                for (Map.Entry<String, String> where : whereOderBy.get("where").entrySet()) {
                    sql.append(where.getKey()).append(" = '").append(where.getValue()).append("', ");
                }
                sql.setLength(sql.length() - 2);
            }
            if (whereOderBy.containsKey("oderBy") && whereOderBy.get("oderBy").size() > 0) {
                for (Map.Entry<String, String> cwo : whereOderBy.get("oderBy").entrySet()) {
                    sql.append(cwo.getKey()).append(" ").append(cwo.getValue()).append(", ");
                }
                sql.setLength(sql.length() - 2);
            }
            sql.append(" ; ");

            logger.info(sql);

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

            ResultSet rs = pstmt.executeQuery();

            ScriptEngine engine = new ScriptEngineManager().getEngineByName("beanshell");
            String CapitalFirst = "";
            while (rs.next()) {
                T appEntityORM = cls.newInstance();
                // ------------ values real
                for (Field f : fields) {
                    CapitalFirst = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    engine.put("appUserORM", appEntityORM);

                    if (f.getType().isAssignableFrom(LocalDateTime.class)) {
                        engine.eval("appUserORM.set" + CapitalFirst + "(\"" + rs.getTimestamp(f.getName()).toString().substring(0, 19) + "\");");
                    } else {
                        engine.eval("appUserORM.set" + CapitalFirst + "(\"" + rs.getString(f.getName()) + "\");");
                    }
                }
                appUserORMList.add(appEntityORM);
            }
        } catch (SQLException | ScriptException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return appUserORMList;
    }

    public <T> T updateTable(T newData, Map<String, Map<String, String>> colsWhereOderBy) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Class<?> aClass = newData.getClass();
            DataSourceORM tableAnnotation = aClass.getAnnotation(DataSourceORM.class);
            String tableName = tableAnnotation.TableName();
            String schema = tableAnnotation.Schema();
            List<Field> fields = ReflectionORM.getFieldNamesAndValues(aClass);

            StringBuilder sql = new StringBuilder();
            sql.append("update ").append(schema).append(".").append(tableName).append(" set ");
            // ------------ column
            if (colsWhereOderBy.containsKey("cols") && colsWhereOderBy.get("cols").size() > 0) {
                for (Map.Entry<String, String> col : colsWhereOderBy.get("cols").entrySet()) {
                    sql.append(col.getKey()).append(" = '").append(col.getValue()).append("', ");
                }
                sql.setLength(sql.length() - 2);
            }
            // ------------ where
            if (colsWhereOderBy.containsKey("where") && colsWhereOderBy.get("where").size() > 0) {
                sql.append(" where ");
                for (Map.Entry<String, String> wh : colsWhereOderBy.get("where").entrySet()) {
                    sql.append(wh.getKey()).append(" = '").append(wh.getValue()).append("', ");
                }
                sql.setLength(sql.length() - 2);
            }
            sql.append(";");

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

            logger.info(sql);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 0) {
                return newData;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T deletTable(T newData, Map<String, Map<String, String>> colsWhereOderBy) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Class<?> aClass = newData.getClass();
            DataSourceORM tableAnnotation = aClass.getAnnotation(DataSourceORM.class);
            String tableName = tableAnnotation.TableName();
            String schema = tableAnnotation.Schema();
            List<Field> fields = ReflectionORM.getFieldNamesAndValues(aClass);

            StringBuilder sql = new StringBuilder();
            sql.append("delete from ").append(schema).append(".").append(tableName);
            // ------------ where
            if (colsWhereOderBy.containsKey("where") && colsWhereOderBy.get("where").size() > 0) {
                sql.append(" where ");
                for (Map.Entry<String, String> wh : colsWhereOderBy.get("where").entrySet()) {
                    sql.append(wh.getKey()).append(" = '").append(wh.getValue()).append("', ");
                }
                sql.setLength(sql.length() - 2);
            }
            sql.append(";");

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

            logger.info(sql);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 0) {
                return newData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T insertTable(T newData) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Class<?> aClass = newData.getClass();
            DataSourceORM tableAnnotation = aClass.getAnnotation(DataSourceORM.class);
            String tableName = tableAnnotation.TableName();
            String schema = tableAnnotation.Schema();
            List<Field> fields = ReflectionORM.getFieldNamesAndValues(aClass);

            // ------------
            String sql = "insert into " + schema + "." + tableName + "( ";
            // ------------ column
            for (Field f : fields) {
                if (f.getAnnotation(NotIntoDabase.class) != null)
                    continue;
                sql += f.getName() + ", ";
            }
            sql = sql.substring(0, sql.length() - 2);

            // ------------ values placeholders
            sql += ") values (";
            for (Field f : fields) {
                if (f.getAnnotation(NotIntoDabase.class) != null)
                    continue;
                sql += "?, ";
            }
            sql = sql.substring(0, sql.length() - 2);
            sql += ")";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            // ------------ values real
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("beanshell");
            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).getAnnotation(NotIntoDabase.class) != null)
                    continue;
                String fn = fields.get(i).getName();
                String cap = fn.substring(0, 1).toUpperCase() + fn.substring(1);

                engine.put("newData", newData);

                if (fields.get(i).getType().isAssignableFrom(Double.class)) {
                    engine.eval("Double d = newData.get" + cap + "();");
                    pstmt.setDouble(i + 1, (Double) engine.get("d"));
                } else {
                    engine.eval("String r = newData.get" + cap + "();");
                    pstmt.setString(i + 1, (String) engine.get("r"));
                }
            }

            logger.info(sql);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 0) {
                return newData;
            }

        } catch (SQLException | ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> void testCreateAllTablesWithDataSourceORM() throws SQLException {
        List<String> listClassesNames = ReflectionORM.getClassNamesInPackage("com.revature");
        List<Class<?>> classList = listClassesNames.stream()
                .map(ClassNameToClassMapper.getInstance())
                .filter(clazz -> clazz.isAnnotationPresent(DataSourceORM.class))
                .collect(Collectors.toList());

        System.out.println("+---------------------------------------------------------------------------------------+");
        System.out.printf("Found <<--- %d --->> target classes.\n", classList.size());

        System.out.println("+---------------------------------------------------------------------------------------+");
        for (Class<?> aClass : classList) {
            DataSourceORM tableAnnotation = aClass.getAnnotation(DataSourceORM.class);

            System.out.printf("Table Name <<--- %s --->>\n", tableAnnotation.Schema() + "." + tableAnnotation.TableName());
            //if (tableExists(tableAnnotation.Schema() + "." + tableAnnotation.TableName())) {
                createTable(aClass);
            //}
        }
    }

    boolean tableExists(String tableName) throws SQLException {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, tableName, new String[]{"TABLE"});
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T> void createTable(Class<T> newData) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            DataSourceORM tableAnnotation = newData.getAnnotation(DataSourceORM.class);
            String tableName = tableAnnotation.TableName();
            String schema = tableAnnotation.Schema();
            List<Field> fields = ReflectionORM.getFieldNamesAndValues(newData);

            StringBuilder sql = new StringBuilder();
            sql.append(" CREATE TABLE ").append(schema).append(".").append(tableName).append(" ( ");

            for (Field f : fields) {
                sql.append(f.getName()).append(" ").append(convertTypeFromJaveToPostgresql(f.getType())).append(" ");
                ColumnInORM columnInORM = f.getAnnotation(ColumnInORM.class);
                if (columnInORM.Size() != 0) {
                    sql.append("( ");
                    sql.append(columnInORM.Size());
                    sql.append(") ");
                }
                if (!columnInORM.Constraint().equals("")) {
                    sql.append(columnInORM.Constraint());
                    sql.append(" ");
                }
                if (!columnInORM.DefaultValue().equals("")) {
                    sql.append("Default ");
                    sql.append(columnInORM.DefaultValue());
                }
                sql.append(", ");
            }
            for (Field f : fields) {
                ColumnInORM columnInORM = f.getAnnotation(ColumnInORM.class);
                if (columnInORM.PRIMARY().equals("Y") || columnInORM.UNIQUE().equals("Y") || !columnInORM.Check().equals("")) {
                    if (columnInORM.PRIMARY().equals("Y")) {
                        sql.append("CONSTRAINT ").append(tableName).append("_").append(f.getName()).append("_");
                        sql.append("pk PRIMARY KEY (");
                        sql.append(f.getName());
                        sql.append("), ");
                    }
                    if (columnInORM.UNIQUE().equals("Y")) {
                        sql.append("CONSTRAINT ").append(tableName).append("_").append(f.getName()).append("_");
                        sql.append("key UNIQUE (");
                        sql.append(f.getName());
                        sql.append("), ");
                    }
                    if (!columnInORM.Check().equals("")) {
                        sql.append("CONSTRAINT ").append(tableName).append("_").append(f.getName()).append("_");
                        sql.append("check Check ((");
                        sql.append(columnInORM.Check());
                        sql.append(")), ");
                    }
                }
            }
            sql.setLength(sql.length() - 2);
            sql.append(" ); ");

            logger.info(sql);

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 0) {
                System.out.println("wwww");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
