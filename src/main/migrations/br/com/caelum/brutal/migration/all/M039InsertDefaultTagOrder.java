package br.com.caelum.brutal.migration.all;

import static br.com.caelum.brutal.migration.RawSQLOperation.forSqls;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M039InsertDefaultTagOrder implements Migration {
	
	private static Logger LOG = Logger.getLogger(M039InsertDefaultTagOrder.class); 

	@Override
	@SuppressWarnings("unchecked")
	public List<MigrationOperation> up() {
		MigrationOperation operation = new MigrationOperation() {
			@Override
			public void execute(Session session, StatelessSession statelessSession) {
				Query query = session.createSQLQuery("select QuestionInformation_id, tags_id, tag_order from QuestionInformation_Tag order by QuestionInformation_id");
				List<Object[]> results = query.list();
				int tagOrder = 0;
				BigInteger currentId = null;
				for (Object[] row : results) {
					LOG.info("updating QuestionInformation_Tag of id = " + currentId);
					BigInteger nextId = (BigInteger) row[0];
					BigInteger tagId = (BigInteger) row[1];
					if (!nextId.equals(currentId)) {
						tagOrder = 0;
						currentId = nextId;
					}
					
					String sql = "update QuestionInformation_Tag qt "
							+ "set qt.tag_order=:count "
							+ "where qt.QuestionInformation_id=:id and qt.tags_id=:tagId";
					session.createSQLQuery(sql)
						.setParameter("count", tagOrder)
						.setParameter("id", currentId)
						.setParameter("tagId", tagId)
						.executeUpdate();
					tagOrder++;
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
