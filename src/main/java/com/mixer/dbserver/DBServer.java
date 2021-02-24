package com.mixer.dbserver;

import com.mixer.raw.FileHandler;
import com.mixer.raw.Person;

import java.io.FileNotFoundException;
import java.io.IOException;

public final class DBServer implements DB {
    private FileHandler fileHandler;
    public DBServer(String dbFileName) throws IOException {
        this.fileHandler = new FileHandler(dbFileName);
        this.fileHandler.loadAllDataToIndex();
    }

    public void close() throws IOException {
        this.fileHandler.close();
    }

    @Override
    public void add() {

    }

    @Override
    public void delete(int rowNum) {

    }

    @Override
    public Person read(int rowNum) {
        return null;
    }
}
