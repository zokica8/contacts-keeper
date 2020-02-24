package contacts.keeper.model;

import java.time.LocalDate;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@MappedEntity
@Introspected
@Schema(name = "User", description = "Contacts Keeper registered User")
public class User {
	
	@Id
	@GeneratedValue(value = GeneratedValue.Type.IDENTITY)
	private Long id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	@Pattern(regexp = "^(.+)@(.+)$", message = "Email invalid")
	private String email;

	@NotBlank
	@Size(min = 10, message = "Password must have at least 10 characters")
	private String password;
	
	@Getter(value = AccessLevel.NONE)
	@DateCreated
	private LocalDate dateRegistered;
	
	@Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "user")
	private Set<Contacts> contacts;
	
	@PostConstruct
	public LocalDate getDateRegistered() {
		return LocalDate.now();
	}

}
