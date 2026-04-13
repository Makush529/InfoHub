package com.IH.controller;

import com.IH.model.dto.request.CreatePostRequest;
import com.IH.model.dto.responce.PostDto;
import com.IH.model.dto.responce.TagDto;
import com.IH.repository.TagRepository;
import com.IH.service.PostService;
import com.IH.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/posts")
@Tag(name = "Posts", description = "Managing posts: creating, viewing, liking")
public class PostController {

    private final PostService postService;
    private final TagRepository tagRepository;
    private final AuthUtil authUtil;

    @Autowired
    public PostController(PostService postService, TagRepository tagRepository, AuthUtil authUtil) {
        this.postService = postService;
        this.tagRepository = tagRepository;
        this.authUtil = authUtil;
    }

    @PostMapping()
    @Operation(summary = "Create Post"
            , description = "Creates a post and submits it for moderation. Authorization required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"
                    , description = "The post has been successfully created and sent for moderation."),
            @ApiResponse(responseCode = "401"
                    , description = "The user is not authorized"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest postRequest, HttpServletRequest request) {
        log.debug(">> post title {} ", postRequest.getTitle());
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.warn("<< UNAUTHORIZED: user unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
        }
        Optional<Long> postId = postService.createPost(postRequest, userId);
        if (postId.isPresent()) {
            log.debug("<< OK:Post has been successfully created and sent for moderation.");
            return ResponseEntity.status(HttpStatus.OK).body("Post created, id: " + postId.get());
        } else {
            log.warn("<< BAD_REQUEST: Post could not be created");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post could not be created");
        }
    }

    @GetMapping()
    @Operation(summary = "Get all published posts",
            description = "Returns a list of posts with the APPROVED status. " +
                    "For authorized users, it also shows whether the user has liked or disliked the post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of posts has been successfully retrieved.")
    })
    public ResponseEntity<List<PostDto>> getAllPosts(HttpServletRequest request) {
        log.debug(">> GetAllPosts, user id = {}", request.getAttribute("userId"));
        Long userId = (Long) request.getAttribute("userId");
        List<PostDto> posts = postService.getAllPublishedPosts(userId);
        log.debug("<<OK: Posts retrieved successfully, {}", posts.size());
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find post by id"
            , description = "Returns detailed information about a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id, HttpServletRequest request) {
        log.debug(">> GetPostById, post id = {}", id);
        Long userId = (Long) request.getAttribute("userId");
        Optional<PostDto> post = postService.getPostById(id, userId);
        if (post.isPresent()) {
            log.debug("<< OK: Post has been successfully retrieved.");
            return ResponseEntity.status(HttpStatus.OK).body(post.get());
        } else {
            log.warn("<< NOT_FOUND: Post could not be retrieved.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "Like"
            , description = "Adds a like to the post. If the user already disliked it,"
            + " it is replaced with a like")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like successfully placed"),
            @ApiResponse(responseCode = "401", description = "Authorization required"),
            @ApiResponse(responseCode = "400", description = "Error placing like")
    })
    public ResponseEntity<?> addLike(@PathVariable Long id, HttpServletRequest request) {
        log.debug(">> addLike, post id = {}", id);
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.warn("<< UNAUTHORIZED: user unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        boolean success = postService.addLike(id, userId);
        if (success) {
            log.debug("<< OK: Post has been successfully liked.");
            return ResponseEntity.status(HttpStatus.OK).body(success);
        } else {
            log.warn("<< BAD_REQUEST: Post could not be liked.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(success);
        }
    }

    @PostMapping("/{id}/dislike")
    @Operation(summary = "Dislike"
            , description = "Adds a Dislike to the post. If the user already liked it,"
            + " it is replaced with a Dislike")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dislike successfully placed"),
            @ApiResponse(responseCode = "401", description = "Authorization required"),
            @ApiResponse(responseCode = "400", description = "Error placing like")
    })
    public ResponseEntity<?> removeLike(@PathVariable Long id, HttpServletRequest request) {
        log.debug(">> add dislike, post id = {}", id);
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.warn("<< UNAUTHORIZED: user unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        boolean success = postService.addDislike(id, userId);
        if (success) {
            log.debug("<< OK: Dislike successfully placed");
            return ResponseEntity.status(HttpStatus.OK).body(success);
        } else {
            log.warn("<< BAD_REQUEST: Post could not be liked.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(success);
        }
    }

    @PostMapping("/{id}/reaction")
    @Operation(summary = "Deleted reaction", description = "Removes a user's like or dislike from a post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "reaction deleted"),
            @ApiResponse(responseCode = "401", description = "Authorization required"),
            @ApiResponse(responseCode = "400", description = "No reaction found to remove")
    })
    public ResponseEntity<?> removeReaction(@PathVariable Long id, HttpServletRequest request) {
        log.debug(">> removeReaction, post id = {}", id);
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.warn("<< UNAUTHORIZED: user unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        boolean success = postService.removeReaction(id, userId);
        if (success) {
            log.debug("<< OK: Reaction has been successfully removed.");
            return ResponseEntity.status(HttpStatus.OK).body(success);
        } else {
            log.warn("<< BAD_REQUEST: Reaction could not be removed.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(success);//TODO проверить, может 404
        }
    }

    @GetMapping("/tags")
    @Operation(
            summary = "Get all tags",
            description = "Returns a list of all tags with the number of posts associated with each tag. " +
                    "Does not require authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tags retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TagDto.class))
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TagDto>> getAllTags() {
        log.debug(">> get all tags");
        try{List<TagDto>tags =tagRepository.getAllTags();
            log.debug("<< tags were sent successfully {}}",tags.size());
        return ResponseEntity.status(HttpStatus.OK).body(tags);}
        catch(Exception e){
            log.error("<< Error getting all tags", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/tag/{tagName}")
    @Operation(
            summary = "Get posts by tag",
            description = "Returns all approved posts associated with the specified tag. " +
                    "Authentication is optional - shows user's likes/dislikes status if authenticated."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Posts retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PostDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Tag not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PostDto>>getPostsByTags(@PathVariable String tagName, HttpServletRequest request) {
        log.debug(">> get posts by tags {}", tagName);
        try{
            Long userId = (request!=null)?(Long) request.getAttribute("userId"):null;

            List <Long>postIds = tagRepository.getPostIdsByTagId(tagName);

            List<PostDto>posts=new ArrayList<>();
            for (Long postId : postIds) {
                Optional<PostDto>postDtoOptional = postService.getPostById(postId, userId);
                if (postDtoOptional.isPresent()) {
                    posts.add(postDtoOptional.get());
                }
            }
            log.debug("<<posts were sent successfully {}}",posts.size());
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        }catch (Exception e){
            log.error("<<Error getting posts by tags", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post", description = "Deletes a post. Author, moderator or admin can delete.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Access denied (not the author and not moderator/admin)"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Could not delete post (internal error)")
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> deletePost(@PathVariable Long id,
                                        HttpServletRequest request) {
        log.debug(">> delete post, post id = {}", id);
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            log.warn("<< UNAUTHORIZED: user unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to delete a post");
        }

        Optional<Long> authorIdOpt = postService.getPostAuthorId(id);
        if (authorIdOpt.isEmpty()) {
            log.warn("<< NO AUTHOR ID FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Post not found");
        }

        Long authorId = authorIdOpt.get();

        if (!authUtil.isOwner(userId, authorId)) {
            log.warn("<< User {} tried to delete post {} without permission", userId, id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("<< You are not the author of this post");
        }

        boolean deleted = postService.deletePost(id);

        if (deleted) {
            log.info("<< Post {} deleted by user {}", id, userId);
            return ResponseEntity.ok("Post deleted successfully");
        } else {
            log.info("<< Post {} could not be deleted", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<< Could not delete post");
        }
    }
}
