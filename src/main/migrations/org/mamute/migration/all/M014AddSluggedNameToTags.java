package org.mamute.migration.all;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class M014AddSluggedNameToTags implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		return RawSQLOperation.forSqls(
                "ALTER TABLE Tag ADD sluggedName longtext NOT NULL AFTER name",
                "UPDATE Tag SET sluggedName=name");
	}

	@Override
	public List<MigrationOperation> down() {
        return RawSQLOperation.forSqls("ALTER TABLE Tag DROP sluggedName");
	}
}
