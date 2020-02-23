package contacts.keeper.repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import contacts.keeper.model.User;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.MYSQL)
public abstract class UserRepository implements CrudRepository<User, Long>{
	
	private final JdbcOperations jdbcOperations;

	public UserRepository(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}
	
	@Transactional
	public List<User> getUsers() {
		String sql = "select * from user";
		return jdbcOperations.prepareStatement(sql, statement -> {
			ResultSet rs = statement.executeQuery();
			return jdbcOperations.entityStream(rs, User.class).collect(Collectors.toList());
		});
	}

	@Transactional
	public Optional<User> findByEmail(String email) {
		String sql = "select * from user where email = ?";
		return jdbcOperations.prepareStatement(sql, statement -> {
			statement.setString(1, email);
			ResultSet rs = statement.executeQuery();
			return jdbcOperations.entityStream(rs, User.class).findFirst();
		});
	}

}
