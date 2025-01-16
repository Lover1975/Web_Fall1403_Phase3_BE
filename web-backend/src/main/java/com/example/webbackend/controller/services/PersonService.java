package com.example.webbackend.controller.services;

import com.example.webbackend.repository.PersonRepository;
import com.example.webbackend.repository.entity.Person;
import com.example.webbackend.repository.entity.enums.PersonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository userRepository) {
        this.personRepository = userRepository;
    }

    @Transactional
    public Person createPerson(String username, String password, PersonType personType) {
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);
        person.setPersonType(personType);
        person.setScore(0);

        return personRepository.save(person);
    }

    @Transactional
    public void followUser(Long followerId, Long targetUserId) {
        Person follower = personRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        Person targetUser = personRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        if (follower.equals(targetUser)) {
            throw new RuntimeException("Users cannot follow themselves");
        }

        follower.followPerson(targetUser);
        personRepository.save(follower);
    }

    @Transactional
    public void unfollowPerson(Long followerId, Long targetUserId) {
        Person follower = personRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        Person targetUser = personRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        follower.unfollowPerson(targetUser);
        personRepository.save(follower);
    }

    public boolean existsPersonByUserName(String username) {
        return personRepository.existsByUsername(username);
    }

    public Person findPersonByUsername(String username) {
        return personRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Person not found"));
    }

    public List<Person> findAllPersons() {
        return personRepository.findAll();
    }

    @Transactional
    public void addScore(String username, int score) {
        Person person = findPersonByUsername(username);
        person.setScore(person.getScore() + score);
        personRepository.save(person);
    }
}