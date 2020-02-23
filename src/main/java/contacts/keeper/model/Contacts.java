package contacts.keeper.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@MappedEntity
@Introspected
@Schema(name = "User's contacts", description = "User's added contacts")
public class Contacts {

	@Id
	@GeneratedValue(value = GeneratedValue.Type.IDENTITY)
	private Long id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	private String email;

	@NotBlank
	@Pattern(regexp = "\\+(.+)[0-9]+", message = "Invalid phone")
	private String phone;

	@NotBlank
	private String type;
	private User user;

}
