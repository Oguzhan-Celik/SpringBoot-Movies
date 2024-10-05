package work.oguzhan.movies.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import work.oguzhan.movies.dtos.LoginUserDto;
import work.oguzhan.movies.dtos.RegisterUserDto;
import work.oguzhan.movies.model.User;
import work.oguzhan.movies.repository.UserRepository;

@Service
public class AuthenticationService {
	private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User signup(RegisterUserDto input) {
    	if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
    	
    	User user = new User()
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()));
    	System.out.println(user.toString());
    	
    	return userRepository.save(user);
    }
 
    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
        		.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + input.getEmail()));
    }
}