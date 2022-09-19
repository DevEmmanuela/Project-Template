package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.request.UsersRequest;
import com.emmanuela.newecommerce.entities.Users;
import com.emmanuela.newecommerce.response.UsersResponse;
import com.emmanuela.newecommerce.security.oauth.CustomOAuth2User;
import com.emmanuela.newecommerce.validationtoken.ConfirmationToken;
import org.springframework.security.core.Authentication;

public interface UsersService {
    String registerUser(UsersRequest usersRequest);
    void saveToken(String token, Users users);
    void deleteUnverifiedToken(ConfirmationToken token);
    void enableUser(String email);
    UsersResponse getUser();
    String getUsername();

    void createOAuthUser(CustomOAuth2User oAuth2User, Authentication authentication);
    void updateOAuth(Users users, CustomOAuth2User oAuth2User, Authentication authentication);
}
