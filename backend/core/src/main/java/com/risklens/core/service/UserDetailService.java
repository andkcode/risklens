package com.risklens.core.service;

import com.risklens.core.entity.User;
import com.risklens.core.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + email));

    List<GrantedAuthority> authorities =
        user.getRole() != null
            ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
            : Collections.emptyList();

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPasswordHash(), authorities);
  }
}
