/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package database.classes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * This class implements the CRUD operations for museum api data
 */
public class CrudOperations {
    public static Logger logger = LogManager.getLogger(CrudOperations.class);
    Connection connect = null;

    /**
     * This method create table
     *
     * @param tableName it is name of table
     */
    public void create(String tableName, String json) {
        try {
            // Load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // DB connection setup
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/normal", "root", "Sql2000!");
            Statement stmt = connect.createStatement();

            String sql = null;
            StringBuilder sb = new StringBuilder();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(json, Map.class);
            if (tableName != null) {
                logger.info("Dynamically creating query");
                sql = "create table " + tableName + "(";
                int i = 0;
                for (Map.Entry<String, Object> m : map.entrySet()) {
                    if (i == 0) {
                        sb.append(m.getKey()).append(" int primary key,");
                    } else {
                        sb.append(m.getKey()).append(" text,");
                    }

                    i++;
                }
                sb.append(")");
                String query = sb.deleteCharAt(sb.length() - 2).toString();
                sql += query;
            } else {
                logger.error("SYNTAX ERROR - Table name not given to create table");
            }
            ResultSet rs = stmt.executeQuery("show tables");
            ArrayList<String> arrayList = new ArrayList<>();
            while (rs.next()) {
                arrayList.add(rs.getString(1));

            }
            logger.info("Checking table name is database");
            if (!arrayList.contains(tableName)) {
                stmt.executeUpdate(sql);
                logger.info("Table {} created", tableName);
            } else {
                logger.error("table {} already created", tableName);
            }
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException - " + e);
        } catch (SQLException e) {
            logger.error("SQLException - " + e);
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException - " + e);
        } finally {
            try {
                if (connect != null) {
                    logger.info("Connection closed");
                    connect.close();
                }
            } catch (SQLException e) {
                logger.error("SQLException - " + e);
                System.err.println(e.getMessage());
            }
        }
    }

    public void insert(String json, String tableName) {
        try {
            logger.info("converting json to map");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(json, Map.class);
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/normal", "root", "Sql2000!");
            StringBuilder query = new StringBuilder("insert into " + tableName + "(");
            for (Map.Entry<String, Object> m : map.entrySet()) {
                query.append(m.getKey()).append(",");
            }
            query.append(")");
            query.deleteCharAt(query.length() - 2).append(" values(");
            int i = 0;
            for (Map.Entry<String, Object> m : map.entrySet()) {
                if (m.getValue() == null) {
                    m.setValue("null");
                    query.append("'").append(m.getValue()).append("'").append(",");
                } else {
                    if (i == 0) {
                        query.append(m.getValue()).append(",");
                    } else {
                        query.append("'").append(m.getValue()).append("'").append(",");
                    }
                }
                i++;
            }
            query.append(")");
            query.deleteCharAt(query.length() - 2);
            PreparedStatement statement = connect.prepareStatement(query.toString());
            statement.executeUpdate();
            statement.close();
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException - " + e);
        } catch (SQLException e) {
            logger.error("Exception - " + e);
        } finally {
            try {
                if (connect != null) {
                    logger.info("Connection closed");
                    connect.close();
                }
            } catch (SQLException e) {
                logger.error("SQLException - " + e);
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * This method update table
     *
     * @param query sql query
     */
    public void update(String query) {
        try {
            logger.info("Connection to database");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/normal", "root", "Sql2000!");
            Statement stmt = connect.createStatement();
            if (query != null) {
                logger.info("creating query");
                stmt.executeQuery(query);
                logger.info("query executed");
            } else {
                logger.error("query is invalid");
            }
            logger.info("createMuseum table updated");
        } catch (SQLException e) {
            logger.error("SQLException - " + e);
        } finally {
            try {
                if (connect != null) {
                    logger.info("Connection closed");
                    connect.close();
                }
            } catch (SQLException e) {
                logger.error("SQLException - " + e);
                System.err.println(e.getMessage());
            }
        }

    }

    /**
     * This method implements select query
     *
     *
     */
    public JSONArray select() {
        JSONArray result = null;

        try {
            ResultSet resultSet = null;
            logger.info("Connection to database");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/normal", "root", "Sql2000!");
            Statement stmt = connect.createStatement();


            logger.info("creating query");
            resultSet = stmt.executeQuery("select * from test");
            logger.info("query executed");

            ResultSetMetaData md = null;
            if (resultSet != null) {
                md = resultSet.getMetaData();
            } else {
                logger.error("resultSet is null");
            }
            int numCols = 0;
            if (md != null) {
                numCols = md.getColumnCount();
            } else {
                logger.error("ResultMetaData is empty");
            }
            ResultSetMetaData finalMd = md;
            List<String> colNames = IntStream.range(0, numCols)
                    .mapToObj(i -> {
                        try {
                            return finalMd.getColumnName(i + 1);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return "?";
                        }
                    })
                    .collect(Collectors.toList());

            result = new JSONArray();
            while (resultSet.next()) {
                JSONObject row = new JSONObject();
                ResultSet finalResultSet = resultSet;
                colNames.forEach(cn -> {
                    try {
                        row.put(cn, finalResultSet.getObject(cn));
                    } catch (JSONException e) {
                        logger.error("JSONException - " + e);
                    } catch (SQLException e) {
                        logger.error("SQLException - " + e);
                    }
                });
                result.put(row);
            }
        } catch (SQLException e) {
            logger.error("SQLException - " + e);
        }
        return result;

    }

    /**
     * This method implements delete sql query
     *
     * @param condition primary key of table
     * @param tableName represents table name of which data is to be deleted
     */
    public void delete(String condition, String tableName) {
        try {
            logger.info("Connection to database");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/normal", "root", "Sql2000!");
            Statement stmt = connect.createStatement();

            if (condition != null) {
                logger.info("creating query");
                stmt.executeUpdate("delete from " + tableName + " where " + condition);
                logger.info("query executed");
            } else {
                logger.error("query is null");
            }
            logger.info("deleted");
        } catch (SQLException e) {
            logger.error("SQLException - " + e);
        } finally {
            try {
                if (connect != null) {
                    logger.info("Connection closed");
                    connect.close();
                }
            } catch (SQLException e) {
                logger.error("SQLException - " + e);
                System.err.println(e.getMessage());
            }
        }
    }

//    public static void main(String[] args) {
//
//        CrudOperations crud = new CrudOperations();
//        DatabaseConnectivity db = new DatabaseConnectivity();
//        String response = db.response("https://collectionapi.metmuseum.org/public/collection/v1/objects/1");
//        logger.info("Creating table method invoked");
//        // Creating table
//        crud.create("test",response);
//        crud.insert(response,"test");
//
//        logger.info("DatabaseConnectivity program invoked");
//        DatabaseConnectivity db = new DatabaseConnectivity();
//        // Fetching response from museum API for object id 1 to 5
//        db.insert("createMuseum");

//        crud.update("update createMuseum set primaryImage = 'no image' where objectID = 1 ");
//        System.out.println(crud.select());
//        crud.delete("createMuseum" ,"objectID=5");
//    }
}