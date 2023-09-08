package com.example.lv03.repository;

import com.example.lv03.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long>{

    List<Board> findAllByOrderByCreateAtDesc();

    List<Board> findAllByTitleContainsOrderByModifiedAtDesc(String id);

    Board findByIdAndUsername(Long id,String username);

    Optional<Board> findById(Long id);

}
