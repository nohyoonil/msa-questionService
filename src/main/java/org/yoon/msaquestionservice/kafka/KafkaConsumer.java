package org.yoon.msaquestionservice.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.yoon.msaquestionservice.Question;
import org.yoon.msaquestionservice.QuestionRepository;
import org.yoon.msaquestionservice.model.dto.MemberPlusPointsDto;
import org.yoon.msaquestionservice.model.dto.QuestionAndMemberDto;

import java.time.Duration;


@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final QuestionRepository questionRepository;

    @KafkaListener(topics = "validate.questionId", groupId = "question-service")
    public void validateQuestionId(String message) {
        try {
            Long questionId = objectMapper.readValue(message, Long.class);
            questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다. ID: " + questionId));

            redisTemplate.opsForValue().set("questionId:" + questionId, "exist", Duration.ofSeconds(3));
        } catch (JsonProcessingException e) {
            System.err.println("[❌ JSON 파싱 실패] " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[❌ 처리 중 오류 발생] " + e.getMessage());
        }
    }

    @KafkaListener(topics = "question.plusVotedSum", groupId = "question-service")
    public void listen(String message) {
        try {
            QuestionAndMemberDto dto = objectMapper.readValue(message, QuestionAndMemberDto.class);
            Question question = questionRepository.findById(dto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다. ID: " + dto.getQuestionId()));

            question.plusVotedSum();
            questionRepository.save(question);

            MemberPlusPointsDto rewardDto = new MemberPlusPointsDto(dto.getMemberId(), question.getReward());
            kafkaTemplate.send("member.plusPoints", objectMapper.writeValueAsString(rewardDto));
        } catch (JsonProcessingException e) {
            System.err.println("[❌ JSON 파싱 실패] " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[❌ 처리 중 오류 발생] " + e.getMessage());
        }
    }
}