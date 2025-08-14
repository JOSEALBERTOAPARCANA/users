package com.compustore.users.service;

import com.compustore.users.dto.UserRegisterRequest;
import com.compustore.users.model.User;

public interface UserService {
  User register(UserRegisterRequest req);
  User getByUsername(String username);
}
