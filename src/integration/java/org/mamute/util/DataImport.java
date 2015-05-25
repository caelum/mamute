package org.mamute.util;

import static java.util.Arrays.asList;
import static org.mamute.model.MarkedText.notMarked;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import net.vidageek.mirror.dsl.Mirror;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.mamute.brutauth.auth.rules.EnvironmentKarma;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.QuestionDAO;
import org.mamute.dao.TestCase;
import org.mamute.model.*;
import org.mamute.vraptor.environment.MamuteEnvironment;

import javax.servlet.ServletContext;

public class DataImport extends TestCase {
	private Updater updater;
	private Session session;
    private List<Question> questions;
    private List<Answer> answers;
    private List<User> users;
    private HashMap<String, Tag> tags;
    private static final Logger LOG = Logger.getLogger(DataImport.class); 
    private QuestionBuilder question = new QuestionBuilder();
	private ScriptSessionCreator sessionProvider = new ScriptSessionCreator();
	private List<User> usersNotUserInTests;
	private ArrayList<Flag> flags;
	private List<Information> edits;
	private ArrayList<News> news;
	private Random random;
	
    public DataImport() {
        users = new ArrayList<>();
        flags = new ArrayList<>();
        usersNotUserInTests = new ArrayList<>();
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        edits = new ArrayList<>();
        tags = new HashMap<String, Tag>();
        this.news = new ArrayList<>();
        sessionProvider.dropAndCreate();
        session = sessionProvider.getSession();
        random = new Random();
		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = null;
		try {
			env = new EnvironmentKarma(new MamuteEnvironment(ctx));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.updater = new Updater(env);
    }

    public static void main(String[] args) throws IOException {
        new DataImport().run();
    }
    
    public void run() throws IOException {
        buildData();
        
        session.beginTransaction();
        try {
            saveObjects();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        LOG.info("data import finished successfully");
    }

    private void buildData() {
    	newUser(userWithPassword("Francisco", "chico@email.com.br").asModerator(), true);
    	newUser(userWithPassword("Fernanda", "fe@email.com.br").asModerator(), true);
        newUser(userWithPassword("Artur", "art@email.com.br").asModerator(), false);
        newUser(userWithPassword("Leonardo", "leo@email.com.br").asModerator(), false);
        newUser(userWithPassword("Guilherme", "gui@email.com.br").asModerator(), false);
        newUser(userWithPassword("Ricardo", "rick@email.com.br").asModerator(), false);
        newUser(userWithPassword("Felipe", "fe@email.com.br").asModerator(), false);
        newUser(userWithPassword("Moderator", "moderator@email.com.br").asModerator(), false);
        newUser(userWithPassword("Paulo", "paulo@gmail.com").asModerator(), false);
        newUser(userWithPassword("Marco", "marco@email.com.br").asModerator(), false);
        newUser(userWithPassword("Yuri", "yuri@email.com.br").asModerator(), false);
        newUser(userWithPassword("Acceptance Test user", "acceptance@email.com.br"), true);
        User karmaUser = userWithPassword("User with a lot of karma", "karma.user@email.com.br");
        setKarma(karmaUser, 1000l);
        newUser(karmaUser, true);
        
        newTag("ruby", "");
        newTag("java", "");
        newTag("random", "");
        newTag("null", "");
        newTag("code-readability", "");
        newTag("ruby-on-rails", "");
        newTag("rubygems", "");
        newTag("android", "");
        newTag("api", "");
        newTag("vraptor", "");
        newTag("casting", "");
        newTag("matemática", "");
        newTag("io", "");
        newTag("arquivos", "");
        newTag("c#", "");
        newTag("c", "");
        newTag("c++", "");
        
        for (int i = 1; i < 251; i++) {
        	addQuestion("Questão de número " + i, "Questão questão questão questão questão", getTag("arquivos"), getRandomTag());
        }
        
        addQuestion("Como posso converter uma String em um inteiro com Java?", read("questions/q0.md"), getTag("java"), getTag("casting"));
        addSolution(read("answers/q0_a0.md"), anyUserNotUsedInTests());
        addAnswerComment("há também a possibilidade de usar `new Integer(valor)`");
        addAnswer(read("answers/q0_a1.md"), anyUserNotUsedInTests());
        
        addQuestion("Lendo um arquivo de texto inteiro", read("questions/q1.md"), getTag("java"), getTag("io"), getTag("arquivos"));
        addQuestionComment(anyUserNotUsedInTests(), "Cuidado! Ler um arquivo inteiro na memória pode ser arriscado se você não tiver certeza de seu tamanho.");
        addFlaggedQuestionComment(anyUserNotUsedInTests(), "Este comentário é tem vários flags!");
        addSolution(read("answers/q1_a0.md"), anyUserNotUsedInTests());
        addAnswer(read("answers/q1_a1.md"), anyUserNotUsedInTests());
        addFlaggedAnswerComment("Este comentário de pergunta é tem vários flags!");
        addAnswerComment("há também o `SecureRandom`");
        

        addQuestion("Como gerar um número aleatório em Java?", read("questions/q2.md"), getTag("java"), getTag("matemática"));

        addSolution(read("answers/q2_a0.md"), anyUserNotUsedInTests());
        addAnswer(read("answers/q2_a1.md"), anyUserNotUsedInTests());

        addAnswerComment("há também o `SecureRandom`");
        
        Question spam = question
        		.withTitle("Spam spam question")
        		.withDescription("this is a bad bad bad bad bad bad question")
        		.withAuthor(anyUserNotUsedInTests())
        		.withTags(asList(getTag("matemática")))
        		.build();
        new Mirror().on(spam).set().field("voteCount").withValue(QuestionDAO.SPAM_BOUNDARY - 1l);
        spam.remove();
        questions.add(spam);
        
        Question first_flagged = flaggedQuestion(5);
        questions.add(first_flagged);

        Question second_flagged = flaggedQuestion(3);
        questions.add(second_flagged);
        
        Answer first_flaggedAnswer = flaggedAnswer(first_flagged, 5);
        answers.add(first_flaggedAnswer);
        Answer second_flaggedAnswer = flaggedAnswer(second_flagged, 3);
        answers.add(second_flaggedAnswer);
        
        Question question = questions.get(0);
        addVersionTo(question);
        addVersionTo(answers.get(0));
        
        approvedNews("First approved news", "New ultra breaking news new ultra breaking news new ultra breaking news");
        approvedNews("Second approved news", "New ultra breaking news new ultra breaking news new ultra breaking news");
        approvedNews("Third approved news", "New ultra breaking news new ultra breaking news new ultra breaking news");
        notApprovedNews("News waiting to be approved", "New ultra breaking news new ultra breaking news new ultra breaking news");
        notApprovedNews("News waiting to be approved", "New ultra breaking news new ultra breaking news new ultra breaking news");
        notApprovedNews("News waiting to be approved", "New ultra breaking news new ultra breaking news new ultra breaking news");
    }

	private void approvedNews(String title, String content) {
		LOG.info("adding news: " + title);
		newNews(title, content, true);
	}
	private void notApprovedNews(String title, String content) {
		newNews(title, content, false);
	}

	private void newNews(String title, String description, boolean approved) {
		for (int i = 0; i < 450; i++) {
		User author = anyUserNotUsedInTests();
		NewsInformation newsInformation = new NewsInformation(title, notMarked(description), 
				new LoggedUser(author, null), i + "bla bla bla bla bla");
		
		News n = new News(newsInformation, author);
		if (approved)
			n.approved();
		this.news.add(n);
		}
	}

	private void addVersionTo(Question question) {
		User editor = newUser(userWithPassword("some editor", "editor@email.com.br"), false);
		setId(editor, 1l);
		setId(question.getAuthor(), 2l);
		List<Tag> tagsEdited = asList(getTag("java"));
		QuestionInformation information = new QuestionInformation("question question question question question", 
				notMarked("edit edit edit edit edit edit edit edit edit edit edit"), new LoggedUser(editor, null), tagsEdited, "blablablab");
		edits.add(information);
		question.updateWith(information, updater);
		setId(question.getAuthor(), null);
		setId(editor, null);
	}
	
	private void addVersionTo(Answer answer) {
		User editor = newUser(userWithPassword("some editor", "editor@email.com.br"), false);
		setId(editor, 1l);
		setId(answer.getAuthor(), 2l);
		AnswerInformation information = new AnswerInformation(notMarked("edited edited edited edited edited edited"), new LoggedUser(editor, null),
				answer, "bla bla bla blabla");
		edits.add(information);
		answer.updateWith(information, updater);
		setId(answer.getAuthor(), null);
		setId(editor, null);
	}

	private Answer flaggedAnswer(Question flagged, int n) {
		Answer flaggedAnswer = answer("flagged answer flagged answer flagged answer", flagged, anyUserNotUsedInTests());
		List<Flag> list = new ArrayList<>();
		for (int i = 1; i < (n+1); i++) {
			Flag flag = new Flag(FlagType.OBSOLETE, users.get(users.size() - i));
			flaggedAnswer.add(flag);
			list.add(flag);
		}
		flags.addAll(list);
		return flaggedAnswer;
	}

	private Question flaggedQuestion(int n) {
		Question flagged = question
        		.withTitle("Questão com várias flags")
        		.withDescription("this is a bad bad bad bad bad bad question")
        		.withAuthor(anyUserNotUsedInTests())
        		.withTags(asList(getTag("matemática")))
        		.build();
		List<Flag> list = new ArrayList<>();
		for (int i = 1; i < (n+1); i++) {
			Flag flag = new Flag(FlagType.OBSOLETE, users.get(users.size() - i));
			flagged.add(flag);
			list.add(flag);
		}
		flags.addAll(list);
		return flagged;
	}

	private User newUser(User user, boolean isUsedInTests) {
        users.add(user);
        if (!isUsedInTests) {
        	usersNotUserInTests.add(user);
        }
        return user;
		
	}

	private void setKarma(User user, long karma) {
		new Mirror().on(user).set().field("karma").withValue(karma);
	}

	private void saveObjects() {
        saveAllUsers();
        saveTags();
        saveAll(flags);
        saveAll(questions);
        saveAll(edits);
        saveAll(answers);
        saveAll(news);
        saveAnonymous();
    }


	private void saveAllUsers() {
    	for (User user : users) {
    		session.save(user);
			session.save(user.getBrutalLogin());
		}
		
	}

    private void saveAnonymous() {
        try {
        	session.createSQLQuery("insert into Users (id, moderator,karma, name, sluggedName,"
            		+ " createdAt, email, confirmedEmail, isSubscribed, isBanned) "
            		+ "values (1000, false,0,'Anonimo', 'anonimo', '20120101', 'anonimo@anonimo.com', false, false, false)")
            		.executeUpdate();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    private Answer addSolution(String desc, User author) {
        Answer a = addAnswer(desc, author);
        a.markAsSolution();
        return a;
    }

    private void saveTags() {
        Set<Entry<String, Tag>> entries = tags.entrySet();
        for (Entry<String, Tag> entry : entries) {
            session.save(entry.getValue());
        }
    }

    private void newTag(String name, String desc) {
        Tag tag = new Tag(name, desc, anyUserNotUsedInTests());
        tags.put(tag.getName(), tag);
    }
    
    private Tag getTag(String name) {
        Tag tag = tags.get(name);
        if (tag == null) {
            throw new RuntimeException("could not find tag: " + name);
        }
        return tag;
    }
    
    private Tag getRandomTag() {
    	Object[] array = tags.keySet().toArray();
    	String key = (String) array[(int) (random.nextInt(array.length))];
    	Tag tag = tags.get(key); 
    	if (tag == null) {
    		throw new RuntimeException("could not find tag: " + key);
    	}
    	return tag; 
    }

    private void addAnswerComment(String desc) {
        Comment comment = new Comment(anyUserNotUsedInTests(), notMarked(desc));
        answers.get(answers.size() - 1).add(comment);
    }
    
    private void addFlaggedAnswerComment(String desc) {
        Comment comment = new Comment(anyUserNotUsedInTests(), notMarked(desc));
        Flag flag1 = new Flag(FlagType.OBSOLETE, users.get(users.size() - 1));
        Flag flag2 = new Flag(FlagType.OBSOLETE, users.get(users.size() - 2));
        Flag flag3 = new Flag(FlagType.OBSOLETE, users.get(users.size() - 3));
        Flag flag4 = new Flag(FlagType.OBSOLETE, users.get(users.size() - 4));
        Flag flag5 = new Flag(FlagType.OBSOLETE, users.get(users.size() - 4));
		comment.add(flag1);
		comment.add(flag2);
		comment.add(flag3);
		comment.add(flag4);
		comment.add(flag5);
		flags.addAll(asList(flag1, flag2, flag3, flag4, flag5));
        answers.get(answers.size() - 1).add(comment);
    }

    private Answer addAnswer(String description, User author) {
        Question question = questions.get(questions.size() - 1);
        Answer answer = answer(description, question, author);
        answers.add(answer);
        return answer;
    }

    private void addQuestionComment(User author, String desc) {
        Comment comment = new Comment(author, notMarked(desc));
        questions.get(questions.size() - 1).add(comment);
    }
    
    private void addFlaggedQuestionComment(User author, String desc) {
        Comment comment = new Comment(author, notMarked(desc));
        Flag flag1 = new Flag(FlagType.OBSOLETE, users.get(users.size() - 1));
        Flag flag2 = new Flag(FlagType.OBSOLETE, users.get(users.size() - 2));
        Flag flag3 = new Flag(FlagType.OBSOLETE, users.get(users.size() - 3));
        Flag flag4 = new Flag(FlagType.OBSOLETE, users.get(users.size() - 4));
        Flag flag5 = new Flag(FlagType.OBSOLETE, users.get(users.size() - 4));
		comment.add(flag1);
		comment.add(flag2);
		comment.add(flag3);
		comment.add(flag4);
		comment.add(flag5);
		flags.addAll(asList(flag1, flag2, flag3, flag4, flag5));
        questions.get(questions.size() - 1).add(comment);
    }

    private void addQuestion(String title, String desc, Tag... tags) {
        Question myQuestion = question.withTitle(title).withDescription(desc).withAuthor(anyUserNotUsedInTests()).withTags(asList(tags)).build();
        questions.add(myQuestion);
    }
    
    private void saveAll(List<?> list) {
        for (Object object : list) {
            session.save(object);
        }
    }

    private String read(String name) {
        String description;
        LOG.info("Trying to read /data/" + name + " resource");
        try {
            description = IOUtils.toString(getClass().getResourceAsStream("/data/" + name));
            return description;
        } catch (IOException e) {
            throw new RuntimeException("could not find /data/" + name + " resource", e);    
        }
    }
    
    private User anyUserNotUsedInTests() {
        int i = new Random().nextInt(usersNotUserInTests.size());
        return usersNotUserInTests.get(i);
    }
    
    public Session getSession() {
		return session;
	}

}
