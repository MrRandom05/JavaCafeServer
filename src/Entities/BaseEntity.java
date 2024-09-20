package Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class BaseEntity<Q extends BaseEntity> {
    public abstract ArrayList<Q> castResultSetToObject(ResultSet resultSet) throws SQLException;
}
