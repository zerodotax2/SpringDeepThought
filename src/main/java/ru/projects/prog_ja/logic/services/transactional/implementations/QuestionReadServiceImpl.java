package ru.projects.prog_ja.logic.services.transactional.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.projects.prog_ja.dto.auth.UserDTO;
import ru.projects.prog_ja.dto.dao.PageableEntity;
import ru.projects.prog_ja.dto.full.FullQuestionTransfer;
import ru.projects.prog_ja.dto.view.PageableContainer;
import ru.projects.prog_ja.dto.view.PagesDTO;
import ru.projects.prog_ja.dto.view.ViewAnswerTransfer;
import ru.projects.prog_ja.logic.queues.views.ViewsQueue;
import ru.projects.prog_ja.logic.services.simple.implementations.RegexUtil;
import ru.projects.prog_ja.logic.services.simple.interfaces.ValuesParser;
import ru.projects.prog_ja.logic.services.transactional.interfaces.QuestionReadService;
import ru.projects.prog_ja.model.dao.QuestionsDAO;

import java.util.List;

@Service
@Scope("prototype")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class QuestionReadServiceImpl implements QuestionReadService {

    private static int smallQuestionsSize;
    private static int smallAnswersSize;
    private static int commonQuestionsSize;
    private static int maxEntitiesSize;

    private final QuestionsDAO questionsDAO;
    private final ViewsQueue viewsQueue;
    private final ValuesParser parser;

    @Autowired
    public QuestionReadServiceImpl(QuestionsDAO questionsDAO,
                                   ViewsQueue viewsQueue,
                                   ValuesParser parser){
        this.questionsDAO = questionsDAO;
        this.viewsQueue = viewsQueue;
        this.parser = parser;
    }

    @Override
    public PageableContainer getQuestions(String page, String query, String type, String sort) {
        if(query == null || "".equals(query)){
            return getDefaultQuestions(page, type, sort);
        }else{
            return findCommonQuestions(page, query, type, sort);
        }
    }

    public PageableContainer getDefaultQuestions(String page, String type, String sort){

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                questionsDAO.getQuestions((parsedPage-1)*commonQuestionsSize, commonQuestionsSize, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), commonQuestionsSize));
    }

    @Override
    public PageableContainer findCommonQuestions(String page, String search, String type, String sort)  {
        if(search == null || !RegexUtil.string(search).matches())
            return getDefaultQuestions(page, type, sort);

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                questionsDAO.findQuestions((parsedPage-1)*commonQuestionsSize, commonQuestionsSize, search,  getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), commonQuestionsSize));
    }

    @Override
    public PageableContainer findSmallQuestions(String page, String search, String type, String sort)  {
        if(search == null || !RegexUtil.string(search).matches())
            return getDefaultSmallQuestions(page, type, sort);

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                questionsDAO.findSmallQuestions((parsedPage-1)*smallQuestionsSize, smallQuestionsSize, search, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), smallQuestionsSize));
    }

    @Override
    public FullQuestionTransfer getOneQuestion(long id, UserDTO userDTO) {

        FullQuestionTransfer fullQuestionTransfer =  questionsDAO.getFullQuestion(id);
        if(fullQuestionTransfer != null && userDTO != null && fullQuestionTransfer.getUser().getId() != userDTO.getId()){
            viewsQueue.addQuestionView(id);
        }

        return fullQuestionTransfer;
    }

    @Override
    public PageableContainer getSmallQuestions(String page, String query, String type, String sort) {
        if(query == null || "".equals(query)){
            return getDefaultSmallQuestions(page, type, sort);
        }else{
            return findSmallQuestions(page, query, type, sort);
        }
    }

    @Override
    public PageableContainer getDefaultSmallQuestions(String page, String type, String sort){

        int parsedPage = parser.getPage(page);
        PageableEntity pageable =
                questionsDAO.getSmallQuestions((parsedPage-1)*smallQuestionsSize, smallQuestionsSize, getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), smallQuestionsSize));
    }


    @Override
    public PageableContainer getQuestionsByUser(String page, String size, long userId, String query, String type, String sort) {

        int parsedSize = parser.getSize(size),
            parsedPage = parser.getPage(page);
        PageableEntity pageable = questionsDAO.getSmallQuestionsByUser((parsedPage-1)*parsedSize, parsedSize+1,
                userId, parser.getQuery(query  ), getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), parsedSize));
    }

    @Override
    public PageableContainer getQuestionsByTag(String page, String  size, long tagId, String q, String type, String sort) {

        int parsedSize = parser.getSize(size),
                parsedPage = parser.getPage(page);
        PageableEntity pageable = questionsDAO.getSmallQuestionsByTag((parsedPage-1)*parsedSize, parsedSize+1,
                tagId, parser.getQuery(q), getOrderField(type), parser.getSort(sort));

        return new PageableContainer(pageable.getList(),
                parser.getPages(parsedPage, pageable.getCount(), parsedSize));
    }

    @Override
    public PageableContainer getAnswersByUser(int start, String size, long userId, String q, String type, String sort){

        int parsedSize = parser.getSize(size);
        List<ViewAnswerTransfer> answers = questionsDAO.getSmallAnswersByUser(start, parsedSize+1,
                userId,parser.getQuery(q), getOrderField(type), parser.getSort(sort));

        return new PageableContainer(answers, new PagesDTO(0,0,0,0));
    }
    

    private String getOrderField(String type){
        if(type == null)
            return "rating";

        switch (type) {
            case "rating":
                return "rating";

            case "date":
                return "createDate";

            default:
                return "rating";
        }
    }

    @Value("${questions.small.show.size}")
    public  void setSmallQuestionsSize(int size) {
        smallQuestionsSize = size;
    }

    @Value("${questions.answers.small.size}")
    public  void setSmallAnswersSize(int size) {
        smallAnswersSize = size;
    }

    @Value("${questions.common.show.size}")
    public  void setCommonQuestionsSize(int size) {
        commonQuestionsSize = size;
    }

    @Value("${entities.max.size}")
    public  void setMaxEntitiesSize(int size) {
        maxEntitiesSize = size;
    }
}
