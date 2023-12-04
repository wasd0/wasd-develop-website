package com.wasd.website.service.role;

import com.wasd.website.entity.Role;
import com.wasd.website.model.user.UserRole;

public interface RoleService {
    Role getUserRole(UserRole role);
}
