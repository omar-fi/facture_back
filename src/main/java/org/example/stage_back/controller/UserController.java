package org.example.stage_back.controller;

import org.example.stage_back.entities.AgentInscrit;
import org.example.stage_back.entities.User;
import org.example.stage_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public AgentInscrit register(@RequestBody AgentInscrit user) {

        return userService.registerUser(user);
    }
} 