/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.dao.impl;

import edu.unl.csce361.group4.cookbook.Recipe;
import edu.unl.csce361.group4.cookbook.User;
import edu.unl.csce361.group4.cookbook.dao.UserDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 *
 * @author mstark
 */
public class UserDAOImpl implements UserDAO {

    private JdbcTemplate dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = new JdbcTemplate(dataSource);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public User getUserInfo(String userName) {
        String sql = "SELECT user_id, user_name, email_address, full_name FROM users WHERE user_name = ?";

        List<User> users = dataSource.query(sql,
                new Object[]{
                    userName
                },
                new BeanPropertyRowMapper(User.class));

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    @Override
    public void create(final User user) {
        

        KeyHolder holder = new GeneratedKeyHolder();
        dataSource.update(new PreparedStatementCreator() {
            final String sql = "INSERT INTO users (user_name, password, full_name, email_address) VALUES (?, ?, ?, ?)";
            
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                
                ps.setString(1, user.getUserName());
                ps.setString(2, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
                ps.setString(3, user.getFullName());
                ps.setString(4, user.getEmailAddress());
                
                return ps;
            }
        }, holder);
        
        user.setUserId(holder.getKey().longValue());

        if (user.getFavoriteRecipes() != null) {
            String sql = "INSERT INTO favorite_recipes (user_id, recipe_id) VALUES (?, ?)";
            for (Recipe favs : user.getFavoriteRecipes()) {
                

                dataSource.update(sql,
                        new Object[]{
                            user.getUserId(),
                            favs.getRecipeId()
                        });
            }
        }
    }

    @Override
    public void modify(User user) {
        //Update favorite_recipes
        String sql = "";

        if (user.getFavoriteRecipes() != null) {
            for (Recipe item : user.getFavoriteRecipes()) {
                sql = "SELECT favorite_recipe_id FROM favorite_recipes WHERE user_id = ? and recipe_id = ?";

                long favorite_recipe_id = dataSource.queryForObject(sql,
                        new Object[]{
                            user.getUserId(),
                            item.getRecipeId()
                        },
                        Long.class);

                if (favorite_recipe_id == 0) {
                    sql = "INSERT INTO favorite_recipes (user_id, recipe_id) VALUES (?, ?)";

                    dataSource.update(sql,
                            new Object[]{
                                user.getUserId(),
                                item.getRecipeId()
                            });
                } else {
                    sql = "UPDATE favorite_recipes "
                            + "SET user_id = ? "
                            + "recipe_id = ? "
                            + "WHERE favorite_recipe_id = ?";

                    dataSource.update(sql,
                            new Object[]{
                                user.getUserId(),
                                item.getRecipeId(),
                                favorite_recipe_id
                            });
                }
            }
        }

        //Update user entry
        sql = "UPDATE users "
                + "SET user_name = ?, "
                + "password = ?, "
                + "full_name = ?,"
                + "email_address = ? "
                + "WHERE user_id = ?";

        dataSource.update(sql,
                new Object[]{
                    user.getUserName(),
                    BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()),
                    user.getFullName(),
                    user.getEmailAddress(),
                    user.getUserId()
                });

        user.getFavoriteRecipes();
    }

    @Override
    public void delete(User user) {
        //Delete favorite_recipes
        String sql = "DELETE FROM favorite_recipes WHERE user_id = ?";

        dataSource.update(sql, new Object[]{user.getUserId()});

        //Delete recipe_owners recipes
        sql = "DELETE FROM recipe_owners WHERE user_id = ?";

        dataSource.update(sql, new Object[]{user.getUserId()});

        //Delete user
        sql = "DELETE FROM users WHERE user_id = ?";

        dataSource.update(sql, new Object[]{user.getUserId()});
    }
}
