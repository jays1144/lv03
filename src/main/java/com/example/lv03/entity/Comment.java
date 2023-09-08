package com.example.lv03.entity;


import com.example.lv03.dto.comment.CommentRequestDto;
import com.example.lv03.dto.comment.ModifiedCommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String username;

    @ManyToOne()
    @JoinColumn(name = "board_id")
    private Board board_id;

    public Comment(CommentRequestDto requestDto, String username) {
        this.comment = requestDto.getComment();
        this.username = username;
        this.board_id = requestDto.getBoardId();
    }


    public void update(ModifiedCommentRequestDto requestDto,Comment comment) {
        this.comment = requestDto.getComments();
        this.board_id = comment.getBoard_id();
        this.username = comment.getUsername();
    }
}
