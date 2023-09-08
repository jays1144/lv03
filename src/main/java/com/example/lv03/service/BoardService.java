package com.example.lv03.service;

import com.example.lv03.dto.board.BoardRequestDto;
import com.example.lv03.dto.board.BoardResponseDto;
import com.example.lv03.entity.Board;
import com.example.lv03.entity.User;
import com.example.lv03.jwt.JwtUtil;
import com.example.lv03.repository.BoardRepository;
import com.example.lv03.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public List<BoardResponseDto> getBoards() {
        return boardRepository.findAllByOrderByCreateAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    public BoardResponseDto create(String token, BoardRequestDto requestDto) {
        String username = tokenCheck(token);

        Board board = new Board(requestDto,username);

        Board saveBoard = boardRepository.save(board);

        BoardResponseDto boardResponseDto = new BoardResponseDto(saveBoard);

        return boardResponseDto;


    }

    public List<BoardResponseDto> getBoardsByKey(String id) {
        return boardRepository.findAllByTitleContainsOrderByModifiedAtDesc(id).stream().map(BoardResponseDto::new).toList();
    }



    @Transactional
    public Board update(String token, BoardRequestDto requestDto,Long id) {
       String username = tokenCheck(token);
       Board board = findBoard(id,username);
       board.update(requestDto,username);

       return board;
    }


    public String delete(String token, Long id) {
        String user = tokenCheck(token);
        Board board = findBoard(id,user);
        boardRepository.delete(board);
        return "삭제";
    }




    public String tokenCheck(String token){
        String subedToken = jwtUtil.substringToken(token);

        if(jwtUtil.validateToken(subedToken).getMsg().equals("400")){
            throw new IllegalArgumentException("토큰문제 발견");
        }

        Claims info = jwtUtil.getUserInfoFromToken(subedToken);

        String username = info.getSubject();
        System.out.println("info.getId() = " + info.getId());
        System.out.println("info.getAudience() = " + info.getAudience());
        System.out.println("username = " + username);

        return username;

    }



    private Board findBoard(Long id,String username){
        User user = userRepository.findByUsername(username).orElseThrow();
        System.out.println("user.getRole() = " + user.getRole());
        if(user.getRole().equals("ADMIN")){
            return boardRepository.findById(id).orElseThrow();
        }
        return boardRepository.findByIdAndUsername(id,username);
    }



}
