package com.example.gui.authenticationStrategy;

import com.example.gui.models.User;

public interface AuthenticationStrategy {
    public int authenticate(User user);
}
