package com.charles.study.springbase.web;

import com.charles.study.springbase.config.auth.LoginUser;
import com.charles.study.springbase.config.auth.dto.SessionUser;
import com.charles.study.springbase.service.posts.PostsService;
import com.charles.study.springbase.web.dto.PostsResponseDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    public IndexController(PostsService postsService, HttpSession httpSession) {
        this.postsService = postsService;
        this.httpSession = httpSession;
    }

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());

        // 앞서 작성된 CustomOAuth2UserService 에서 로그인 성공 시 세션에 SessionUser를 저장하도록 구성함.
        // @LoginUser 로 파라미터로 가져옴. 반복 코드 제거
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
