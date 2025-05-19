package org.yoon.msaquestionservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.yoon.msaquestionservice.Question;

@Getter
@Builder
@AllArgsConstructor
public class QuestionDetailRes {

    private Long id;
    private String content;
    private int reward; //투표에 대한 보상
    private int votedSum; //해당 질문 총 투표수

    public static QuestionDetailRes from(Question question) {
        return QuestionDetailRes.builder()
                .id(question.getId())
                .content(question.getContent())
                .reward(question.getReward())
                .votedSum(question.getVotedSum())
                .build();
    }
}
