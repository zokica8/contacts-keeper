package contacts.keeper.service;

import java.util.Optional;

import javax.inject.Singleton;

import contacts.keeper.model.User;
import contacts.keeper.repository.UserRepository;

@Singleton
public class UserService {
	
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	};
	
	public User addUser(User user) {
		return userRepository.save(user);
	}
	
	public User findById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> 
			new IllegalArgumentException(String.format("User with id %d not found", id)));
	}
	
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
	
	public User updateUser(User user, Long id) {
		Optional<User> update = userRepository.findById(id);
		if(update.isPresent()) {
			User updatedUser = update.get();
			updatedUser.setFirstName(user.getFirstName());
			updatedUser.setLastName(user.getLastName());
			updatedUser.setEmail(user.getEmail());
			updatedUser.setPassword(user.getPassword());
			return userRepository.update(updatedUser);
		}
		else {
			throw new IllegalArgumentException(String.format("User with id %d not found", id));
		}
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

}
