package com.mixer.dbserver;

import com.mixer.raw.Person;

import java.io.IOException;

public interface DB {
    public void add() throws IOException;
    public void delete(int rowNum) throws IOException;
    public Person read(int rowNum) throws IOException;
}
