package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class UserDAO {

	private final Session session;

	public UserDAO(Session session) {
		this.session = session;
	}

	public User findByMailAndPassword(String email, String pass) {
		return (User) session
				.createQuery(
						"from User where email = :email and password = :password")
				.setParameter("email", email)
				.setParameter("password",
						br.com.caelum.brutal.infra.Digester.encrypt(pass))
				.uniqueResult();
	}

	public void save(User user) {
		session.save(user);
	}

    public User findById(Long id) {
        return (User) session.load(User.class, id);
    }

	public User load(User user) {
		return findById(user.getId());
	}

	public User loadByEmail(String email) {
		return (User) session
				.createQuery("from User where email = :email")
				.setParameter("email", email)
				.uniqueResult();
	}

	public User loadByIdAndToken(Long id, String token) {
		return (User) session
				.createQuery("from User where id = :id and forgotPasswordToken = :token")
				.setParameter("id", id)
				.setParameter("token", token)
				.uniqueResult();
	}
}