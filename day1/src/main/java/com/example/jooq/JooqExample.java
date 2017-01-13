package com.example.jooq;

import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.jooq.generated.Tables.JOOQRESERVATION;


/**
 * @author Kj Nam
 * @since 2017-01-09
 */
@Component
public class JooqExample implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(JooqExample.class);

    private final DSLContext dsl;

    private final JdbcTemplate jdbc;

    public JooqExample(DSLContext dsl, JdbcTemplate jdbc) {
        this.dsl = dsl;
        this.jdbc = jdbc;
    }

    @Override
    public void run(String... strings) throws Exception {
        jooqFetch();
        jooqSql();
    }

    private void jooqFetch() {
        Result<Record> results = this.dsl.select().from(JOOQRESERVATION).fetch();
        for (Record each : results) {
            Integer id = each.getValue(JOOQRESERVATION.ID);
            String name = each.getValue(JOOQRESERVATION.NAME);
            logger.info("service Fetch " + id + " " + name);
        }
    }

    private void jooqSql() {
        Query query = this.dsl.select(JOOQRESERVATION.ID, JOOQRESERVATION.NAME)
                .from(JOOQRESERVATION);

        Object[] bind = query.getBindValues().toArray(new Object[] {});
        List<String> list = this.jdbc.query(query.getSQL(), bind,
                (rs, i) -> rs.getString(1) + " : " + rs.getString(2));
        logger.info("service SQL" + list);
    }
}
