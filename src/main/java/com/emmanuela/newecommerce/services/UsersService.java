package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.response.UsersResponse;
import com.emmanuela.newecommerce.entities.Users;
import com.emmanuela.newecommerce.validationtoken.ConfirmationToken;

public interface UsersService {
    String registerUser(UsersResponse usersResponse);
    void saveToken(String token, Users users);
    void deleteUnverifiedToken(ConfirmationToken token);
    void enableUser(String email);
}
