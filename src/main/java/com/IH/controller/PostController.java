package com.IH.controller;

import com.IH.model.dto.PostResponse;
import com.IH.model.dto.RequestPostDTO;
import com.IH.model.dto.rest.CreatePostRequest;
import com.IH.model.dto.rest.PostDto;
import com.IH.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@Tag(name ="Posts", description = "API for creating posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/create")
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest postRequest, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
        }
        Optional<Long>postId = postService.createPost(postRequest, userId);
        if (postId.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body("Post created, id: " + postId);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post could not be created");
        }
    }

    @GetMapping()
    public ResponseEntity<List<PostDto>> getAllPosts(HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        List<PostDto>posts =  postService.getAllPublishedPosts(userId);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        Optional<PostDto>post=postService.getPostById(id,userId);
        if (post.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(post.get());
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> addLike(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        if(userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        boolean success = postService.addLike(id, userId);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body(success);
        }else  {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(success);
        }
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<?> removeLike(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        if(userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        boolean success = postService.addDislike(id, userId);
        if(success){
            return ResponseEntity.status(HttpStatus.OK).body(success);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(success);
        }
    }

    @PostMapping("/{id}/reaction")
    public ResponseEntity<?> removeReaction(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        if(userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        boolean success = postService.removeReaction(id, userId);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body(success);
        }else  {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(success);
        }
    }
//Дописать
    @PostMapping("/create")
    public String createPostOld(@ModelAttribute RequestPostDTO dto, HttpSession session) throws SQLException {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/security/login";

        postService.createPostOld(dto, userId);
        return "redirect:/posts/feed";
    }

    @GetMapping({"/feed"})
    public String getFeed(Model model) throws SQLException {
        try {
            List<PostResponse> posts = postService.getFeed();
            model.addAttribute("postsList", posts);
            for (PostResponse post : posts) {
                System.out.println(post.getPostTitle());
                System.out.println(post.getText());
            }
            return "/feed";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
