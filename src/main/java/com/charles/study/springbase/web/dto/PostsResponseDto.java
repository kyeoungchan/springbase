package com.charles.study.springbase.web.dto;

import com.charles.study.springbase.domain.posts.Posts;
import lombok.Getter;

/**
 * 엔티티로만 받아서 생성한다.
 */
@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
