package com.IH.service;

import com.IH.model.dto.PostResponse;
import com.IH.model.dto.RequestPostDTO;
import com.IH.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;

    }
    public void createPost(RequestPostDTO dto, Long userId) throws SQLException {
        //проверка текста на мат или запрещенку
        postRepository.savePost(dto.getPostTitle(), dto.getText(), userId);
    }

    public List<PostResponse> getFeed() throws SQLException {
        return postRepository.findAll();
    }
}
