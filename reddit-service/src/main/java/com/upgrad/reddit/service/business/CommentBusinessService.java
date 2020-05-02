package com.upgrad.reddit.service.business;

import com.upgrad.reddit.service.dao.CommentDao;
import com.upgrad.reddit.service.dao.UserDao;
import com.upgrad.reddit.service.entity.CommentEntity;
import com.upgrad.reddit.service.entity.PostEntity;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.CommentNotFoundException;
import com.upgrad.reddit.service.exception.InvalidPostException;
import com.upgrad.reddit.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Service
public class CommentBusinessService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private CommentDao commentDao;


    /**
     * The method implements the business logic for createComment endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity createComment(CommentEntity commentEntity, String commentId, String authorization) throws AuthorizationFailedException, InvalidPostException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an comment");
        }
        else {
            CommentEntity commentEntity1 = this.commentDao.getCommentByUuid(commentId);
            if (commentEntity1 == null) {
                throw new InvalidPostException("POS-001", "The post entered is invalid");
            }
            return this.commentDao.createComment(commentEntity);
        }
    }

    public PostEntity getPostByUuid(String Uuid) throws InvalidPostException {
        PostEntity postEntity = commentDao.getPostByUuid(Uuid);
            return postEntity;
    }


    /**
     * The method implements the business logic for editCommentContent endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity editCommentContent(CommentEntity commentEntity, String commentId, String authorization) throws AuthorizationFailedException, CommentNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an comment");
        }
        else {
            CommentEntity commentEntity1 = this.commentDao.getCommentByUuid(commentId);
            if (commentEntity1 == null) {
                throw new CommentNotFoundException("COM-001", "Entered comment uuid does not exist");
            }
            else if (userAuthEntity.getUser() == commentEntity1.getUser()) {
                commentEntity.setUser(commentEntity1.getUser());
                commentEntity.setDate(commentEntity1.getDate());
                commentEntity.setUuid(commentEntity1.getUuid());
                commentEntity.setId(commentEntity1.getId());
                return this.commentDao.editComment(commentEntity);
            }
            else {
                throw new AuthorizationFailedException("ATHR-003", "Only the comment owner can edit the comment");
            }
        }
    }

    /**
     * The method implements the business logic for deleteComment endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity deleteComment(String commentId, String authorization) throws AuthorizationFailedException, CommentNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a comment");
        }
        else {
            CommentEntity commentEntity = this.commentDao.getCommentByUuid(commentId);
            if (commentEntity == null) {
                throw new CommentNotFoundException("COM-001", "Entered comment uuid does not exist");
            }
            else if (userAuthEntity.getUser() != commentEntity.getUser() && !userAuthEntity.getUser().getRole().equals("nonadmin")) {
                throw new AuthorizationFailedException("ATHR-003", "Only the comment owner or admin can delete the comment");
            }
            else {
                return this.commentDao.deleteComment(commentEntity);
            }
        }
    }

    /**
     * The method implements the business logic for getAllCommentsToPost endpoint.
     */
    public TypedQuery<CommentEntity> getCommentsByPost(String postId, String authorization) throws AuthorizationFailedException, InvalidPostException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get the comments");
        }
        else {
            CommentEntity commentEntity = this.commentDao.getCommentByUuid(postId);
            if (commentEntity == null) {
                throw new InvalidPostException("POS-001", "The post with entered uuid whose details are to be seen does not exist");
            }
            else {
                PostEntity postEntity = new PostEntity();
                return this.commentDao.getCommentsByPost(postEntity);
            }
        }
    }
}
