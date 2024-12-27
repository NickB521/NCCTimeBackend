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
			void testGetAllUsersSuccess() {
				List<UserDto> mockUsers = Arrays.asList(
						createMockUser(1, "user1@example.com"),
						createMockUser(2, "user2@example.com")
				);
				when(userService.getAllUsers()).thenReturn(mockUsers);

				ResponseEntity<List<UserDto>> response = userController.all();

				assertEquals(HttpStatus.OK, response.getStatusCode());
				assertEquals(2, response.getBody().size());
			}
			@Test
			void testGetUserByIdNotFound() {
				when(userService.getUser(999)).thenThrow(new ResourceNotFoundException("User not found"));

				ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
					userController.user(999);
				});

				assertEquals("User not found", exception.getMessage());
			}

			private UserDto createMockUser(int id, String email) {
				UserDto dto = new UserDto();
				dto.setId(id);
				dto.setEmail(email);
				return dto;
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
				//when(worksiteService.getAllWorksites()).thenReturn(mockWorksites);

				//TO DO: Adjust return statement in Worksite Controller to return WorksiteDto
				//ResponseEntity<List<WorksiteDto>> response = worksiteController.all();

				//assertEquals(HttpStatus.OK, response.getStatusCode());
				//assertEquals(2, response.getBody().size());
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
				dto.setWorksite_id(id);
				dto.setWorksite_name(name);
				return dto;
			}

		}
	}
}