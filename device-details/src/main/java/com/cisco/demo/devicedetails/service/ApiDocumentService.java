package com.cisco.demo.devicedetails.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
@Controller
public class ApiDocumentService {

    @GetMapping(value = "/api-ui")
    public ModelAndView redirectToSwaggerPrefix() {
        return new ModelAndView(new RedirectView("/swagger-ui/index.html", true, false));
    }
}
