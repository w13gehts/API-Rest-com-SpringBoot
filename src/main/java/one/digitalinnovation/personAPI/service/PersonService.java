package one.digitalinnovation.personAPI.service;
import lombok.AllArgsConstructor;
import one.digitalinnovation.personAPI.dto.request.PersonDTO;
import one.digitalinnovation.personAPI.dto.response.MessageResponseDTO;
import one.digitalinnovation.personAPI.entity.Person;
import one.digitalinnovation.personAPI.exception.PersonNotFoundException;
import one.digitalinnovation.personAPI.mapper.PersonMapper;
import one.digitalinnovation.personAPI.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__ (@Autowired))
public class PersonService {

    private PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

        public MessageResponseDTO createPerson(PersonDTO personDTO) {
        Person personToSave = personMapper.toModel(personDTO);

        Person savedPerson = personRepository.save(personToSave);
        return createMessageResponse(savedPerson.getId(), "Created person with id: ");
    }

    public List<PersonDTO> listAll() {
        List<Person> allPeople = personRepository.findAll();
        return allPeople.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
       Person person = VerifyIfExists(id);
        return personMapper.toDTO(person);
    }

    public void delete(Long id) throws PersonNotFoundException {
        VerifyIfExists(id);
        personRepository.deleteById(id);
    }

    private Person VerifyIfExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {
        VerifyIfExists(id);
        Person personToUpdate = personMapper.toModel(personDTO);

        Person savedPerson = personRepository.save(personToUpdate);
        return createMessageResponse(savedPerson.getId(), "Updated person with id: ");
    }

    private MessageResponseDTO createMessageResponse (Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }
}
