package org.yoon.msaquestionservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberPlusPointsDto {

    private long memberId;
    private long reward;
}
