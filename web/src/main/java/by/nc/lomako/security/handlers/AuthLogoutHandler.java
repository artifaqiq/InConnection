/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.security.handlers;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Lomako
 * @version 1.0
 */
@Component
public class AuthLogoutHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
    }
}