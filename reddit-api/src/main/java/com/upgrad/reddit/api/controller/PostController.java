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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostBusinessService postBusinessService;

    /**
     * A controller method to create a post.
     *
     * @param postRequest - This argument contains all the attributes required to store post details in the database.
     * @param authorization   - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<PostResponse> type object along with Http status CREATED.
     * @throws AuthorizationFailedException
     */

    @RequestMapping (method= RequestMethod.POST, path= "/post/create" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
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

    /**
     * A controller method to fetch all the posts from the database.
     *
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<List<PostDetailsResponse>> type object along with Http status OK.
     * @throws AuthorizationFailedException
     */

    @RequestMapping(method = RequestMethod.GET, path = "/post/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<PostDetailsResponse>> getAllPosts (@RequestHeader(value="authorization") final String authorization) throws AuthorizationFailedException {
        String [] bearerToken = authorization.split("Bearer ");
        final PostEntity postEntity = (PostEntity) postBusinessService.getPosts(bearerToken[0]);
        PostDetailsResponse postDetailsResponse = new PostDetailsResponse().id(postEntity.getUuid()).content(postEntity.getContent());
        return new ResponseEntity<List<PostDetailsResponse>>((List<PostDetailsResponse>) postDetailsResponse,HttpStatus.OK);
    }

        /**
         * A controller method to edit the post in the database.
         *
         * @param postEditRequest - This argument contains all the attributes required to edit the post details in the database.
         * @param postId          - The uuid of the post to be edited in the database.
         * @param authorization       - A field in the request header which contains the JWT token.
         * @return - ResponseEntity<PostEditResponse> type object along with Http status OK.
         * @throws AuthorizationFailedException
         * @throws InvalidPostException
         */

        @RequestMapping (method= RequestMethod.PUT, path= "/post/edit/{postId}" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
        public ResponseEntity<PostEditResponse> editPostContent(@RequestBody PostEditRequest postEditRequest, @PathVariable("postId") String postId, @RequestHeader("authorization") final String authorization)throws AuthorizationFailedException, InvalidPostException{
            String [] bearerToken = authorization.split("Bearer ");
            final PostEntity postEntity = new PostEntity();
            postEditRequest.setContent(postEditRequest.getContent());
            final PostEntity postEntity1 = postBusinessService.editPostContent(postEntity,postId,bearerToken[0]);
            PostEditResponse postEditResponse = new PostEditResponse().id(postEntity1.getUuid()).status("POST EDITED");
            return new ResponseEntity<PostEditResponse>(postEditResponse, HttpStatus.OK);
        }

    /**
     * A controller method to delete the post in the database.
     *
     * @param postId    - The uuid of the post to be deleted in the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<PostDeleteResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */

    @RequestMapping (method= RequestMethod.DELETE, path= "/post/delete/{postId}" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PostDeleteResponse> deletePost (@PathVariable("postId") String postId, @RequestHeader("authorization") final String authorization)throws AuthorizationFailedException, InvalidPostException{
        String [] bearerToken = authorization.split("Bearer ");
        final PostEntity postEntity = postBusinessService.deletePost(postId, bearerToken[0]);
        PostDeleteResponse postDeleteResponse = new PostDeleteResponse().id(postEntity.getUuid()).status("POST DELETED");
        return new ResponseEntity<PostDeleteResponse>(postDeleteResponse, HttpStatus.OK);
    }

    /**
     * A controller method to fetch all the posts posted by a specific user.
     *
     * @param userId        - The uuid of the user whose posts are to be fetched from the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<List<PostDetailsResponse>> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */

    @RequestMapping (method= RequestMethod.GET, path= "post/all/{userId}" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<PostDetailsResponse>> getAllPostsByUser(@PathVariable("userId") String userId, @RequestHeader("authorization") final String authorization)throws AuthorizationFailedException, UserNotFoundException{
        String [] bearerToken = authorization.split("Bearer ");
        final PostEntity postEntity = (PostEntity) postBusinessService.getPostsByUser(userId, bearerToken[0]);
        PostDetailsResponse postDetailsResponse = new PostDetailsResponse().id(postEntity.getUuid()).content(postEntity.getContent());
        return new ResponseEntity<List<PostDetailsResponse>>((List<PostDetailsResponse>) postDetailsResponse, HttpStatus.OK);
    }
}
