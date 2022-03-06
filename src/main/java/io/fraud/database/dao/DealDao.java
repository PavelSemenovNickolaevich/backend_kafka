package io.fraud.database.dao;

import io.fraud.database.model.Deal;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterBeanMapper(Deal.class)
public interface DealDao {

    @SqlQuery("SELECT * from deals WHERE id = ?")
    Deal findById(int id);

    @SqlQuery("SELECT * from deals WHERE currency = ?")
    List<Deal> findByCurrency(String currency);
}
