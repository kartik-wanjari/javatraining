/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package com.azuga;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * This class implements the CRUD operations for museum api data
 */
public class CrudOperations {
    public static Logger logger = LogManager.getLogger(CrudOperations.class);
    Connection connect = null;

    /**
     * This method create table. Primary key will be id of dataset.
     *
     * @param tableName it is name of table
     */
    public void create(String tableName, String json) {
        try {
            logger.debug("Dataset - "+json);
            logger.debug("Table name - "+tableName);
            // Load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("Setting up database connection");
            // DB connection setup
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/normal", "root", "Sql2000!");

            Statement stmt = connect.createStatement();

            String sql = null;
            StringBuilder sb = new StringBuilder();
            logger.info("Mapping json to Map using object mapper");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(json, Map.class);
            if (tableName != null) {
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
                logger.debug("Query - "+sql);
            } else {
                logger.error("SYNTAX ERROR - Table name not given to create table");
            }
            logger.info("Query execution");
            ResultSet rs = stmt.executeQuery("show tables");
            ArrayList<String> arrayList = new ArrayList<>();
            while (rs.next()) {
                arrayList.add(rs.getString(1));

            }
            logger.debug("Tables in database - "+arrayList);
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

    /**
     * This method insert dataset provided table.
     * @param json dataset to be inserted.
     * @param tableName name of the table in which data is to be inserted
     * @return response of method.
     */
    public Response insert(String json, String tableName) {
        String payload = "";
        String result = null;
        String id = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            payload = jsonObject.toString();
            logger.debug("Dataset - "+json);
            logger.debug("Table name - "+tableName);
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("converting json to map");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(json, Map.class);
            logger.info("Setting up database connection");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/normal", "root", "Sql2000!");
            StringBuilder query = new StringBuilder("insert into " + tableName + "(");
            for (Map.Entry<String, Object> m : map.entrySet()) {
                query.append(m.getKey()).append(",");
                if(Objects.equals(m.getKey(), "objectID")){
                    id = m.getValue().toString();
                }
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

            result = "inserted data"+"\n"+payload;
            statement.close();
        } catch (SQLSyntaxErrorException e) {
            logger.error("SQLSyntaxErrorException - " + e.getMessage());

            result = "Invalid "+id+", please provide numeric objectID"+"\n"+payload;
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }catch (SQLIntegrityConstraintViolationException e) {
            logger.error("SQLIntegrityConstraintViolationException - " + e.getMessage());
            result = "Duplicate objectID = "+id +"\n"+payload;
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }catch (SQLDataException e) {
            logger.error("SQLDataException - " + e.getMessage());
            result = e.getMessage();
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }catch (SQLFeatureNotSupportedException e) {
            logger.error("SQLFeatureNotSupportedException - " + e.getMessage());

            result = e.getMessage();
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }catch (JsonProcessingException e) {
            logger.error("JsonProcessingException - " + e.getMessage());
            result = e.getMessage();
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException - " + e.getMessage());

            result = e.getMessage();
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        } catch (SQLException e) {
            logger.error("SQLException - " + e.getMessage());
            result = e.getMessage();
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        } catch (JSONException e) {
            logger.error("JSONException - " + e.getMessage());
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

        return Response.ok(result).build();
    }

    /**
     * This method update table.
     *
     * @param json dataset to be updated.
     * @return response of method
     */
    public String update(String json) {
        String result = "not updated";
        try {
            System.out.println("this is json: "+json);
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("Connection to database");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/normal", "root", "Sql2000!");
            Statement stmt = connect.createStatement();

            StringBuilder sb = new StringBuilder("update test set ");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(json, Map.class);
            String condition = "";
            logger.info("Dynamically creating query");

            for (Map.Entry<String, Object> m : map.entrySet()) {
                if (m.getKey().equals("objectID")) {
                    condition = " where " + m.getKey() + " = " + m.getValue();
                    sb.append(m.getKey()).append(" = ").append("'").append(m.getValue()).append("'").append(",");
                } else {
                    sb.append(m.getKey()).append(" = ").append("'").append(m.getValue()).append("'").append(",");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(condition);

            String query = sb.toString();

            if (json != null) {
                logger.info("creating json");
                stmt.executeUpdate(query);
                result = "updated";
                logger.info("json executed");
            } else {
                logger.error("json is invalid");
            }
            logger.info("createMuseum table updated");
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException - " + e);
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException - " + e);
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
        return result;
    }

    /**
     * This method implements select query
     * @return dataset of table from database
     */
    public String select() {
        JSONArray result = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            ResultSet resultSet;
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
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException - " + e);
        } catch (SQLException e) {
            logger.error("SQLException - " + e);
        }
        if (result != null) {
            return result.toString();
        } else {
            return "null";
        }
    }

    /**
     * This method implements delete sql query
     *
     * @param id id of dataset to be deleted.
     */
    public Response delete(String id) {
        String result;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("Connection to database");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/normal", "root", "Sql2000!");
            Statement stmt = connect.createStatement();

            int i = Integer.parseInt(id);
            logger.info("creating query");

            ResultSet rs = stmt.executeQuery("select objectID from test");
            ArrayList<Integer> arrayList = new ArrayList<>();
            while (rs.next()){
                arrayList.add(Integer.parseInt(rs.getString(1)));
            }
            if(arrayList.contains(i)){
                stmt.executeUpdate("delete from test where objectID = " + i);
                result = "deleted objectId = " + i;
            }else {
                result = "Could not find objectID "+i;
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
            logger.info("query executed");

            logger.info("deleted");
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException - " + e);
            result = e.getMessage();
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        } catch (SQLException e) {
            logger.error("SQLException - " + e);
            result = e.getMessage();
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        } finally {
            try {
                if (connect != null) {
                    logger.info("Connection closed");
                    connect.close();
                }
            } catch (SQLException e) {
                logger.error("SQLException - " + e);
                result = e.getMessage();
            }
        }
        return Response.ok(result).build();
    }

}