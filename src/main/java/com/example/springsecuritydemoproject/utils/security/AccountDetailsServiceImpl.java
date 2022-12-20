package com.example.springsecuritydemoproject.utils.security;

import com.example.springsecuritydemoproject.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user ->
                        AppUserDetails.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .password(user.getPassword())
                                .authorities(user.getRole().getGrantedAuthorities())
                                .enabled(user.isEnabled())
                                .locked(user.isLocked())
                                .build())
                .orElse(null);
    }

}
