package com.sprintify.task.Service;

import com.sprintify.task.Entity.Role;
import com.sprintify.task.Entity.UserEntity;
import com.sprintify.task.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("User " +email +" Not Available ");
        }


        return new UserInfoService(userOptional.get());
    }

}
