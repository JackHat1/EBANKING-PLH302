package com.bank.manager;

import com.bank.model.accounts.Account;
import com.bank.model.statements.StatementEntry;
import com.bank.storage.CsvStorageManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatementManager {

    private final String basePath = "./data/statements/";
    private final CsvStorageManager storage= new CsvStorageManager();

    public StatementManager() {
        File dir = new File(basePath);
        if (!dir.exists()) dir.mkdirs();
    }


    public void saveStatement(Account acc, StatementEntry entry) {
        String fullPath = basePath + acc.getIban() + ".csv";

        storage.save(entry,fullPath, true);
    }


    public List<StatementEntry> load(Account acc) {

        String path = basePath + acc.getIban() + ".csv";
        List<StatementEntry> list = new ArrayList<>();
        List<String> lines= storage.loadLines(path);

        for(String line: lines){
            StatementEntry entry= new StatementEntry();
            
            entry.unmarshal(line);
            list.add(entry);
        }

        Collections.reverse(list); 
        return list;

    }



}