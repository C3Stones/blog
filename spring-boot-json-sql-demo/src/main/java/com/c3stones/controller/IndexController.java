package com.c3stones.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页Controller
 *
 * @author CL
 */
@Controller
public class IndexController {

    /**
     * 首页
     *
     * @return
     */
    @GetMapping(value = "index")
    public String index() {
        return "index";
    }

}