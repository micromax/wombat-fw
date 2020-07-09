package com.cloudsgen.system;

import com.cloudsgen.system.core.DataBase.DataBaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.DbMigrationConfig;
import io.ebean.config.ServerConfig;
import io.vertx.core.*;
import io.vertx.core.eventbus.Message;

import java.io.File;

public class boot extends AbstractVerticle {

    public static DataBaseConfig getConfig ;
    public static Vertx vertx ;

    public static HikariConfig config = new HikariConfig();

    public static HikariDataSource datasource;
    public static ServerConfig ServerDbconfig = new ServerConfig();
    public  static EbeanServer server;

    public boot(){}

    public void ts(){
        vertx.eventBus().consumer("news" , this::messageHandler);
    }

    public static boolean DataConnectionBoot(){

            boolean r = false;
        try {
            getConfig = new DataBaseConfig();
              System.setProperty("ddl.migration.generate", "true");
            String dbURL =  getConfig.getDataBaseConf().getUrl() ;//;"jdbc:h2:~/sadeemdb";
            //CreatVisSql();
//        createTablesIfNotExist(dbURL);
            config.setDriverClassName( getConfig.getDataBaseConf().getDriver());
            config.setJdbcUrl(dbURL);
            config.setUsername( getConfig.getDataBaseConf().getUsername());
            config.setPassword( getConfig.getDataBaseConf().getPassword());

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.setMinimumIdle(2);
            config.setMaximumPoolSize(10);
            config.setAutoCommit(false);


            datasource = new HikariDataSource(config);
            DbMigrationConfig dbMigrationConfig = new DbMigrationConfig() ;
            dbMigrationConfig.setRunMigration(true);
            ServerDbconfig.setDdlGenerate(true);
            ServerDbconfig.setMigrationConfig(dbMigrationConfig);
            ServerDbconfig.setName("chatserver");
            ServerDbconfig.setDataSource(datasource);
            ServerDbconfig.setDefaultServer(true);
            // Properties properties = new Properties();
            //  properties.put("ebean.db.ddl.generate", "true");
            // properties.put("ebean.db.ddl.run", "true");
            ServerDbconfig.loadFromProperties();
            server = EbeanServerFactory.create(ServerDbconfig);
            r = true;
        }catch (RuntimeException e)
        {
            r = false;
        }



        return r;


    }






    public static void main(String[] args) {

        boolean dberr = false;

        File outer = new File("database.properties");
        boolean exists = outer.exists();
        if(exists == true)
        {
            dberr =  DataConnectionBoot();

        }





        VertxOptions options = new VertxOptions();
        options.setBlockedThreadCheckInterval(8000);
        options.setMaxEventLoopExecuteTime(options.getMaxEventLoopExecuteTime() * 20);
        options.setMaxWorkerExecuteTime(options.getMaxWorkerExecuteTime() * 2);
        options.setWarningExceptionTime(Long.MAX_VALUE);
        options.setPreferNativeTransport(true);

        vertx = Vertx.vertx(options);
        vertx.deployVerticle("com.cloudsgen.system.Luncher", new DeploymentOptions().setWorker(true).setWorkerPoolSize(40).setInstances(4));





    }

    private Handler<Object> messageHandler(Message message){

        return Object::notify;
    }





}
