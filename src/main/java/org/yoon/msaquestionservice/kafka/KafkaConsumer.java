package org.yoon.msaquestionservice.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.yoon.msaquestionservice.Question;
import org.yoon.msaquestionservice.QuestionRepository;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final QuestionRepository questionRepository;

    @KafkaListener(topics = "vote.created", groupId = "question-service")
    public void listen(String message) {
        System.out.println("[✅ 수신된 Kafka 메시지] " + message);

        try {
            HashMap<String, Object> map = objectMapper.readValue(message, HashMap.class);

            Long questionId = Long.valueOf(map.get("questionId").toString());

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다. ID: " + questionId));

            question.plusVotedSum();
            map.put("reward", question.getReward());

            System.out.println("[🧾 응답 데이터] " + map);

        } catch (JsonProcessingException e) {
            System.err.println("[❌ JSON 파싱 실패] " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[❌ 처리 중 오류 발생] " + e.getMessage());
        }
    }

}