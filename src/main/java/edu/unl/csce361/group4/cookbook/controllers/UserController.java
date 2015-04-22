/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.controllers;

import edu.unl.csce361.group4.cookbook.User;
import edu.unl.csce361.group4.cookbook.dao.UserDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mstark
 */
@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserDAO userDAO;
    
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private SecurityContextRepository securityContextRepository;
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User loginUser(@RequestParam("username") String userName,
            @RequestParam("password") String password,
            HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);
        try{ 
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(auth);
            securityContextRepository.saveContext(context, request, response);
            return userDAO.login(userName, password);
        } catch(BadCredentialsException e) {
            return null;
        }
    }
    
    @RequestMapping(value = "/createUser", method = RequestMethod.POST, consumes="application/json")
    public User createUser(@RequestBody User user) {
        userDAO.create(user);
        return user;
    }
    
    @RequestMapping(value = "/modifyUser", method = RequestMethod.POST, consumes="application/json")
    public User modifyUser(@RequestBody User user) {
        userDAO.modify(user);
        return user;
    }
    
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST, consumes="application/json")
    @PreAuthorize("isAuthenticated()")
    public User deleteUser(@RequestBody User user) {
        userDAO.delete(user);
        return user;
    }
}
