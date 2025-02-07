package com.codedifferently.tsm;


import com.codedifferently.tsm.domain.controller.AuthController;
import com.codedifferently.tsm.domain.controller.UserController;
import com.codedifferently.tsm.domain.controller.WorksiteController;
import com.codedifferently.tsm.domain.model.dto.*;
import com.codedifferently.tsm.domain.model.entity.*;
import com.codedifferently.tsm.domain.repository.UserRepository;
import com.codedifferently.tsm.domain.service.impl.*;
import com.codedifferently.tsm.exception.*;
import com.codedifferently.tsm.jwt.JwtTokenProvider;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ApplicationTests {


    @Nested
    @DisplayName("Auth Controller Tests")
    class AuthControllerTests {
        @Mock
        private AuthenticationManager authenticationManager;
        @Mock
        private JwtTokenProvider tokenProvider;
        @Mock
        private UserRepository userRepository;
        @Mock
        private AuthServiceImpl authService;
        private AuthController authController;


        @BeforeEach
        void setUp() {
            authController = new AuthController(authenticationManager, tokenProvider, userRepository, authService);
        }


        @Test
        @DisplayName("Authentication Success")
        void testAuthenticateSuccess() {
            AuthLoginDto loginDto = new AuthLoginDto();
            loginDto.setEmail("test@example.com");
            loginDto.setPassword("password123");


            Authentication authentication = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(tokenProvider.generateToken(authentication)).thenReturn("test.jwt.token");


            ResponseEntity<?> response = ReflectionTestUtils.invokeMethod(
                    authController,
                    "authenticate",
                    loginDto
            );


            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertInstanceOf(AuthTokenDto.class, response.getBody());
            AuthTokenDto tokenDto = (AuthTokenDto) response.getBody();
            assertEquals("test.jwt.token", tokenDto.getToken());
        }


        @Test
        @DisplayName("Registration Success")
        void testRegisterSuccess() throws ResourceCreationException {
            AuthRegisterDto registerDto = new AuthRegisterDto();
            registerDto.setEmail("new@example.com");
            registerDto.setPassword("password123");


            UserEntity mockUser = new UserEntity();
            mockUser.setEmail(registerDto.getEmail());
            when(authService.createUser(any())).thenReturn(mockUser);


            ResponseEntity<?> response = ReflectionTestUtils.invokeMethod(
                    authController,
                    "register",
                    registerDto
            );


            assertNotNull(response);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals("Success", response.getBody());
        }


        @Test
        @DisplayName("Registration Failure - Email Exists")
        void testRegisterFailureEmailExists() throws Exception {
            AuthRegisterDto registerDto = new AuthRegisterDto();
            registerDto.setEmail("existing@example.com");


            when(authService.createUser(any()))
                    .thenThrow(new ResourceCreationException("Email already exists"));


            try {
                ReflectionTestUtils.invokeMethod(
                        authController,
                        "register",
                        registerDto
                );
                fail("Expected ResourceCreationException");
            } catch (Exception e) {
                Throwable cause = e.getCause();
                assertInstanceOf(ResourceCreationException.class, cause);
                assertEquals("Email already exists", cause.getMessage());
            }
        }


        @Nested
        @DisplayName("User Controller Tests")
        class UserControllerTests {
            @Mock
            private UserServiceImpl userService;
            private UserController userController;


            @BeforeEach
            void setUp() {
                userController = new UserController(userService);
            }


            @Test
            @DisplayName("Get All Users Success")
                //testGetAllUsersSuccess: Verifies the successful retrieval of all users
            void testGetAllUsersSuccess() throws PermissionDeniedException {
                mockAuthentication();


                List<UserDto> mockUsers = Arrays.asList(
                        createMockUser(1, "user1@example.com", "John", "Doe"),
                        createMockUser(2, "user2@example.com", "Jane", "Smith")
                );
                when(userService.getAllUsers(any())).thenReturn(mockUsers);


                ResponseEntity<List<UserDto>> response = userController.all();


                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(2, response.getBody().size());
            }


            @Test
            @DisplayName("Get User By ID Success")
                //testGetUserByIdSuccess: Tests retrieving a specific user by ID when the user exists
            void testGetUserByIdSuccess() throws ResourceNotFoundException, PermissionDeniedException {
                mockAuthentication();


                UserDto mockUser = createMockUser(1, "user1@example.com", "John", "Doe");
                when(userService.getUser(eq(1), any())).thenReturn(mockUser);


                ResponseEntity<UserDto> response = userController.user(1);


                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals("user1@example.com", response.getBody().getEmail());
            }


            @Test
            @DisplayName("Get User By ID Not Found")
                //testGetUserByIdNotFound: Ensures proper handling when a user by ID is not found
            void testGetUserByIdNotFound() throws PermissionDeniedException {
                mockAuthentication();


                when(userService.getUser(eq(999), any())).thenThrow(new ResourceNotFoundException("User not found"));


                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                    userController.user(999);
                });


                assertEquals("User not found", exception.getMessage());
            }


            @Test
            @DisplayName("Get User By Email Success")
                //testGetUserByEmailSuccess: Verifies the successful retrieval of a user by their email
            void testGetUserByEmailSuccess() throws ResourceNotFoundException, PermissionDeniedException {
                mockAuthentication();


                UserDto mockUser = createMockUser(1, "user1@example.com", "john", "doe");
                when(userService.getUser(eq("user1@example.com"), any())).thenReturn(mockUser);


                ResponseEntity<UserDto> response = userController.email("user1@example.com");


                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals("user1@example.com", response.getBody().getEmail());
            }


            @Test
            @DisplayName("Get User By Email Not Found")
                //testGetUserByEmailNotFound: Ensures appropriate handling when a user by email is not found
            void testGetUserByEmailNotFound() throws PermissionDeniedException {
                mockAuthentication();


                when(userService.getUser(eq("unknown@example.com"), any())).thenThrow(new ResourceNotFoundException("User not found"));


                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                    userController.email("unknown@example.com");
                });


                assertEquals("User not found", exception.getMessage());
            }


            @Test
            @DisplayName("Get Users By Worksite Success")
                //testGetUsersByWorksiteSuccess: Verifies the successful retrieval of users by their worksite
            void testGetUsersByWorksiteSuccess() throws ResourceNotFoundException, PermissionDeniedException {
                mockAuthentication();


                List<UserDto> mockUsers = Arrays.asList(
                        createMockUser(1, "user1@example.com", "john", "doe"),
                        createMockUser(2, "user2@example.com", "Jane", "doe")
                );
                when(userService.getWorksiteUsers(eq(1), any())).thenReturn(mockUsers);


                ResponseEntity<List<UserDto>> response = userController.worksiteUsers(1);


                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(2, response.getBody().size());
            }


            private UserDto createMockUser(int id, String email, String firstName, String lastName) {
                UserDto dto = new UserDto();
                dto.setId(id);
                dto.setEmail(email);
                dto.setFirstName(firstName);
                dto.setLastName(lastName);
                return dto;
            }


            private void mockAuthentication() {
                Authentication authentication = mock(Authentication.class);
                when(authentication.getAuthorities()).thenReturn(Collections.emptyList());


                SecurityContext securityContext = mock(SecurityContext.class);
                when(securityContext.getAuthentication()).thenReturn(authentication);


                SecurityContextHolder.setContext(securityContext);
            }
        }


        @Nested
        @DisplayName("User Controller Tests")
        class WorksiteControllerTests {
            @Mock
            private WorksiteServiceImpl worksiteService;
            private WorksiteController worksiteController;


            @BeforeEach
            void setUp() {
                worksiteController = new WorksiteController(worksiteService);
            }


            @Test
            @DisplayName("Get All Worksites Success")
            void testGetAllWorksitesSuccess() {
                List<WorksiteDto> mockWorksites = Arrays.asList(
                        createMockWorksite(1, "Code Differently"),
                        createMockWorksite(2, "Code Differently")
                );
                when(worksiteService.getAllWorksites()).thenReturn(mockWorksites);


                ResponseEntity<List<WorksiteDto>> response = worksiteController.all();


                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(2, response.getBody().size());
            }
            @Test
            void testGetWorksiteByIdNotFound() {
                when(worksiteService.getWorksite(999)).thenThrow(new ResourceNotFoundException("Worksite not found"));


                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                    worksiteController.worksite(999);
                });


                assertEquals("Worksite not found", exception.getMessage());
            }


            private WorksiteDto createMockWorksite(int id, String name) {
                WorksiteDto dto = new WorksiteDto();
                dto.setId(id);
                dto.setName(name);
                return dto;
            }


        }
    }
}

