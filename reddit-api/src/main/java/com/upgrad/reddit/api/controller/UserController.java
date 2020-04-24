package com.upgrad.reddit.api.controller;

import com.upgrad.reddit.api.model.SigninResponse;
import com.upgrad.reddit.api.model.SignoutResponse;
import com.upgrad.reddit.api.model.SignupUserRequest;
import com.upgrad.reddit.api.model.SignupUserResponse;
import com.upgrad.reddit.service.business.UserBusinessService;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthenticationFailedException;
import com.upgrad.reddit.service.exception.SignOutRestrictedException;
import com.upgrad.reddit.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    /**
     * A controller method for user signup.
     *
     * @param signupUserRequest - This argument contains all the attributes required to store user details in the database.
     * @return - ResponseEntity<SignupUserResponse> type object along with Http status CREATED.
     * @throws SignUpRestrictedException
     */

    @RequestMapping (method= RequestMethod. POST, path= "/signup" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        final UserEntity userEntity=new UserEntity();

        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setSalt("123@4");
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setDob(signupUserRequest.getDob());
        userEntity.setRole("user");
        userEntity.setContactNumber(signupUserRequest.getContactNumber());

        final UserEntity createdUserEntity=userBusinessService.signup(userEntity);
        SignupUserResponse userResponse=new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupUserResponse>(userResponse,HttpStatus.CREATED);
    }

    /**
     * A controller method for user authentication.
     *
     * @param authorization - A field in the request header which contains the user credentials as Basic authentication.
     * @return - ResponseEntity<SigninResponse> type object along with Http status OK.
     * @throws AuthenticationFailedException
     */

    @RequestMapping (method= RequestMethod. POST, path= "/login" , consumes=MediaType. APPLICATION_JSON_UTF8_VALUE , produces=MediaType. APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<SigninResponse> authentication(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decode=Base64.getDecoder().decode(authorization.split("Basic")[1]);
        String decodedText=new String(decode);
        String[] decodedArray=decodedText.split(":");

        UserAuthEntity userAuthToken=userBusinessService.authenticate(decodedArray[0],decodedArray[1]);
        UserEntity user=userAuthToken.getUser();
        SigninResponse signinResponse=new SigninResponse().id(user.getUuid()).message("SIGNED IN SUCCESSFULLY");
        HttpHeaders header=new HttpHeaders();
        header.add("access-token",userAuthToken.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinResponse,header,HttpStatus.OK);
    }

    /**
     * A controller method for user signout.
     *
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<SignoutResponse> type object along with Http status OK.
     * @throws SignOutRestrictedException
     */

    @RequestMapping(method=RequestMethod.POST, path="/user/signout", consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType. APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userBusinessService.signout(authorization);
        SignoutResponse signoutResponse = new SignoutResponse().id(userAuthEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(signoutResponse,HttpStatus.OK);
    }

}
