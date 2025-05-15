package org.yoon.msaquestionservice.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.yoon.msaquestionservice.Question;
import org.yoon.msaquestionservice.QuestionRepository;
import org.yoon.msaquestionservice.model.dto.VoteDetailDto;


@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;
    private final QuestionRepository questionRepository;

    @KafkaListener(topics = "validate.question", groupId = "question-service")
    public void listen(String message) {
        try {
            VoteDetailDto voteDetail = objectMapper.readValue(message, VoteDetailDto.class);
            Long questionId = voteDetail.getQuestionId();
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다. ID: " + questionId));

            question.plusVotedSum();
            questionRepository.save(question);
            voteDetail.setRewards(question.getReward());
            kafkaProducer.send("validate.member", objectMapper.writeValueAsString(voteDetail));

        } catch (JsonProcessingException e) {
            System.err.println("[❌ JSON 파싱 실패] " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[❌ 처리 중 오류 발생] " + e.getMessage());
        }
    }

}