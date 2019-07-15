/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.service;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 *
 * @author imaginifer
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecConfig extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().formLogin()
                .defaultSuccessUrl("/messaging", true)
                .loginPage("/messaging/login")
                .permitAll()    //login oldal, amit mindenki elérhet
                .and().logout()         //logout oldal is
                .logoutSuccessUrl("/messaging")
                .and().authorizeRequests()
                .antMatchers("/messaging").permitAll()  //ide szabad a hozzáférés
                .antMatchers("/messaging/register").permitAll()
                .antMatchers("/messaging/search").permitAll()
                .antMatchers("/messaging/topics").permitAll()
                .antMatchers("/css/*","/js/*").permitAll()  //a static mappa nyílttá tétele, különben leesik a css
                .antMatchers("/messaging/delete").access("hasRole('ADMIN')") // csak az admin törölhet!
                .anyRequest().authenticated(); //minden máshoz hitelesítés kell
                
    }
    
    /*@Bean
    public UserDetailsService userDetailService(){
        InMemoryUserDetailsManager man = new InMemoryUserDetailsManager(); 
        //ez a memóriában dolgozik, mert nincs adatbázis
        man.createUser(User.withUsername("user").password("password").roles("USER")
                .build());//új felhasználó
        man.createUser(User.withUsername("admin").password("password").roles("ADMIN")
                .build());//új admin
        
        
        return man;
    }*/
    
    //@SuppressWarnings("deprecation")
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
