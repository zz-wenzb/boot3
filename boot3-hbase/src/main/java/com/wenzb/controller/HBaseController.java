package com.wenzb.controller;

import com.wenzb.service.HBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/hbase")
public class HBaseController {

    @Autowired
    private HBaseService hBaseService;

    @GetMapping("/createNamespace")
    public void createNamespace(String namespace) throws IOException {
        hBaseService.createNamespace(namespace);
    }

    @GetMapping("/tableExists")
    public void tableExists(String namespace, String tableName) throws IOException {
        hBaseService.tableExists(namespace,tableName);
    }

    @GetMapping("/createTable")
    public void createTable(String namespace, String tableName, String columnFamilie) throws IOException {
        hBaseService.createTable(namespace,tableName,columnFamilie);
    }
}
