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
import java.time.ZonedDateTime;

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
    public CommentEntity createComment(CommentEntity commentEntity, String authorization) throws AuthorizationFailedException, InvalidPostException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        if(userAuthEntity != null){
            ZonedDateTime logout = userAuthEntity.getLogoutAt();
            if (logout != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an comment");
            }
            CommentEntity commentEntity1 = commentDao.createComment(commentEntity);
            if (commentEntity1 == null){
                throw new InvalidPostException("POS-001", "The post entered is invalid");
            }
            return commentEntity1;
        }
        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }

    public PostEntity getPostByUuid(String Uuid) throws InvalidPostException {

        PostEntity postEntity = commentDao.getPostByUuid(Uuid);

        return null;
    }


    /**
     * The method implements the business logic for editCommentContent endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity editCommentContent(CommentEntity commentEntity, String commentId, String authorization) throws AuthorizationFailedException, CommentNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        return null;
    }

    /**
     * The method implements the business logic for deleteComment endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity deleteComment(String commentId, String authorization) throws AuthorizationFailedException, CommentNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        return null;
    }

    /**
     * The method implements the business logic for getAllCommentsToPost endpoint.
     */
    public TypedQuery<CommentEntity> getCommentsByPost(String postId, String authorization) throws AuthorizationFailedException, InvalidPostException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        return null;
    }
}
