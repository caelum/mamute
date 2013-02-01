package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.Logged;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class QuestionController {

    private final Result result;
    private final QuestionDAO questionDAO;
    private final User currentUser;

    public QuestionController(Result result, QuestionDAO questionDAO, User currentUser) {
        this.result = result;
        this.questionDAO = questionDAO;
        this.currentUser = currentUser;
    }
    
    @Get("/question/ask")
    @Logged
    public void questionForm() {
    }
    
    @Get("/questions/{questionId}/{sluggedTitle}")
    public void showQuestion(Long questionId, String sluggedTitle){
    	Question question = questionDAO.getById(questionId);
    	result.include("question", question);
    }
    
    @Post("/question/ask")
    @Logged
    public void newQuestion(Question question) {
        question.setAuthor(currentUser);
        questionDAO.save(question);
        result.redirectTo(this).showQuestion(question.getId(), question.getSluggedTitle());
    }
    
}
