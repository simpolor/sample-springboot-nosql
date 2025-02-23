package io.simpolor.elasticsearch.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResultDto {

    private String id;

    public static ResultDto of(String id){
        return ResultDto.builder()
                .id(id)
                .build();
    }
}