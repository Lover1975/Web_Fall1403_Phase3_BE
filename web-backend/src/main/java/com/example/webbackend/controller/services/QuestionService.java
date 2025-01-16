package com.example.webbackend.controller.services;

import com.example.webbackend.repository.CategoryRepository;
import com.example.webbackend.repository.QuestionRepository;
import com.example.webbackend.repository.entity.Category;
import com.example.webbackend.repository.entity.Person;
import com.example.webbackend.repository.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, CategoryRepository categoryRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public List<Question> findByCategory(Category category) {
        return questionRepository.findByCategory(category);
    }

    public List<Question> getQuestionsByPerson(Person designer) {
        return questionRepository.findByDesigner(designer);
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public Question addQuestion(Person designer, String questionText, String answer1, String answer2,
                                String answer3, String answer4, int correctAnswer, int hardness,
                                String categoryName) {

        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category does not exist"));

        Question question = new Question();
        question.setDesigner(designer);
        question.setQuestion(questionText);
        question.setAnswer1(answer1);
        question.setAnswer2(answer2);
        question.setAnswer3(answer3);
        question.setAnswer4(answer4);
        question.setCorrectAnswer(correctAnswer);
        question.setHardness(hardness);
        question.setCategory(category);

        questionRepository.save(question);

        category.setNumberOfQuestions(category.getNumberOfQuestions() + 1);
        categoryRepository.save(category);

        return question;
    }
}
