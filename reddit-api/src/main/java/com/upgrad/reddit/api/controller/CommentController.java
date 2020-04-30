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

@RestController
@RequestMapping("/")
public class CommentController {


    @Autowired
    private CommentBusinessService commentBusinessService;

    // createComment
    @RequestMapping (method= RequestMethod.POST, path= "/post/{postId}/comment/create" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<CommentResponse> createComment(final CommentEntity commentRequest, @PathVariable("postId") String postId, @RequestHeader("authorization") final String authorization)throws AuthorizationFailedException, InvalidPostException{
        String [] bearerToken = authorization.split("Bearer ");
        CommentEntity comment = new CommentEntity();
        comment.setComment(commentRequest.getComment());
        comment.setDate(ZonedDateTime.now());
        comment.setUuid(UUID.randomUUID().toString());
        final CommentEntity commentEntity = commentBusinessService.createComment(commentRequest, postId, bearerToken[0]);
        CommentResponse commentResponse = new CommentResponse().id(commentEntity.getUuid()).status("Comment CREATED");
        return new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.CREATED);
    }

    // editCommentContent
    @RequestMapping (method= RequestMethod.PUT, path= "/comment/edit/{commentId}" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<CommentEditResponse> editCommentContent(final CommentEditRequest commentEditRequest, @PathVariable("commentId") String commentId, @RequestHeader("authorization") final String authorization)throws AuthorizationFailedException, CommentNotFoundException{
        String [] bearerToken = authorization.split("Bearer ");
        final CommentEntity commentEntity = new CommentEntity();
        commentEntity.setComment(commentEditRequest.getContent());
        final CommentEntity commentEntity1 = commentBusinessService.editCommentContent(commentEntity, commentId, bearerToken[0]);
        CommentEditResponse commentEditResponse = new CommentEditResponse().id(commentEntity.getUuid()).status("COMMENT EDITED");
        return new ResponseEntity<CommentEditResponse>(commentEditResponse, HttpStatus.OK);
    }

    // deleteComment
    @RequestMapping (method= RequestMethod.DELETE, path= "/comment/delete/{commentId}" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<CommentDeleteResponse> deleteComment(@PathVariable("commentId") String commentId, @RequestHeader("authorization") final String authorization)throws AuthorizationFailedException, CommentNotFoundException{
        String [] bearerToken = authorization.split("Bearer ");
        final CommentEntity commentEntity = commentBusinessService.deleteComment(commentId, bearerToken[0]);
        CommentDeleteResponse commentDeleteResponse = new CommentDeleteResponse().id(commentEntity.getUuid()).status("COMMENT DELETED");
        return new ResponseEntity<CommentDeleteResponse>(commentDeleteResponse, HttpStatus.OK);
    }

    // getAllCommentsToPost
    @RequestMapping(method = RequestMethod.GET, path = "comment/all/{postId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<CommentDetailsResponse>> getAllCommentsToPost (@PathVariable("postId") String postId, @RequestHeader(value="authorization") final String authorization) throws AuthorizationFailedException, InvalidPostException {
        String [] bearerToken = authorization.split("Bearer ");
        final CommentEntity commentEntity = (CommentEntity) commentBusinessService.getCommentsByPost(postId, bearerToken[0]);
        CommentDetailsResponse commentDetailsResponse = new CommentDetailsResponse();
        commentDetailsResponse.id(commentEntity.getUuid());
        commentDetailsResponse.commentContent(commentEntity.getComment());
        commentDetailsResponse.getCommentContent();
        return new ResponseEntity<List<CommentDetailsResponse>>((List<CommentDetailsResponse>) commentDetailsResponse,HttpStatus.OK);
    }
}