package Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User extends BaseEntity {
    public int IdUser;
    public String Login;
    public String Password;
    public int RoleId;
    public int UserStatusId;

    @Override
    public ArrayList<User> castResultSetToObject(ResultSet resultSet) throws SQLException {
        ArrayList<User> resList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.IdUser = resultSet.getInt(1);
            user.Login = resultSet.getString(2);
            user.Password = resultSet.getString(3);
            user.RoleId = resultSet.getInt(4);
            user.UserStatusId = resultSet.getInt(5);
            resList.add(user);
        }
        resultSet.close();
        return resList;
    }
}
