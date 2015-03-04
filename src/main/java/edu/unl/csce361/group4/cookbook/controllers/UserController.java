/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.controllers;

import edu.unl.csce361.group4.cookbook.User;
import edu.unl.csce361.group4.cookbook.dao.UserDAO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mstark
 */
@RestController
@RequestMapping("/users/")
public class UserController {
    private UserDAO userDAO;
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User loginUser(String userName, String password) {
        return userDAO.login(userName, password);
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public User createUser(User user) {
        userDAO.create(user);
        return user;
    }
    
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public User modifyUser(User user) {
        userDAO.modify(user);
        return user;
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public User deleteUser(User user) {
        userDAO.delete(user);
        return user;
    }
}
