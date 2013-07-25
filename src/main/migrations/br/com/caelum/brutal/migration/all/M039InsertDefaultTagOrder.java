package br.com.caelum.brutal.migration.all;

import static br.com.caelum.brutal.migration.RawSQLOperation.forSqls;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M039InsertDefaultTagOrder implements Migration {

	@Override
	@SuppressWarnings("unchecked")
	public List<MigrationOperation> up() {
		MigrationOperation operation = new MigrationOperation() {
			@Override
			public void execute(Session session, StatelessSession statelessSession) {
				List<QuestionInformation> questionInformations = session.createQuery("from QuestionInformation").list();
				for (QuestionInformation questionInformation : questionInformations) {
					Query query = session.createSQLQuery("select * from QuestionInformation_Tag where QuestionInformation_id=:id");
					List<Object[]> list = query.setParameter("id", questionInformation.getId()).list();
					int count = 0;
					for (Object[] row : list) {
						String sql = "update QuestionInformation_Tag qt "
								+ "set qt.tag_order=:count "
								+ "where qt.QuestionInformation_id=:id and qt.tags_id=:tagId";
						session.createSQLQuery(sql)
							.setParameter("count", count)
							.setParameter("id", row[0])
							.setParameter("tagId", row[1])
							.executeUpdate();
						count++;
					}
				}
			}
		};
		return Arrays.asList(operation);
	}

	@Override
	public List<MigrationOperation> down() {
		return forSqls("");
	}
}
