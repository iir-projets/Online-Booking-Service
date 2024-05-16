package org.example.myspringapp.Repositories;

import org.example.myspringapp.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllBy();
}
