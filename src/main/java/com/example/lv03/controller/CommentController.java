package com.example.lv03.controller;

import com.example.lv03.dto.MsgResponseDto;
import com.example.lv03.dto.comment.CommentRequestDto;
import com.example.lv03.dto.comment.ModifiedCommentRequestDto;
import com.example.lv03.entity.Comment;
import com.example.lv03.jwt.JwtUtil;
import com.example.lv03.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public Comment createComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String token, @RequestBody CommentRequestDto requestDto) {
        return commentService.createComment(token, requestDto);
    }

    @PutMapping("/comment")
    public Comment updateComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String token, @RequestBody ModifiedCommentRequestDto requestDto) {
        System.out.println("requestDto.getComments() = " + requestDto.getComments());
        return commentService.updateComment(token, requestDto);
    }

    @DeleteMapping("/comment")
    public MsgResponseDto deleteComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String token, @RequestParam Long id) {
        System.out.println("token = " + token);
        System.out.println("id = " + id);
        return commentService.deleteComment(token, id);
    }
}
