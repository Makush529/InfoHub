package com.IH.controller;


import com.IH.model.dto.PostResponse;
import com.IH.model.dto.RequestPostDTO;
import com.IH.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/create")
    public String getCreateForm(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/security/login";
        }
        return "create-post";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute RequestPostDTO dto, HttpSession session) throws SQLException {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/security/login";

        postService.createPost(dto, userId);
        return "redirect:/";

    }
    @GetMapping({"/feed","/"})
    public String getFeed(Model model) throws SQLException {
        try {
            List<PostResponse> posts = postService.getFeed();
            model.addAttribute("postsList", posts);
            return "index"; // или "feed"
        } catch (Exception e) {
            e.printStackTrace(); // <--- СМОТРИ СЮДА!
            return "error"; // Переходим на страницу ошибки, как обычно
        }
    }
}
