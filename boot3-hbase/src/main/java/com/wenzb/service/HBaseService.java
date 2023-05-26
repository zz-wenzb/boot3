package com.wenzb.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author wenzb
 */
@Service
@Slf4j
public class HBaseService {

    @Resource(name = "hbaseConn")
    private Connection connection;

    public void createNamespace(String namespace) throws IOException {
        Admin admin = connection.getAdmin();
        NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(namespace);
        builder.addConfiguration("user", "wenzb");
        admin.createNamespace(builder.build());
        admin.close();
    }

    public boolean tableExists(String namespace, String tableName) throws IOException {
        Admin admin = connection.getAdmin();
        boolean b = admin.tableExists(TableName.valueOf(namespace, tableName));
        log.info("b-->{}", b);
        admin.close();
        return b;
    }

    public void createTable(String namespace, String tableName, String... columnFamilies) throws IOException {
        if (columnFamilies.length == 0) {
            log.info("创建表格至少有一个列族");
            return;
        }
        if (tableExists(namespace, tableName)) {
            log.info("表格已经存在");
            return;
        }
        Admin admin = connection.getAdmin();
        TableDescriptorBuilder td =
                TableDescriptorBuilder.newBuilder(TableName.valueOf(namespace, tableName));
        for (String cf : columnFamilies) {
            ColumnFamilyDescriptorBuilder fd = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf));
            fd.setMaxVersions(5);
            td.setColumnFamily(fd.build());
        }
        try {
            admin.createTable(td.build());
        } catch (IOException e) {
            e.printStackTrace();

        }
        admin.close();
    }

    public void modifyTable(String namespace, String tableName, String columnFamily, int version) throws IOException {
        if (!tableExists(namespace, tableName)) {
            log.info("表格不存在无法修改");
            return;
        }
        Admin admin = connection.getAdmin();
        try {
            TableDescriptor td = admin.getDescriptor(TableName.valueOf(namespace, tableName));
            TableDescriptorBuilder tdb = TableDescriptorBuilder.newBuilder(td);
            ColumnFamilyDescriptor cf = td.getColumnFamily(Bytes.toBytes(columnFamily));
            ColumnFamilyDescriptorBuilder fd = ColumnFamilyDescriptorBuilder.newBuilder(cf);
            fd.setMaxVersions(version);
            tdb.modifyColumnFamily(fd.build());
            admin.modifyTable(tdb.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        admin.close();
    }

    public boolean deleteTable(String namespace ,String tableName) throws IOException {
        if (!tableExists(namespace,tableName)){
            System.out.println("表格不存在 无法删除");
            return false;
        }
        Admin admin = connection.getAdmin();
        try {
            TableName  tableName1  =  TableName.valueOf(namespace, tableName);
            admin.disableTable(tableName1);
            admin.deleteTable(tableName1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        admin.close();
        return true;
    }
}
