package manager;

import db.DBConnectionProvider;
import model.Post;
import util.DateUtil;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PostManager {

    private CategoryManager categoryManager;
    private UserManager userManager;

    private Connection connection;

    public PostManager() {
        connection = DBConnectionProvider.getInstance().getConnection();
        categoryManager = new CategoryManager();
        userManager = new UserManager();
    }

    public void addPost(Post post) {
        try {
            String query = "INSERT INTO post(`title`,`short_text`,`text`,`created_date`, `category_id`, `pic_url`, `user_id`) " +
                    "VALUES(?,?,?,?,?,?,?);";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getShortText());
            statement.setString(3, post.getText());
            statement.setString(4, DateUtil.convertDateToString(post.getCreatedDate()));
            statement.setLong(5, post.getCategory().getId());
            statement.setString(6, post.getPicUrl());
            statement.setLong(7, post.getUser().getId());
            System.out.println("executing the following statement ->" + query);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                post.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Post getPostById(int id) {
        String query = "SELECT * FROM post WHERE id = " + id;
        return getPostFromDB(query);
    }


    public List<Post> getAllPost() {
        String query = "SELECT * FROM post";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<Post> posts = new LinkedList<Post>();
            while (resultSet.next()) {
                posts.add(createPostFromResultSet(resultSet));
            }
            return posts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Post> getAllPostByCategory(int categoryId) {
        String query = "SELECT * FROM post where category_id = " + categoryId;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<Post> posts = new LinkedList<Post>();
            while (resultSet.next()) {
                posts.add(createPostFromResultSet(resultSet));
            }
            return posts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removePostById(int id) {
        String query = "DELETE  FROM post WHERE id = " + id;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Post getPostFromDB(String query) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return createPostFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Post createPostFromResultSet(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getInt(1));
        post.setTitle(resultSet.getString(2));
        post.setShortText(resultSet.getString(3));
        post.setText(resultSet.getString(4));
        post.setCreatedDate(DateUtil.convertStringToDate(resultSet.getString(5)));
        post.setCategory(categoryManager.getCategoryById(resultSet.getInt(6)));
        post.setPicUrl(resultSet.getString(7));
        post.setUser(userManager.getUserById(resultSet.getInt(8)));

        return post;
    }
}
