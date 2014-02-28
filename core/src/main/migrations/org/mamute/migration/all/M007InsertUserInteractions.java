package org.mamute.migration.all;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

@ApplicationScoped
public class M007InsertUserInteractions implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		String sql = "insert into Question_Interactions (userInteractions_id, Question_id) "
				+ "(select a.author_id, a.question_id from Answer a) "
				+ "union "
				+ "(select c.author_id, qc.question_id from Comment c inner join Question_Comments qc on c.id = qc.Comments_id) "
				+ "union "
				+ "(select v.author_id, qv.Question_id from Vote v inner join Question_Votes qv on v.id=qv.votes_id) "
				+ "union "
				+ "(select v.author_id, a.question_id from Vote v inner join Answer_Votes av on av.votes_id=v.id join Answer a on av.Answer_id=a.id) "
				+ "union "
				+ "(select q.author_id, q.id from Question q) "
				+ "union "
				+ "(select c.author_id, a.question_id from Answer a inner join Answer_Comments ac on a.id=ac.Answer_id inner join Comment c on c.id=ac.comments_id)";
	
		return RawSQLOperation.forSqls(sql);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
