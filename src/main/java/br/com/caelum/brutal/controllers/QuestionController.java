package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class QuestionController {

    private final Result result;
    private final QuestionDAO questionDAO;

    public QuestionController(Result result, QuestionDAO questionDAO) {
        this.result = result;
        this.questionDAO = questionDAO;
    }
    
    @Get("/question/ask")
    public void questionForm() {
    }
    
    @Post("/question/ask")
    public void newQuestion(Question question) {
        questionDAO.save(question);
        result.redirectTo(ListController.class).home();
    }
    
}
