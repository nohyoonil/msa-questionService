package org.yoon.msaquestionservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yoon.msaquestionservice.model.response.QuestionDetailRes;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/questions/random")
    public ResponseEntity<QuestionDetailRes> getRandomQuestion() {
        return ResponseEntity.ok(questionService.getRandomQuestion());
    }
}
