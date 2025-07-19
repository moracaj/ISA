package com.project.onlybuns.controller;

import com.project.onlybuns.service.UnregisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ActivationController {

    private final UnregisteredUserService unregisteredUserService;

    @Autowired
    public ActivationController(UnregisteredUserService unregisteredUserService) {
        this.unregisteredUserService = unregisteredUserService;
    }

}
