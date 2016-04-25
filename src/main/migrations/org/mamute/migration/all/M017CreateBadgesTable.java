package org.mamute.migration.all;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 *
 */
@ApplicationScoped
public class M017CreateBadgesTable implements SchemaMigration {
    @Override
    public List<MigrationOperation> up() {
        String q1 = " create table Badges (\n" +
                "        id bigint not null auto_increment,\n" +
                "        user_id bigint not null,\n" +
                "        badgeKey varchar(255),\n" +
                "        createdAt datetime,\n" +
                "        comment varchar(255),\n" +
                "        primary key (id),\n" +
                "        foreign key (user_id)\n" +
                "        references Users (id)" +
                "    ) ENGINE=InnoDB";

        return RawSQLOperation.forSqls(q1);
    }

    @Override
    public List<MigrationOperation> down() {
        return RawSQLOperation.forSqls();
    }
}
