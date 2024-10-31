package com.ayom.example.provider;

import com.ayom.example.common.model.User;
import com.ayom.example.service.UserService;

public class UserServiceImpl implements UserService {
    /**
     * 用户实现服务类
     * @param user
     * @return
     */
    @Override
    public User getUser(User user) {
        System.out.println("用户名" + user.getName());
        return user;
    }
}
