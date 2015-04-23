/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.dao;

import edu.unl.csce361.group4.cookbook.User;

/**
 *
 * @author mstark
 */
public interface UserDAO {
    User getUserInfo(String userName);
    void create(User user);
    void modify(User user);
    void delete(User user);
}
