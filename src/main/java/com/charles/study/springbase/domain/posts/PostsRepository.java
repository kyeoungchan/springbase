package com.charles.study.springbase.domain.posts;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// <Entity 클래스, PK 타입>
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    public List<Posts> findAllDesc();
}
