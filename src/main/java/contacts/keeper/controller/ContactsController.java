package contacts.keeper.controller;

import contacts.keeper.model.Contacts;
import contacts.keeper.service.ContactsService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@Controller("/api/contacts")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class ContactsController {

    private final ContactsService contactsService;

    public ContactsController(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @Operation(summary = "Find all contacts for a specific user", description = "Resource where all user's contacts are found")
    @Tag(name = "Contacts by User")
    @ApiResponse(description = "Contacts for a specific user", content = @Content(mediaType = "application/json", schema = @Schema(type = "Contacts")))
    @ApiResponse(responseCode = "200", description = "Contacts for all users found")
    @ApiResponse(responseCode = "404", description = "Resource not found")
    @Get("/{user_id}")
    public HttpResponse<?> findByUser(@PathVariable Long user_id) {
        List<Contacts> contacts = contactsService.findByUser(user_id);
        return HttpResponse.ok(contacts);
    }

    @Operation(summary = "Add contact for a specific user", description = "Add contact for a specific user")
    @Tag(name = "Contacts by User")
    @ApiResponse(description = "Add contact", content = @Content(mediaType = "application/json", schema = @Schema(type = "Contacts")))
    @ApiResponse(responseCode = "201", description = "Contact successfully added")
    @ApiResponse(responseCode = "404", description = "Resource not found")
    @Post
    public HttpResponse<?> addContact(@Body Contacts contact) {
        Contacts newContact = contactsService.addContact(contact);
        return HttpResponse.ok(newContact).status(201);
    }

    @Operation(summary = "Update existing contact for a specific user", description = "Update existing contact for a specific user")
    @Tag(name = "Contacts by User")
    @ApiResponse(description = "Update contact", content = @Content(mediaType = "application/json", schema = @Schema(type = "Contacts")))
    @ApiResponse(responseCode = "200", description = "Contact successfully updated")
    @ApiResponse(responseCode = "404", description = "Resource not found")
    @Put("/{id}")
    public HttpResponse<?> updateContact(@Body Contacts contact, @PathVariable Long id) {
        Contacts updatedContact = contactsService.updateContact(contact, id);
        return  HttpResponse.ok(updatedContact);
    }

    @Operation(summary = "Delete contact for a specific user", description = "Delete contact for a specific user")
    @Tag(name = "Contacts by User")
    @ApiResponse(description = "Delete contact", content = @Content(mediaType = "application/json", schema = @Schema(type = "Contacts")))
    @ApiResponse(responseCode = "200", description = "Contact successfully deleted")
    @ApiResponse(responseCode = "404", description = "Resource not found")
    @Delete("/{id}")
    public HttpResponse<?> deleteContact(@PathVariable Long id) {
        contactsService.deleteContact(id);
        return HttpResponse.noContent();
    }
}

