package com.devsync.DevSync.Service.Implementation;

import com.devsync.DevSync.Entity.Role;
import com.devsync.DevSync.Entity.UserEntity;
import com.devsync.DevSync.Repository.UserRepository;
import com.devsync.DevSync.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean registration(UserEntity user){
        Optional<UserEntity> u = userRepository.findByEmail(user.getEmail());

        if(u.isPresent()){
            return false;
        }

        if(user.getRole() == null){
            user.setRole(Role.DEV);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setEmail(user.getEmail().toLowerCase());
        userRepository.save(user);

        return true;
    }
}
