package com.vn.iphoneshop.sercurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    // Cấu hình user trong bộ nhớ (dùng cho demo, development)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build());
        manager.createUser(User.withUsername("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build());
        return manager;
    }

    // Mã hoá password với BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình bảo mật HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Phân quyền cho các endpoint
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/login", "/register", "/css/**", "/js/**", "/images/**", "/api/public/**").permitAll() // public
                        .requestMatchers("/admin/**").hasRole("ADMIN") // chỉ admin mới vào được
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // user hoặc admin
                        .anyRequest().authenticated() // còn lại phải đăng nhập
                )
                // Cấu hình form login
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                // Cấu hình logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                // Nếu làm API thì nên disable CSRF
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // disable csrf cho các endpoint REST API
                )
                .rememberMe(remember -> remember // cấu hình remember-me (giữ đăng nhập)
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400) // 1 ngày
                );

        return http.build();
    }
}
