package com.backend.heathos.controllers;

import com.backend.heathos.auth.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = ("/api/v1/health"))
public class HealthController {
    @GetMapping
    public List<User> getUser(){
        User one = new User();
        one.setFullName("Ama Doe");
        User two = new User();
        two.setFullName("Kojo Asani");

        return List.of(one, two);
    }
}
