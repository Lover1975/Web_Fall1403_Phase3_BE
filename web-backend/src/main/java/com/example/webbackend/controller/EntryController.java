package com.example.webbackend.controller;

import com.example.webbackend.controller.services.PersonService;
import com.example.webbackend.repository.entity.Person;
import com.example.webbackend.repository.entity.dtos.ModelMapperInstance;
import com.example.webbackend.repository.entity.dtos.PersonDto;
import com.example.webbackend.repository.entity.enums.PersonType;
import com.example.webbackend.web.BaseResponse;
import com.example.webbackend.web.ResponseHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntryController {
    @Autowired protected PersonService personService;

    @PostMapping(value = "signin")
    public BaseResponse signin(@RequestParam String username, @RequestParam String password) {
        if (!personService.existsPersonByUserName(username)) {
            return (new BaseResponse(ResponseHeader.USERNAME_NOT_EXISTS, null));
        }
        Person person = personService.findPersonByUsername(username);
        if (!person.getPassword().equals(password)) {
            return(new BaseResponse(ResponseHeader.WRONG_PASSWORD, null));
        }
        PersonDto personDto = ModelMapperInstance.getModelMapper().map(person, PersonDto.class);

        return (new BaseResponse(ResponseHeader.OK, personDto));
    }

    @PostMapping(value = "signup")
    public BaseResponse signup(@RequestParam String username, @RequestParam String password,
                               @RequestParam String personType) {
        if (personService.existsPersonByUserName(username)) {
            return (new BaseResponse(ResponseHeader.USERNAME_NOT_EXISTS, null));
        }
        Person person = personService.createPerson(username, password, PersonType.valueOf(personType));
        PersonDto personDto = ModelMapperInstance.getModelMapper().map(person, PersonDto.class);
        return(new BaseResponse(ResponseHeader.OK, personDto));
    }
}
