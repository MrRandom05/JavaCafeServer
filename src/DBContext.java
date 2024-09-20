import Entities.BaseEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DBContext<T extends BaseEntity> {
protected String connectionString = "";
protected Connection connection;

    public  DBContext(String connectionString) {
        try {
            this.connectionString = connectionString;
            connection = DriverManager.getConnection(connectionString, "root", "123456");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String getConnectionString() {
        return connectionString;
    }

    public Connection getConnection() {
        return connection;
    }

    public String createAny(String query) {
        try {
            if (query == null || query.trim().isEmpty()) return "failed";
            Statement statement = connection.createStatement();
            int count = statement.executeUpdate(query);
            statement.close();
            return count == 0 ? "failed" : "success";
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String executeAny(String query) {
        try {
            if (query == null || query.trim().isEmpty()) return "failed";
            Statement statement = connection.createStatement();
            var res = statement.execute(query);
            statement.close();
            return !res ? "failed" : "success";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList queryAny(String query, T targetType) {
        try {
            if (query == null || query.trim().isEmpty()) return null;
            Statement statement = connection.createStatement();
            var res = statement.executeQuery(query);
            return targetType.castResultSetToObject(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isDBExists(String dbName) {
        try {
            ResultSet resultSet = connection.getMetaData().getCatalogs();
            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if (Objects.equals(databaseName, dbName)) {
                    return true;
                }
            }
            resultSet.close();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void useDB(String dbName) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("use " + dbName + ";");
            statement.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String dataBaseCreate() {
        try {
            createAny("Create DataBase CafeDB;");
            executeAny("Use CafeDB;");
            createAny("Create table Roles " +
                    "(IdRole int Primary Key Auto_Increment, " +
                    "RoleName nvarchar(50) not null) Engine = INNODB;");
            createAny("Create table UserStatuses " +
                    "(IdUserStatuses int Primary Key Auto_Increment, " +
                    "UserStatusName nvarchar(50) not null) Engine = INNODB;");
            createAny("Create table Users " +
                    "(IdUser int Primary key Auto_Increment, " +
                    "Login nvarchar(50) not null, " +
                    "Password nvarchar(50) not null, " +
                    "RoleId int not null, " +
                    "UserStatusId int not null, " +
                    "Foreign Key (RoleId) " +
                    "References Roles(IdRole) On Update Cascade On Delete Restrict," +
                    "Foreign Key (UserStatusId) " +
                    "References UserStatuses(IdUserStatuses) On Update Cascade On Delete Restrict) Engine=INNODB;");
            return "failed";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
