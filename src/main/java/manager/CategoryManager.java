package manager;

import db.DBConnectionProvider;
import model.Category;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CategoryManager {


    private Connection connection;

    public CategoryManager() {
        connection = DBConnectionProvider.getInstance().getConnection();
    }

    public void addCategory(Category category) {
        try {
            String query = "INSERT INTO category(`name`) " +
                    "VALUES(?);";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            System.out.println("executing the following statement ->" + query);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                category.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Category getCategoryById(int id) {
        String query = "SELECT * FROM category WHERE id = " + id;
        return getCategoryFromDB(query);
    }


    public List<Category> getAllCategories() {
        String query = "SELECT * FROM category";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<Category> categories = new LinkedList<Category>();
            while (resultSet.next()) {
                categories.add(createCategoriesFromResultSet(resultSet));
            }
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeCategoryById(int id) {
        String query = "DELETE  FROM category WHERE id = " + id;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Category getCategoryFromDB(String query) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return createCategoriesFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Category createCategoriesFromResultSet(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setId(resultSet.getInt(1));
        category.setName(resultSet.getString(2));
        return category;
    }
}
