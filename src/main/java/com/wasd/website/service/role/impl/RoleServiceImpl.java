package com.wasd.website.service.role.impl;

import com.wasd.website.entity.Role;
import com.wasd.website.model.user.UserRole;
import com.wasd.website.repository.RoleRepository;
import com.wasd.website.service.role.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getUserRole(UserRole role) {
        final String ROLE_PREFIX = "ROLE_";
        return roleRepository.findByName(String.format("%s%s", ROLE_PREFIX, role.name()))
                .orElseThrow(EntityNotFoundException::new);
    }
}
