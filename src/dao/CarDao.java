package dao;

import core.Db;
import entity.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarDao{
    private Connection con;
    private final BrandDao brandDao;
    private final ModelDao modelDao;

    public CarDao(){
        this.con = Db.getInstance();
        this.brandDao = new BrandDao();
        this.modelDao = new ModelDao();
    }

    public Car getById(int id){
        Car obj = null;
        String query = "SELECT * FROM public.car WHERE car_id = ?";
        try {
            PreparedStatement pr = con.prepareStatement(query);
            pr.setInt(1,id);
            ResultSet resultSet = pr.executeQuery();
            if(resultSet.next()) obj = this.match(resultSet);
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return obj;
    }

    public ArrayList<Car> findAll(){
        return this.selectByQuery("SELECT * FROM public.car ORDER BY car_id ASC");
    }

    public ArrayList<Car> selectByQuery(String query){
        ArrayList<Car> cars = new ArrayList<>();
        try {
            ResultSet resultSet = this.con.createStatement().executeQuery(query);
            while (resultSet.next()){
                cars.add(this.match(resultSet));
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return cars;
    }

    public Car match(ResultSet resultSet) throws SQLException{
        Car car = new Car();
        car.setId(resultSet.getInt("car_id"));
        car.setModel_id(resultSet.getInt("car_model_id"));
        car.setPlate(resultSet.getString("car_plate"));
        car.setColor(Car.Color.valueOf(resultSet.getString("car_color")));
        car.setKm(resultSet.getInt("car_km"));
        car.setModel(this.modelDao.getById(car.getModel_id()));
        return car;
    }

    public boolean update(Car car){
        String query = "UPDATE public.car SET " +
                "car_model_id = ? , " +
                "car_color = ? , "+
                "car_km = ? , " +
                "car_plate = ? "+
                "WHERE car_id = ?";
        try {
            PreparedStatement pr = con.prepareStatement(query);
            pr.setInt(1,car.getModel_id());
            pr.setString(2,car.getColor().toString());
            pr.setInt(3,car.getKm());
            pr.setString(4,car.getPlate());
            pr.setInt(5,car.getId());
            return pr.executeUpdate() != -1;
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return true;
    }

    public boolean save(Car car){
        String query = "INSERT INTO public.car "+
                "(" +
                "car_model_id,"+
                "car_color," +
                "car_km,"+
                " car_plate"+
                ")" +
                " VALUES (?,?,?,?)";
        try {
            PreparedStatement pr = con.prepareStatement(query);
            pr.setInt(1,car.getModel_id());
            pr.setString(2,car.getColor().toString());
            pr.setInt(3,car.getKm());
            pr.setString(4,car.getPlate());
            return pr.executeUpdate() != -1;
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return true;
    }

    public boolean delete(int carId){
        String query = "DELETE FROM public.car WHERE car_id = ?";
        try {
            PreparedStatement pr = con.prepareStatement(query);
            pr.setInt(1,carId);
            return pr.executeUpdate() != -1;
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return true;
    }
}
