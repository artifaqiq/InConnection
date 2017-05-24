/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.security.handlers;

import by.nc.lomako.dto.user.UserInfoDto;
import by.nc.lomako.security.details.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Lomako
 * @version 1.0
 */
@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ObjectMapper mapper;

    @Autowired
    AuthSuccessHandler(MappingJackson2HttpMessageConverter messageConverter) {
        this.mapper = messageConverter.getObjectMapper();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserInfoDto user = userDetails.getUser();

        PrintWriter writer = response.getWriter();
        mapper.writeValue(writer, user);
        writer.flush();
    }
}