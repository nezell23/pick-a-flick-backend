package com.pickaflick.authorizations;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.pickaflick.authorizations.AuthConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private AuthenticationManager authenticationManager;
  
  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
    try {
      com.pickaflick.models.User creds = new ObjectMapper()
        .readValue(req.getInputStream(), com.pickaflick.models.User.class);

      return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          creds.getUsername(),
          creds.getPassword(),
          new ArrayList<>())
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
    String token = JWT.create()
      .withSubject(((User) auth.getPrincipal()).getUsername())
      .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
      .sign(HMAC512(SECRET.getBytes()));
    res.addHeader(HEADER_STRING, token);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) throws IOException, ServletException {
    super.unsuccessfulAuthentication(req, res, failed);
  }
}