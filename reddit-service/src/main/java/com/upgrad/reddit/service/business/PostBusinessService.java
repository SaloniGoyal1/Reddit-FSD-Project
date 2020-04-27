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
    public PostEntity createPost(PostEntity postEntity, String authorization) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a post");
        }
        else {
            postEntity.setUser(userAuthEntity.getUser());
            return this.postDao.createPost(postEntity);
        }
    }

    /**
     * The method implements the business logic for getAllPosts endpoint.
     */
    public TypedQuery<PostEntity> getPosts(String authorization) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all posts");
        }
        else {
            return this.postDao.getPosts();
        }
    }

    /**
     * The method implements the business logic for editPostContent endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public PostEntity editPostContent(PostEntity postEntity, String postId, String authorization) throws AuthorizationFailedException, InvalidPostException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the post");
        }
        else {
            PostEntity postEntity1 = this.postDao.getPostByUuid(postId);
            if (postEntity1 == null) {
                throw new InvalidPostException("POS-001", "Entered post uuid does not exist");
            }
            else if (userAuthEntity.getUser() == postEntity1.getUser()) {
                postEntity.setUser(postEntity1.getUser());
                postEntity.setDate(postEntity1.getDate());
                postEntity.setUuid(postEntity1.getUuid());
                postEntity.setId(postEntity1.getId());
                return this.postDao.editPost(postEntity);
            }
            else {
                throw new AuthorizationFailedException("ATHR-003", "Only the post owner can edit the post");
            }
        }
    }

    /**
     * The method implements the business logic for deletePost endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public PostEntity deletePost(String postId, String authorization) throws AuthorizationFailedException, InvalidPostException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a post");
        }
        else {
            PostEntity postEntity = this.postDao.getPostByUuid(postId);
            if (postEntity == null) {
                throw new InvalidPostException("POS-001", "Entered post uuid does not exist");
            }
            else if (userAuthEntity.getUser() != postEntity.getUser() && !userAuthEntity.getUser().getRole().equals("nonadmin")) {
                throw new AuthorizationFailedException("ATHR-003", "Only the post owner or admin can delete the post");
            }
            else {
                return this.postDao.deletePost(postEntity);
            }
        }
    }

    /**
     * The method implements the business logic for getAllPostsByUser endpoint.
     */
    public TypedQuery<PostEntity> getPostsByUser(String userId, String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all posts posted by a specific user");
        }
        else {
            UserEntity userEntity = this.postDao.getUserByUuid(userId);
            if (userEntity == null) {
                throw new UserNotFoundException("USR-001", "User with entered uuid whose post details are to be seen does not exist");
            }
            else {
                return this.postDao.getPostsByUser(userEntity);
            }
        }
    }

}
