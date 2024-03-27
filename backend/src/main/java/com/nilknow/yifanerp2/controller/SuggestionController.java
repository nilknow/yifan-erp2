package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Suggestion;
import com.nilknow.yifanerp2.repository.SuggestionRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequestMapping("/suggestion")
@RestController
public class SuggestionController {
    @Resource
    private SuggestionRepository suggestionRepository;

    @PostMapping("/submit")
    public String submit(String email,String phone,String content){
        Suggestion suggestion = new Suggestion();
        suggestion.setEmail(email);
        suggestion.setPhone(phone);
        suggestion.setContent(content);
        suggestion.setCreateTime(new Date());
        suggestionRepository.save(suggestion);
        if (suggestion.getId() != null) {
            return "success";
        } else {
            return "failed";
        }
    }
}
