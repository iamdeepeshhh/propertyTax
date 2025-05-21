package com.GAssociatesWeb.GAssociates.Service.SequenceServices;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;


@Service
public class SequenceService {

    @PersistenceContext
    private EntityManager entityManager;

    Logger logger = LoggerFactory.getLogger(SequenceService.class);

    //this db user is coming from docker-compose - DB_USER=anildb
    private static final String DB_USER = "anildb";

    @Transactional
    public void ensureSequenceExists(String sequenceName, String ownedByColumn, long Value) throws Exception {
        String createSequenceSql = String.format(
                "DO $$ " +
                        "BEGIN " +
                        "IF NOT EXISTS (SELECT FROM pg_class where relkind = 'S' AND relname = '%1$s') THEN " +
                        "CREATE SEQUENCE %1$s " +
                        "INCREMENT 1 " +
                        "START 1 " +
                        "MINVALUE 1 " +
                        "MAXVALUE %3$d " +
                        "CACHE 1 " +
                        "OWNED BY %2$s; " +
                        "ALTER SEQUENCE %1$s OWNER TO " + DB_USER + "; " +
                        "END IF; " +
                        "END " +
                        "$$;",
                sequenceName, ownedByColumn, Value);
        try {
            entityManager.createNativeQuery(createSequenceSql).executeUpdate();
        } catch (Exception e) {
            // Log the exception, optionally rethrow or handle it based on your application's needs
            logger.error("An Error occurred while creating sequence", e);
        }
    }

    public Integer getNextSequenceValue(String sequenceName) {
        String sql = String.format("SELECT nextval('%s');", sequenceName);
        Object result = entityManager.createNativeQuery(sql).getSingleResult();
        return ((Number) result).intValue();
    }

    public Long getNextSequenceValueLong(String sequenceName) {
        String sql = String.format("SELECT nextval('%s');", sequenceName);
        Object result = entityManager.createNativeQuery(sql).getSingleResult();
        return ((Number) result).longValue();
    }

    @Transactional
    public void resetSequenceIfTableIsEmpty(String tableName, String sequenceName) {
        // Execute a native SQL query to check if the table is empty
        String sql = "SELECT COUNT(*) FROM " + tableName;
        Query query = entityManager.createNativeQuery(sql);
        Number count = (Number) query.getSingleResult();

        // If count is 0, then the table is empty, and the sequence can be reset
        if (count != null && count.longValue() == 0) {
            // Execute another native SQL to reset the sequence
            String resetSql = "ALTER SEQUENCE " + sequenceName + " RESTART WITH 1";
            entityManager.createNativeQuery(resetSql).executeUpdate();
        }
    }

    public void resetSequence(String sequenceName, int startWith) {
        String sql = String.format("ALTER SEQUENCE %s RESTART WITH %d;", sequenceName, startWith);
        entityManager.createNativeQuery(sql).executeUpdate();
    }


//This part of code is basically getting used to generate the id's so that duplication dont happens as we import the data through csv directly in database
    @Transactional
    public void adjustSequence(Class<?> entityClass, String sequenceName) {
        // Get table name dynamically
        String tableName = getTableName(entityClass)
                .orElseThrow(() -> new IllegalArgumentException("Table name not found for entity: " + entityClass.getSimpleName()));

        // Get primary key column dynamically
        String primaryKeyColumn = getPrimaryKeyColumn(entityClass)
                .orElseThrow(() -> new IllegalArgumentException("Primary key not found for entity: " + entityClass.getSimpleName()));

        String maxIdQuery = String.format("SELECT COALESCE(MAX(%s), 0) FROM %s", primaryKeyColumn, tableName);
        Long maxId = ((Number) entityManager.createNativeQuery(maxIdQuery).getSingleResult()).longValue();

        // Update the sequence to start from max ID + 1
        if (maxId != null) {
            String updateSequenceSql = String.format("SELECT setval('%s', %d, false)", sequenceName, maxId + 1);
            entityManager.createNativeQuery(updateSequenceSql).getSingleResult();
        }
    }

    public static Optional<String> getTableName(Class<?> entityClass){
        if(entityClass.isAnnotationPresent(Table.class)){
            Table table = entityClass.getAnnotation(Table.class);
            return Optional.ofNullable(table.name());
        }
        return Optional.empty();
    }

    public Optional<String> getPrimaryKeyColumn(Class<?> entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                Column column = field.getAnnotation(Column.class);
                return Optional.ofNullable(column).map(Column::name).or(() -> Optional.of(field.getName()));
            }
        }
        return Optional.empty();
    }
}
