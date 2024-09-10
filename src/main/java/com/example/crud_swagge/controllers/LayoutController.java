package com.example.crud_swagge.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/layout")
public class LayoutController {
    @Operation(summary = "get layout1", description = "list product of layout1")
    @GetMapping("/layout1")
    public String layout1() {
        return "layout1 page";
    }

    @GetMapping("/layout2")
    public String layout2() {
        return "layout2 page";
    }
}
