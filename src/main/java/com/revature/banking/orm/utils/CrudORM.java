package com.revature.banking.orm.utils;

import com.revature.banking.orm.annotation.ColumnInORM;
import com.revature.banking.orm.annotation.DataSourceORM;
import com.revature.banking.orm.annotation.NotIntoDabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrudORM {

    protected Logger logger = LogManager.getLogger();
    public InitORM initORM;

    public CrudORM(Object newData) {
        initORM = new InitORM(newData);
    }

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

    public boolean existsTable(Connection conn, String schema, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet res = meta.getTables(null, schema, tableName,
                new String[]{"TABLE"});
        if (res.next()) {
            logger.info(tableName + " -- table already exists.");
            return true;
        }
        logger.info(tableName + " -- table does not exist.");
        return false;
    }

    public <T> boolean createTable(Class<T> newData) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            DataSourceORM tableAnnotation = newData.getAnnotation(DataSourceORM.class);
            if( tableAnnotation == null){
                return false;
            }
            String tableName = tableAnnotation.TableName();
            String schema = tableAnnotation.Schema();
            if (existsTable(conn, schema, tableName))
                return true;

            List<Field> fields = ReflectionORM.getFieldNames(newData);
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
                    if (!columnInORM.FOREIGN().equals("")) {
                        sql.append("CONSTRAINT ").append(tableName).append("_").append(f.getName()).append("_");
                        sql.append("fk FOREIGN KEY (");
                        sql.append(f.getName());
                        sql.append(") ");
                        sql.append("REFERENCES ");
                        sql.append(schema).append(".").append(columnInORM.FOREIGN());
                        sql.append(", ");
                    }
                }
            }
            sql.setLength(sql.length() - 2);
            sql.append(" ); ");
            logger.info(sql);
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted != 0) {
                logger.info("{} -- table created", tableName);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T> List<T> readTable(T newData, Map<String, Map<String, String>> whereOrderBy, Class<T> cls) {
        List<T> ormList = new LinkedList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            Class<?> aClass = newData.getClass();
            DataSourceORM tableAnnotation = aClass.getAnnotation(DataSourceORM.class);
            if( tableAnnotation == null){
                return null;
            }
            String tableName = tableAnnotation.TableName();
            String schema = tableAnnotation.Schema();
            List<Field> fields = ReflectionORM.getFieldNames(aClass);

            StringBuilder sql = new StringBuilder();
            sql.append(" select * from ").append(schema).append(".").append(tableName).append(" ");
            if (whereOrderBy.containsKey("where") && whereOrderBy.get("where").size() > 0) {
                sql.append("where ");
                for (Map.Entry<String, String> where : whereOrderBy.get("where").entrySet()) {
                    sql.append(where.getKey()).append(" = '").append(where.getValue()).append("', ");
                }
                sql.setLength(sql.length() - 2);
            }
            if (whereOrderBy.containsKey("oderBy") && whereOrderBy.get("oderBy").size() > 0) {
                for (Map.Entry<String, String> cwo : whereOrderBy.get("oderBy").entrySet()) {
                    sql.append(cwo.getKey()).append(" ").append(cwo.getValue()).append(", ");
                }
                sql.setLength(sql.length() - 2);
            }
            sql.append(" ; ");
            logger.info(sql);
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            String CapitalFirst = "";
            while (rs.next()) {
                T appEntityORM = cls.newInstance();
                // ------------ values real
                for (Field f : fields) {
                    CapitalFirst = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);

                    Method aMethod = aClass.getMethod("set" + CapitalFirst, String.class);

                    if (f.getType().isAssignableFrom(LocalDateTime.class)) {
                        aMethod.invoke(appEntityORM, rs.getTimestamp(f.getName()).toString().substring(0, 19));
                    } else {
                        aMethod.invoke(appEntityORM, rs.getString(f.getName()));
                    }
                }
                ormList.add(appEntityORM);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
        return ormList;
    }

    public <T> T updateTable(T newData, Map<String, Map<String, String>> colsWhereOrderBy) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Class<?> aClass = newData.getClass();
            DataSourceORM tableAnnotation = aClass.getAnnotation(DataSourceORM.class);
            if( tableAnnotation == null){
                return null;
            }
            String tableName = tableAnnotation.TableName();
            String schema = tableAnnotation.Schema();
            List<Field> fields = ReflectionORM.getFieldNames(aClass);

            StringBuilder sql = new StringBuilder();
            sql.append("update ").append(schema).append(".").append(tableName).append(" set ");
            // ------------ column
            if (colsWhereOrderBy.containsKey("cols") && colsWhereOrderBy.get("cols").size() > 0) {
                for (Map.Entry<String, String> col : colsWhereOrderBy.get("cols").entrySet()) {

                    String CapitalFirst = col.getKey().substring(0, 1).toUpperCase() + col.getKey().substring(1);
                    Method aMethod = aClass.getMethod("get" + CapitalFirst);

                    if (aMethod.getReturnType().isAssignableFrom(LocalDateTime.class) ||
                            aMethod.getReturnType().isAssignableFrom(Double.class)) {
                        sql.append(col.getKey()).append(" = ").append(aMethod.invoke(newData)).append(", ");
                    } else {
                        sql.append(col.getKey()).append(" = '").append(aMethod.invoke(newData)).append("', ");
                    }
                }
                sql.setLength(sql.length() - 2);
            }
            // ------------ where
            if (colsWhereOrderBy.containsKey("where") && colsWhereOrderBy.get("where").size() > 0) {
                sql.append(" where ");
                for (Map.Entry<String, String> wh : colsWhereOrderBy.get("where").entrySet()) {
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

        } catch (SQLException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T insertTable(T newData) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Class<?> aClass = newData.getClass();
            DataSourceORM tableAnnotation = aClass.getAnnotation(DataSourceORM.class);
            if( tableAnnotation == null){
                return null;
            }
            String tableName = tableAnnotation.TableName();
            String schema = tableAnnotation.Schema();
            List<Field> fields = ReflectionORM.getFieldNames(aClass);
            // ------------
            StringBuilder sql = new StringBuilder("insert into " + schema + "." + tableName + "( ");
            // ------------ column
            for (Field f : fields) {
                if (f.getAnnotation(NotIntoDabase.class) != null)
                    continue;
                sql.append(f.getName()).append(", ");
            }
            sql = new StringBuilder(sql.substring(0, sql.length() - 2));
            // ------------ values placeholders
            sql.append(") values (");
            for (Field f : fields) {
                if (f.getAnnotation(NotIntoDabase.class) != null)
                    continue;
                sql.append("?, ");
            }
            sql = new StringBuilder(sql.substring(0, sql.length() - 2));
            sql.append(")");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            // ------------ values real
            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).getAnnotation(NotIntoDabase.class) != null)
                    continue;
                String fn = fields.get(i).getName();
                String cap = fn.substring(0, 1).toUpperCase() + fn.substring(1);
                Method aMethod = aClass.getMethod("get" + cap);
                if (fields.get(i).getType().isAssignableFrom(Double.class)) {
                    pstmt.setDouble(i + 1, (Double) aMethod.invoke(newData));
                } else {
                    pstmt.setString(i + 1, (String) aMethod.invoke(newData));
                }
            }
            logger.info(pstmt.toString());
            System.out.println(pstmt.toString());
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted != 0) {
                return newData;
            }
        } catch (SQLException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T deletTable(T newData, Map<String, Map<String, String>> colsWhereOrderBy) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            Class<?> aClass = newData.getClass();
            DataSourceORM tableAnnotation = aClass.getAnnotation(DataSourceORM.class);
            if( tableAnnotation == null){
                return null;
            }
            String tableName = tableAnnotation.TableName();
            String schema = tableAnnotation.Schema();
            List<Field> fields = ReflectionORM.getFieldNames(aClass);

            StringBuilder sql = new StringBuilder();
            sql.append("delete from ").append(schema).append(".").append(tableName);
            // ------------ where
            if (colsWhereOrderBy.containsKey("where") && colsWhereOrderBy.get("where").size() > 0) {
                sql.append(" where ");
                for (Map.Entry<String, String> wh : colsWhereOrderBy.get("where").entrySet()) {
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

    public <T> void createAllOfTablesWithDataSourceORM(T newData) throws SQLException, URISyntaxException {
        Class<?> aClass = newData.getClass();
        List<String> listClassesNames = ReflectionORM.getClassNamesInPackage(aClass, InitORM.packageNameInOrm);
        List<Class<?>> classList = listClassesNames.stream()
                .map(ClassNameToClassMapper.getInstance())
                .filter(clazz -> clazz.isAnnotationPresent(DataSourceORM.class))
                .collect(Collectors.toList());
        System.out.println(classList);
        logger.info("+----------------------------------------------------------*");
        logger.info("Found <<--- {} --->> target classes.\n", classList.size());
        logger.info("+----------------------------------------------------------*");
        for (Class<?> aClass1 : classList) {
            DataSourceORM tableAnnotation = aClass1.getAnnotation(DataSourceORM.class);
            System.out.printf("Table Name <<--- %s --->>\n", tableAnnotation.Schema() + "." + tableAnnotation.TableName());
            createTable(aClass1);
        }
    }

//    boolean tableExists(String tableName) throws SQLException {
//        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
//            DatabaseMetaData meta = conn.getMetaData();
//            ResultSet resultSet = meta.getTables(null, null, tableName, new String[]{"TABLE"});
//            return resultSet.next();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

}
