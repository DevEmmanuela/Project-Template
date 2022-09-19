package com.emmanuela.newecommerce.security.oauth;

import com.emmanuela.newecommerce.entities.Users;
import com.emmanuela.newecommerce.repository.UsersRepository;
import com.emmanuela.newecommerce.services.serviceimpl.UsersServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UsersServiceImpl usersService;
    private final UsersRepository usersRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException{
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();

        Users users = usersRepository.findUsersByEmail(email);
        if(users == null){
            usersService.createOAuthUser(oAuth2User, authentication);
        }
        else{
            usersService.updateOAuth(users, oAuth2User, authentication);
        }

        response.sendRedirect("/api/v1/oauth/index");
    }
}
