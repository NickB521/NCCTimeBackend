package com.codedifferently.tsm;

import com.codedifferently.tsm.domain.controller.AuthController;
import com.codedifferently.tsm.domain.controller.UserController;
import com.codedifferently.tsm.domain.model.dto.*;
import com.codedifferently.tsm.domain.model.entity.*;
import com.codedifferently.tsm.domain.service.impl.*;
import com.codedifferently.tsm.exception.*;
import com.codedifferently.tsm.jwt.JwtTokenProvider;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationTests {

	@Nested
	@DisplayName("AuthController Tests")
	class AuthControllerTests {

		@Mock
		private AuthenticationManager authenticationManager;

		@Mock
		private JwtTokenProvider jwtTokenProvider;

		@Mock
		private AuthServiceImpl authService;

		private AuthController authController;

		@BeforeEach
		void setUp() {
			authController = new AuthController(authenticationManager, jwtTokenProvider, null, authService);
		}

		@Test
		void testAuthenticateSuccess() throws Exception {
			AuthLoginDto loginDto = new AuthLoginDto();
			loginDto.setEmail("test@example.com");
			loginDto.setPassword("password");

			Authentication authentication = mock(Authentication.class);
			when(authenticationManager.authenticate(any())).thenReturn(authentication);
			when(jwtTokenProvider.generateToken(authentication)).thenReturn("dummyToken");

			Method authenticateMethod = AuthController.class.getDeclaredMethod("authenticate", AuthLoginDto.class);
			authenticateMethod.setAccessible(true);

			@SuppressWarnings("unchecked")
			ResponseEntity<AuthTokenDto> response = (ResponseEntity<AuthTokenDto>) authenticateMethod.invoke(authController, loginDto);

			assertEquals(200, response.getStatusCode().value());
			assertNotNull(response.getBody());
			assertEquals("dummyToken", response.getBody().getToken());
		}

		@Test
		void testRegisterSuccess() throws Exception {
			AuthRegisterDto registerDto = new AuthRegisterDto();
			registerDto.setEmail("newuser@example.com");
			registerDto.setPassword("password");
			registerDto.setFirstName("John");
			registerDto.setLastName("Doe");
			registerDto.setWorksite("Worksite A");
			registerDto.setDob(new Date());

			UserEntity user = new UserEntity();
			user.setEmail("newuser@example.com");

			when(authService.createUser(any())).thenReturn(user);

			Method registerMethod = AuthController.class.getDeclaredMethod("register", AuthRegisterDto.class);
			registerMethod.setAccessible(true);

			@SuppressWarnings("unchecked")
			ResponseEntity<String> response = (ResponseEntity<String>) registerMethod.invoke(authController, registerDto);

			assertEquals(201, response.getStatusCode().value());
			assertEquals("Success", response.getBody());
		}

		@Test
		void testRegisterFailureEmailExists() throws Exception {
			AuthRegisterDto registerDto = new AuthRegisterDto();
			registerDto.setEmail("existing@example.com");

			when(authService.createUser(any())).thenThrow(new ResourceCreationException("Email already exists"));

			Method registerMethod = AuthController.class.getDeclaredMethod("register", AuthRegisterDto.class);
			registerMethod.setAccessible(true);

			ResourceCreationException exception = assertThrows(ResourceCreationException.class, () -> {
				registerMethod.invoke(authController, registerDto);
			});

			assertEquals("Email already exists", exception.getMessage());
		}
	}

	@Nested
	@DisplayName("UserController Tests")
	class UserControllerTests {

		@Mock
		private UserServiceImpl userService;

		private UserController userController;

		@BeforeEach
		void setUp() {
			userController = new UserController(userService);
		}

		@Test
		void testGetAllUsersSuccess() {
			UserDto user1 = new UserDto();
			user1.setId(1);
			user1.setEmail("user1@example.com");

			UserDto user2 = new UserDto();
			user2.setId(2);
			user2.setEmail("user2@example.com");

			List<UserDto> users = Arrays.asList(user1, user2);

			when(userService.getAllUsers()).thenReturn(users);

			ResponseEntity<List<UserDto>> response = userController.all();

			assertEquals(200, response.getStatusCode().value());
			assertEquals(2, response.getBody().size());
		}

		@Test
		void testGetUserByIdSuccess() {
			UserDto user = new UserDto();
			user.setId(1);
			user.setEmail("user1@example.com");

			when(userService.getUser(1)).thenReturn(user);

			ResponseEntity<UserDto> response = userController.user(1);

			assertEquals(200, response.getStatusCode().value());
			assertEquals("user1@example.com", response.getBody().getEmail());
		}

		@Test
		void testGetUserByIdNotFound() {
			when(userService.getUser(999)).thenThrow(new ResourceNotFoundException("User not found"));

			ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
				userController.user(999);
			});

			assertEquals("User not found", exception.getMessage());
		}
	}
}