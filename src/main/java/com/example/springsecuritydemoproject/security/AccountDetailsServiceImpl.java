package com.example.springsecuritydemoproject.security;

import com.example.springsecuritydemoproject.repository.UserRepository;
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
                .map(account ->
                        AppUserDetails.builder()
                                .id(account.getId())
                                .email(account.getEmail())
                                .password(account.getPassword())
                                .authorities(account.getRole().getGrantedAuthorities())
                                .build())
                .orElse(null);
    }

}
