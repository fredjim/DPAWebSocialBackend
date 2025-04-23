package com.infsis.socialpagebackend.authentication.services;

import com.infsis.socialpagebackend.authentication.models.Role;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUsersDetailsService implements UserDetailsService {
    private UserRepository usuariosRepo;

    @Autowired
    public CustomUsersDetailsService(UserRepository usuariosRepo) {
        this.usuariosRepo = usuariosRepo;
    }

    // Método para traernos una lista de autoridades por medio de una lista de roles
    public Collection<GrantedAuthority> mapToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    // Método para traernos un usuario con todos sus datos por medio de su username
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users usuario = usuariosRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<GrantedAuthority> authorities = usuario.getRoles()
                .stream()
                .flatMap(role -> role.getPermisos().stream()
                        .map(permiso -> new SimpleGrantedAuthority(permiso.getNamePermission())))
                .collect(Collectors.toList());

        return new User(usuario.getEmail(), usuario.getPassword(), authorities);
    }
}