package contacts.keeper.service;

import contacts.keeper.model.Contacts;
import contacts.keeper.repository.ContactsRepository;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class ContactsService {

    private final ContactsRepository contactsRepository;

    public ContactsService(ContactsRepository contactsRepository) {
        this.contactsRepository = contactsRepository;
    }

    public Contacts findById(Long id) {
        return contactsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + "not found"));
    }

    public List<Contacts> findByUser(Long user_id) {
        return contactsRepository.findByUser(user_id);
    }

    public Contacts addContact(Contacts contact) {
        return contactsRepository.save(contact);
    }

    public Contacts updateContact(Contacts contact, Long id) {
        Optional<Contacts> update = contactsRepository.findById(id);
        if(update.isPresent()) {
            Contacts updatedContact = update.get();
            updatedContact.setFirstName(contact.getFirstName());
            updatedContact.setLastName(contact.getLastName());
            updatedContact.setEmail(contact.getEmail());
            updatedContact.setPhone(contact.getPhone());
            updatedContact.setType(contact.getType());
            return contactsRepository.update(updatedContact);
        }
        else {
            throw new IllegalArgumentException(String.format("User with id %d not found", id));
        }
    }

    public void deleteContact(Long id) {
        contactsRepository.deleteById(id);
    }
}
