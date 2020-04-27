package com.upgrad.reddit.api.controller;

import com.upgrad.reddit.api.model.*;
import com.upgrad.reddit.service.business.CommentBusinessService;
import com.upgrad.reddit.service.entity.CommentEntity;
import com.upgrad.reddit.service.entity.PostEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.CommentNotFoundException;
import com.upgrad.reddit.service.exception.InvalidPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/")
public class CommentController {


    @Autowired
    private CommentBusinessService commentBusinessService;

    /**
     * A controller method to post an comment to a specific post.
     *
     * @param commentRequest - This argument contains all the attributes required to store comment details in the database.
     * @param postId    - The uuid of the post whose comment is to be posted in the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<CommentResponse> type object along with Http status CREATED.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */

//    @RequestMapping (method= RequestMethod.POST, path= "/comment/create" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
//    public ResponseEntity<CommentResponse> createComment(final CommentRequest commentRequest, @PathVariable("/comment/create") String postId, @RequestHeader("authorization") String authorization)throws AuthorizationFailedException, InvalidPostException{
//        final CommentEntity commentEntity = commentBusinessService.createComment(commentRequest, postId, authorization);
//        CommentResponse commentResponse = new CommentResponse().id(commentEntity.getUuid()).status("Comment CREATED");
//        return new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.CREATED);
//    }

    /**
     * A controller method to edit an comment in the database.
     *
     * @param commentEditRequest - This argument contains all the attributes required to store edited comment details in the database.
     * @param commentId          - The uuid of the comment to be edited in the database.
     * @param authorization     - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<CommentEditResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws CommentNotFoundException
     */

//    @RequestMapping (method= RequestMethod.PUT, path= "/comment/edit/{commentId}" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
//    public ResponseEntity<CommentEditResponse> editCommentContent(final CommentEditRequest commentEditRequest, @PathVariable("commentId") String commentId, @RequestHeader("authorization") String authorization)throws AuthorizationFailedException, CommentNotFoundException{
//        final CommentEntity commentEntity = commentBusinessService.editCommentContent(commentEditRequest, commentId, authorization);
//        CommentEditResponse commentEditResponse = new CommentEditResponse().id(commentEntity.getUuid()).status("COMMENT EDITED");
//        return new ResponseEntity<CommentEditResponse>(commentEditResponse, HttpStatus.OK);
//    }
    /**
     * A controller method to delete an comment in the database.
     *
     * @param commentId      - The uuid of the comment to be deleted in the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<CommentDeleteResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws CommentNotFoundException
     */
//    @RequestMapping (method= RequestMethod.DELETE, path= "/comment/delete/{commentId}" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
//    public ResponseEntity<CommentDeleteResponse> deleteComment(@PathVariable("commentId") String commentId, @RequestHeader("authorization") String authorization)throws AuthorizationFailedException, CommentNotFoundException{
//        final CommentEntity commentEntity = commentBusinessService.deleteComment(commentId, authorization);
//        CommentDeleteResponse commentDeleteResponse = new CommentDeleteResponse().id(commentEntity.getUuid()).status("COMMENT DELETED");
//        return new ResponseEntity<CommentDeleteResponse>(commentDeleteResponse, HttpStatus.OK);
//    }

    /**
     * A controller method to fetch all the comments for a specific post in the database.
     *
     * @param postId    - The uuid of the post whose comments are to be fetched from the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<List<CommentDetailsResponse>> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */

//    @RequestMapping(method = RequestMethod.GET, path = "comment/all/{postId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<List<CommentDetailsResponse>> getAllCommentsToPost (@PathVariable("postId") String postId, @RequestHeader(value="authorization") String authorization) throws AuthorizationFailedException, InvalidPostException {
//        final CommentEntity commentEntity = (CommentEntity) commentBusinessService.getCommentsByPost(postId, authorization);
//        CommentDetailsResponse commentDetailsResponse = new CommentDetailsResponse().id(commentEntity.getUuid()).commentContent(commentEntity.getComment()).getCommentContent();
//        return new ResponseEntity<List<CommentDetailsResponse>>((List<CommentDetailsResponse>) commentDetailsResponse,HttpStatus.OK);
//    }
}