package com.upgrad.reddit.api.controller;

import com.upgrad.reddit.api.model.*;
import com.upgrad.reddit.service.business.PostBusinessService;
import com.upgrad.reddit.service.entity.PostEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.InvalidPostException;
import com.upgrad.reddit.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostBusinessService postBusinessService;

    //CreatePost
    @RequestMapping (method= RequestMethod.POST, path= "/create" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest, @RequestHeader(value="authorization") final String authorization) throws AuthorizationFailedException {
        String [] bearerToken = authorization.split("Bearer ");
        PostEntity postEntity = new PostEntity();
        postEntity.setUuid(UUID.randomUUID().toString());
        postEntity.setContent(postRequest.getContent());
        postEntity.setDate(ZonedDateTime.now());
        PostEntity postEntity1 = postBusinessService.createPost(postEntity, authorization);
        PostResponse postResponse = new PostResponse().id(postEntity.getUuid()).status("POST CREATED");
        return new ResponseEntity<PostResponse>(postResponse, HttpStatus.CREATED);
    }

    // getAllPosts
    @RequestMapping(method = RequestMethod.GET, path = "/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<PostDetailsResponse>> getAllPosts (@RequestHeader(value="authorization") final String authorization) throws AuthorizationFailedException {
        String [] bearerToken = authorization.split("Bearer ");
        TypedQuery<PostEntity> postList = postBusinessService.getPosts(bearerToken[0]);
        List<PostEntity> resultList = postList.getResultList();
        List<PostDetailsResponse> responseList = resultList.stream()
                .map(post -> {
                    PostDetailsResponse response = new PostDetailsResponse();
                    response.setContent(post.getContent());
                    response.setId(post.getUuid());
                    return response;
                }).collect(Collectors.toList());
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    // editPostContent
        @RequestMapping (method= RequestMethod.PUT, path= "/edit/{postId}" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
        public ResponseEntity<PostEditResponse> editPostContent(@RequestBody PostEditRequest postEditRequest, @PathVariable("postId") String postId, @RequestHeader("authorization") final String authorization)throws AuthorizationFailedException, InvalidPostException{
            String [] bearerToken = authorization.split("Bearer ");
            final PostEntity postEntity = new PostEntity();
            postEditRequest.setContent(postEditRequest.getContent());
            final PostEntity postEntity1 = postBusinessService.editPostContent(postEntity,postId,bearerToken[0]);
            PostEditResponse postEditResponse = new PostEditResponse().id(postEntity1.getUuid()).status("POST EDITED");
            return new ResponseEntity<PostEditResponse>(postEditResponse, HttpStatus.OK);
        }

        //deletePost
    @RequestMapping (method= RequestMethod.DELETE, path= "/delete/{postId}" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PostDeleteResponse> deletePost (@PathVariable("postId") String postId, @RequestHeader("authorization") final String authorization)throws AuthorizationFailedException, InvalidPostException{
        String [] bearerToken = authorization.split("Bearer ");
        final PostEntity postEntity = postBusinessService.deletePost(postId, bearerToken[0]);
        PostDeleteResponse postDeleteResponse = new PostDeleteResponse().id(postEntity.getUuid()).status("POST DELETED");
        return new ResponseEntity<PostDeleteResponse>(postDeleteResponse, HttpStatus.OK);
    }

    // getAllPostsByUser
    @RequestMapping (method= RequestMethod.GET, path= "/all/{userId}" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<PostDetailsResponse>> getAllPostsByUser(@PathVariable("userId") String userId, @RequestHeader("authorization") final String authorization)throws AuthorizationFailedException, UserNotFoundException{
        String [] bearerToken = authorization.split("Bearer ");
        final PostEntity postEntity = (PostEntity) postBusinessService.getPostsByUser(userId, bearerToken[0]);
        PostDetailsResponse postDetailsResponse = new PostDetailsResponse().id(postEntity.getUuid()).content(postEntity.getContent());
        return new ResponseEntity<List<PostDetailsResponse>>((List<PostDetailsResponse>) postDetailsResponse, HttpStatus.OK);
    }
}