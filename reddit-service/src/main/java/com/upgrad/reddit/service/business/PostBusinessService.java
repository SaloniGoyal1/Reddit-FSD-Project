package com.upgrad.reddit.service.business;


import com.upgrad.reddit.service.dao.PostDao;
import com.upgrad.reddit.service.dao.UserDao;
import com.upgrad.reddit.service.entity.PostEntity;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.InvalidPostException;
import com.upgrad.reddit.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;

@Service
public class PostBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;


    /**
     * The method implements the business logic for createPost endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public PostEntity createPost(com.upgrad.reddit.api.model.PostRequest postEntity, String authorization) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity != null){
            ZonedDateTime logout = userAuthEntity.getLogoutAt();
            if (logout != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a post");
            }
        }
        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }

    /**
     * The method implements the business logic for getAllPosts endpoint.
     */
    public TypedQuery<PostEntity> getPosts(String authorization) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity != null){
            ZonedDateTime logout = userAuthEntity.getLogoutAt();
            if (logout != null) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all posts");
            }
        }
        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }

    /**
     * The method implements the business logic for editPostContent endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public PostEntity editPostContent(com.upgrad.reddit.api.model.PostEditRequest postEntity, String postId, String authorization) throws AuthorizationFailedException, InvalidPostException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        return null;
    }

    /**
     * The method implements the business logic for deletePost endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public PostEntity deletePost(String postId, String authorization) throws AuthorizationFailedException, InvalidPostException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        return null;
    }

    /**
     * The method implements the business logic for getAllPostsByUser endpoint.
     */
    public TypedQuery<PostEntity> getPostsByUser(String userId, String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        return null;
    }

}
