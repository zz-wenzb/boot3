package com.wenzb.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author wenzb
 */
@Configuration
public class HbaseConfig {

    @Value("${hbase.zk}")
    private String zk;

    @Bean(name = "hbaseConn")
    public Connection getTable() throws IOException {
        var config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zk);
        return ConnectionFactory.createConnection(config);
    }
}
