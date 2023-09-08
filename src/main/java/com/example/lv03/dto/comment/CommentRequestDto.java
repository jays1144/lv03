package com.example.lv03.dto.comment;

import com.example.lv03.entity.Board;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String comment;
    private Board boardId;
}
