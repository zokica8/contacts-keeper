package contacts.keeper.controller;

import contacts.keeper.model.User;
import contacts.keeper.security.PasswordEncoderConfig;
import contacts.keeper.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;

@Controller("/api/users")
@Secured(SecurityRule.IS_ANONYMOUS)
public class UserController {
	
	private final UserService userService;
	private final PasswordEncoderConfig passwordEncoderConfig;

	public UserController(UserService userService, PasswordEncoderConfig passwordEncoderConfig) {
		this.userService = userService;
		this.passwordEncoderConfig = passwordEncoderConfig;
	}

	/**
	 *
	 * @param id the registered user's id
	 * @return the registered user, if ok 200 status
	 */
	@Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
	public HttpResponse<?> findUserById(@PathVariable Long id) {
		User user = userService.findById(id);
		return HttpResponse.ok(user).status(200);
	}

	@Operation(summary = "To register a user in the app", description = "Registering a user to the Contacts Keeper Application")
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(type = "User")), description = "A validated user registered")
	@ApiResponse(responseCode = "400", description = "Invalid email that is already in the server")
	@ApiResponse(responseCode = "201", description = "User is registered and created as a resource in the server")
	@Tag(name = "register")
	@Post
	public HttpResponse<?> addUser(@Body @Valid User user) {
		user.setPassword(passwordEncoderConfig.encode(user.getPassword()));
		User userInDatabase = userService.findByEmail(user.getEmail());
		if(userInDatabase != null && userInDatabase.getEmail().equals(user.getEmail())) {
			return HttpResponse.badRequest("Email already exists, please add another email");
		}
		User userNew = userService.addUser(user);
		return HttpResponse.ok(userNew).status(201);
	}

	/**
	 *
	 * @param user the current user in the database
	 * @param id the user's id
	 * @return the updated user with 200 status
	 */
	@Put("/{id}")
	public HttpResponse<?> updateUser(@Body User user, @PathVariable Long id) {
		User update = userService.updateUser(user, id);
		return HttpResponse.ok(update);
	}

	/**
	 *
	 * @param id the current user's id
	 * @return deleted user from the server
	 */
	@Delete("/{id}")
	public HttpResponse<?> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return HttpResponse.noContent();
	}

}
