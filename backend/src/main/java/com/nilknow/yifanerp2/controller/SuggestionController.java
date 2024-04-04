package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Suggestion;
import com.nilknow.yifanerp2.repository.SuggestionRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequestMapping("/api/suggestion")
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

    @PostMapping
    public Res<Suggestion> add(@RequestBody Suggestion suggestion){
        suggestion.setCreateTime(new Date());
        suggestionRepository.save(suggestion);
        return new Res<Suggestion>().success(suggestion);
    }

    @GetMapping("/list")
    public Res<List<Suggestion>> list() {
        return new Res<List<Suggestion>>()
                .success(suggestionRepository.findAllByOrderByCreateTimeDesc());
    }
}
