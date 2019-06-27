/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 *
 * @author imaginifer
 */
@EnableWebSecurity
public class WebSecConfig extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .defaultSuccessUrl("/messaging", true)
                .permitAll()    //login oldal, amit mindenki elérhet
                .and().logout()         //logout oldal is
                .and().authorizeRequests()
                .antMatchers("/messaging").permitAll()  //ide szabad a hozzáférés
                .antMatchers("/register").permitAll()
                .antMatchers("/css/*","/js/*").permitAll()  //a static mappa nyílttá tétele, különben leesik a css
                //.antMatchers("/messaging/search").access("hasRole('ADMIN')") // csak az admin kereshet!
                .anyRequest().authenticated(); //minden máshoz hitelesítés kell
                
    }
    
    @Bean
    public UserDetailsService userDetailService(){
        InMemoryUserDetailsManager man = new InMemoryUserDetailsManager(); 
        //ez a memóriában dolgozik, mert nincs adatbázis
        man.createUser(User.withUsername("user").password("password").roles("USER")
                .build());//új felhasználó
        man.createUser(User.withUsername("admin").password("password").roles("ADMIN")
                .build());//új admin
        
        
        return man;
    }
    
    @SuppressWarnings("deprecation")
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
