package com.example.lv03.repository;

import com.example.lv03.entity.Board;
import com.example.lv03.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {

        Comment findByIdAndUsername(Long id, String username);

}
