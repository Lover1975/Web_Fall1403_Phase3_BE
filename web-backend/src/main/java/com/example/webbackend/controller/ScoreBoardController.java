package com.example.webbackend.controller;

import com.example.webbackend.controller.services.CategoryService;
import com.example.webbackend.controller.services.PersonService;
import com.example.webbackend.controller.services.QuestionService;
import com.example.webbackend.repository.entity.Person;
import com.example.webbackend.repository.entity.dtos.PersonDto;
import com.example.webbackend.repository.entity.dtos.ProfileDto;
import com.example.webbackend.repository.entity.dtos.ProfilesDto;
import com.example.webbackend.repository.entity.enums.PersonType;
import com.example.webbackend.web.BaseResponse;
import com.example.webbackend.web.ResponseHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class ScoreBoardController {
    private final QuestionService questionService;
    private final PersonService personService;
    private final CategoryService categoryService;
    public ScoreBoardController(QuestionService questionService, PersonService personService, CategoryService categoryService) {
        this.questionService = questionService;
        this.personService = personService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "get-score-board")
    public BaseResponse getScoreBoard() {
        List<Person> allPersons = personService.findAllPersons();
        List<Person> top10 = allPersons.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore())) // Sort descending
                .limit(10)
                .toList();
        List<ProfileDto> top10Dto = new LinkedList<ProfileDto>();
        for (Person person : top10) {
            if (person.getPersonType() != PersonType.DESIGNER) {
                ProfileDto profileDto = new ProfileDto(person.getId(), person.getUsername(), person.getFollowersCount(), person.getFollowingCount(), person.getQuestions().size(), person.getAnsweredQuestions().size(), person.getScore());
                top10Dto.add(profileDto);
            }
        }
        ProfilesDto profilesDto = new ProfilesDto(top10Dto);
        return new BaseResponse<>(ResponseHeader.OK, profilesDto);
    }

    @PostMapping(value = "follow-action")
    public BaseResponse followAction(@RequestParam Long followerId, @RequestParam Long targetUserId) {
        personService.followUser(followerId, targetUserId);
        return new BaseResponse<>(ResponseHeader.OK, null);
    }

    @PostMapping(value = "unfollow-action")
    public BaseResponse unfollowAction(@RequestParam Long followerId, @RequestParam Long targetUserId) {
        personService.unfollowPerson(followerId, targetUserId);
        return new BaseResponse<>(ResponseHeader.OK, null);
    }
}
