package work.oguzhan.movies;

//import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import work.oguzhan.movies.dtos.RegisterUserDto;
import work.oguzhan.movies.model.User;
import work.oguzhan.movies.repository.UserRepository;
import work.oguzhan.movies.service.AuthenticationService;

@SpringBootTest
public class AuthenticationServiceTest {

    @Spy
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    //@AfterEach
    //public void cleanUp() {
    //    userRepository.deleteAll();
    //}

    @Test
    public void testSignup() {
    	RegisterUserDto registerUserDto = new RegisterUserDto()
        		.setFullName("John Doe")
        		.setEmail("john.doe@example.com")
        		.setPassword("password123");
    	
        User mockUser = new User()
                .setFullName("John Doe")
                .setEmail("john.doe@example.com")
                .setPassword("encodedPassword123");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(mockUser);
        
        // Act
        User savedUser = authenticationService.signup(registerUserDto);

     // Verify that the savedUser is not null and has the expected properties
        Assertions.assertNotNull(registerUserDto.getFullName());
        Assertions.assertNotNull(registerUserDto.getEmail());
        Assertions.assertNotNull(registerUserDto.getPassword());
        Assertions.assertNotNull(savedUser, "User should not be null");
        Assertions.assertEquals("John Doe", savedUser.getFullName());
        Assertions.assertEquals("john.doe@example.com", savedUser.getEmail());
    }
}
