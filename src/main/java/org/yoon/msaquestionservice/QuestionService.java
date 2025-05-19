package org.yoon.msaquestionservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yoon.msaquestionservice.model.response.QuestionDetailRes;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionDetailRes getRandomQuestion() {
        return QuestionDetailRes.from(questionRepository.findRandomQuestion());
    }
}
