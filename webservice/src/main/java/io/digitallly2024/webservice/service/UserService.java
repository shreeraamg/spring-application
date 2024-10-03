package io.digitallly2024.webservice.service;

import io.digitallly2024.webservice.repository.UserRepository;
import io.digitallly2024.webservice.dto.UserDto;
import io.digitallly2024.webservice.entity.User;
import io.digitallly2024.webservice.exception.ResourceNotFoundException;
import io.digitallly2024.webservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with given email: " + username));
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with given id: " + userId));

        return UserMapper.mapToUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAllByRole("USER");
        return users.stream().map(UserMapper::mapToUserDto).toList();
    }

    public UserDto deleteUser(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with given id: " + userId));

        userRepository.delete(user);
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal == null || principal.equals("anonymousUser") ? new User() : (User) principal;
    }

}
