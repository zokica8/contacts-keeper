package contacts.keeper.repository;

import contacts.keeper.model.Contacts;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

@JdbcRepository(dialect = Dialect.MYSQL)
public abstract class ContactsRepository implements CrudRepository<Contacts, Long> {

    private final JdbcOperations jdbcOperations;

    public ContactsRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Transactional
    public List<Contacts> findByUser(Long user_id) {
        String sql = "select * from contacts where user_id = ?";
        return jdbcOperations.prepareStatement(sql, statement -> {
           statement.setLong(1, user_id);
            ResultSet rs = statement.executeQuery();
            return jdbcOperations.entityStream(rs, Contacts.class).collect(Collectors.toList());
        });
    }
}
