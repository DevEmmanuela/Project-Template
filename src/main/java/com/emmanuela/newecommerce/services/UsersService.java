package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.request.UsersRequest;
import com.emmanuela.newecommerce.entities.Users;
import com.emmanuela.newecommerce.response.UsersResponse;
import com.emmanuela.newecommerce.validationtoken.ConfirmationToken;

public interface UsersService {
    String registerUser(UsersRequest usersRequest);
    void saveToken(String token, Users users);
    void deleteUnverifiedToken(ConfirmationToken token);
    void enableUser(String email);
    UsersResponse getUser();
    String getUsername();
}
