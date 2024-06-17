package dao;

import core.Db;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {
    private final Connection con;
    public UserDao(){
        this.con = Db.getInstance();
    }

    private ArrayList<User> findAll(){
        ArrayList<User> userList = new ArrayList<>();
        String sql ="SELECT * FROM public.user";
        try {
            ResultSet resultSet = this.con.createStatement().executeQuery(sql);
            while (resultSet.next()){
                userList.add(this.match(resultSet));
                User obj = new User();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return userList;
    }
    public User findByLogin (String username, String password){
        User obj = null;
        String query = "SELECT * FROM public.user WHERE user_name = ? AND user_pass = ?";
        try {
            PreparedStatement pr = this.con.prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2, password);
            ResultSet resultSet = pr.executeQuery();
            if (resultSet.next()){
                obj = this.match(resultSet);

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return obj;
    }
    public User match(ResultSet resultSet) throws SQLException {
        User obj = new User();
        obj.setId(resultSet.getInt("user_id"));
        obj.setUsername(resultSet.getString("user_name"));
        obj.setPassword(resultSet.getString("user_pass"));
        obj.setRole(resultSet.getString("user_role"));

        return obj;
    }
}
