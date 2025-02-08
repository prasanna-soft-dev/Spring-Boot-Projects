package com.sprintify.task.Service;

import com.sprintify.task.Entity.UserEntity;

public interface UserService {
    String registration(UserEntity user);
    String authorize(String email, String password);
}
