package com.example.lv03.service;

import com.example.lv03.dto.MsgResponseDto;
import com.example.lv03.dto.comment.CommentRequestDto;
import com.example.lv03.dto.comment.CommentResponseDto;
import com.example.lv03.dto.comment.ModifiedCommentRequestDto;
import com.example.lv03.entity.Board;
import com.example.lv03.entity.Comment;
import com.example.lv03.entity.User;
import com.example.lv03.jwt.JwtUtil;
import com.example.lv03.repository.BoardRepository;
import com.example.lv03.repository.CommentRepository;
import com.example.lv03.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public Comment createComment(String token, CommentRequestDto requestDto) {
        String username = tokenCheck(token);
        Optional<Board> answer = boardRepository.findById(requestDto.getBoardId().getId());
        if(!answer.isPresent()){
            throw new IllegalArgumentException("해당 게시물이 존재하지 않습니다");
        }
        Comment comment = new Comment(requestDto,username);

        return commentRepository.save(comment);

    }

    @Transactional
    public Comment updateComment(String token, ModifiedCommentRequestDto requestDto) {
        String username = tokenCheck(token);
        Comment comment = findComment(requestDto.getId(),username);
        System.out.println("comment.getComment() = " + comment.getComment());
        System.out.println("requestDto.getComments() = " + requestDto.getComments());
        comment.update(requestDto,comment);

        return comment;
    }

    public MsgResponseDto deleteComment(String token, Long id) {
        String username = tokenCheck(token);
        Comment comment = findComment(id,username);
        commentRepository.delete(comment);
        return new MsgResponseDto("삭제 성공","200");
    }


    public String tokenCheck(String tokenValue){
        String token = jwtUtil.substringToken(tokenValue);
        if(jwtUtil.validateToken(token).getMsg().equals("400")){
            throw new IllegalArgumentException("토큰문제 발견");
        }

        Claims info = jwtUtil.getUserInfoFromToken(token);
        String username = info.getSubject();

        return username;
    }

    private Comment findComment(Long id,String username){

        User user = userRepository.findByUsername(username).orElseThrow();
        System.out.println("user.getRole() = " + user.getRole());
        if(user.getRole().equals("ADMIN")){
            return commentRepository.findById(id).orElseThrow();
        }
        return commentRepository.findById(id).orElseThrow();
    }



}
