package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.dto.UsersDto;
import com.emmanuela.newecommerce.entities.Users;

public interface UsersService {
    String registerUser(UsersDto usersDto);
    void saveToken(String token, Users users);
}
