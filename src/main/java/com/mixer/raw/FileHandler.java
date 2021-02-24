package com.mixer.raw;

import jdk.jshell.execution.JdiExecutionControl;

import javax.management.openmbean.CompositeDataInvocationHandler;
import java.io.*;

public class FileHandler {
    private RandomAccessFile dbFile;

    public FileHandler(final String dbFileName) throws FileNotFoundException {
        this.dbFile = new RandomAccessFile(dbFileName, "rw");

    }

    public boolean add(String name, int age, String address, String carPlateNumber, String description) throws IOException {
        // seek to the end of the file
        Long currentPositionToInsert = this.dbFile.length();
        this.dbFile.seek(currentPositionToInsert);
        // isDeleted Byte -> record length (int) -> name length (int) -> name -> address length -> address -> carplatenumber length
        // carplatenumber -> description length -> description
        // record length
        int length = 4 * 5 + name.length() + address.length() + carPlateNumber.length() + description.length();
        this.dbFile.writeBoolean(false);
        this.dbFile.writeInt(length);
        this.dbFile.writeInt(name.length());
        this.dbFile.write(name.getBytes());
        this.dbFile.writeInt(age);
        this.dbFile.writeInt(address.length());
        this.dbFile.write(address.getBytes());
        this.dbFile.writeInt(carPlateNumber.length());
        this.dbFile.write(carPlateNumber.getBytes());
        this.dbFile.writeInt(description.length());
        this.dbFile.write(description.getBytes());

        Index.getInstance().add(currentPositionToInsert );
        return true;

    }

    public Person readRow(int rowNumber) throws IOException {
        long bytePosition = Index.getInstance().getBytePosition(rowNumber);
        if(bytePosition  == -1) {
            return null;
        }
        byte[] row = this.readRowRecord(bytePosition);
        Person person = new Person();
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(row));
        int nameLength = stream.readInt();
        byte[] b = new byte[nameLength];
        stream.read(b);
        person.name = new String(b);
        person.age = stream.readInt();
        b = new byte[stream.readInt()];
        stream.read(b);
        person.address = new String(b);
        b = new byte[stream.readInt()];
        stream.read(b);
        person.carPlateNumber = new String(b);
        b = new byte[stream.readInt()];
        stream.read(b);
        person.description = new String(b);
        return person;
    }

    private byte[] readRowRecord(long bytePositionOfRow) throws IOException {
        // transfer rowNumber to byte position
        this.dbFile.seek(bytePositionOfRow);
        if(this.dbFile.readBoolean()) {
            return new byte[0];
        }
        this.dbFile.seek(bytePositionOfRow+1);
        int recordLength = this.dbFile.readInt();
        this.dbFile.seek(bytePositionOfRow+5);
        byte[] data = new byte[recordLength];
        this.dbFile.read(data);

        return data;
    }

    public void loadAllDataToIndex() throws IOException {
        if(this.dbFile.length() == 0) {
            return;
        }
        long currPos = 0, rowNum = 0;
        while(currPos < this.dbFile.length()) {
            this.dbFile.seek(currPos);
            boolean isDeleted = this.dbFile.readBoolean();
            if(!isDeleted) {
                Index.getInstance().add(currPos);
                rowNum++;
            }
            currPos += 1;
            this.dbFile.seek(currPos);
            int recordLength = this.dbFile.readInt();
            currPos += 4 + recordLength; // the recordLength is 4 byte
        }
        System.out.println("Row Num in DB: " + rowNum);
    }

    public void close() throws IOException {
        this.dbFile.close();
    }
}
