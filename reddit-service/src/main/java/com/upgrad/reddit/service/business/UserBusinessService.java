package com.upgrad.reddit.service.business;

import com.upgrad.reddit.service.common.JwtTokenProvider;
import com.upgrad.reddit.service.dao.UserDao;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthenticationFailedException;
import com.upgrad.reddit.service.exception.SignOutRestrictedException;
import com.upgrad.reddit.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.time.ZonedDateTime;
@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * The method implements the business logic for signup endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        if (userEntity != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }
        UserEntity userEntity1 =  userDao.getUserByEmail(userEntity.getEmail());
        if (userEntity1 != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }
        return userDao.createUser(userEntity);
    }

    /**
     * The method implements the business logic for signin endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity authenticate(String username, String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByUsername(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }
        if (password.equals(userEntity.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(password);
            UserAuthEntity userAuthEntity = new UserAuthEntity();
            userAuthEntity.setUser(userEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthEntity.setLoginAt(now);
            userAuthEntity.setExpiresAt(expiresAt);
            userAuthEntity.setUuid(UUID.randomUUID().toString());
            userDao.createUserAuth(userAuthEntity);
            userDao.updateUserAuth(userAuthEntity);
            return userAuthEntity;
        }
        else{
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    /**
     * The method implements the business logic for signout endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signout(String authorization) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity == null){
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        return userDao.signOut(userAuthEntity);
    }
}