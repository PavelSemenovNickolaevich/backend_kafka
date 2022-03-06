package io.fraud.database;

import io.fraud.database.dao.DealDao;
import io.fraud.database.model.Deal;
import io.fraud.kafka.ProjectConfig;
import org.aeonbits.owner.ConfigFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.postgresql.ds.PGSimpleDataSource;

import java.util.List;

public class DbService {

    private final Jdbi jdbi;


    public DbService() {
        ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerName(config.dbHost());
        ds.setPortNumber(config.dbPort());
        ds.setDatabaseName(config.dbName());
        ds.setUser(config.dbUser());
        ds.setPassword(config.dbPassword());

        this.jdbi = Jdbi.create(ds);
        this.jdbi.installPlugin(new SqlObjectPlugin());
    }

    public Deal findDealById(int id) {
       return jdbi.onDemand(DealDao.class).findById(id);
    }

    public List<Deal> findByCurrency(String currency) {
        return jdbi.onDemand(DealDao.class).findByCurrency(currency);
    }
}
