package com.mixer.raw;

import java.util.HashMap;

public final class Index {
    private static Index index;
    private HashMap<Long, Long> rowIndex = new HashMap<>(); // key: row num, value: byte position
    private long totalRowNumber = 0;

    private Index() {

    }
    public static Index getInstance() {
        if(index == null) {
            index = new Index();
        }
        return index;
    }

    public void add(long bytePosition) {
        this.rowIndex.put(this.totalRowNumber, bytePosition);
        this.totalRowNumber++;
    }
    public long getBytePosition(long rowNumber) {
        return this.rowIndex.getOrDefault(rowNumber, (long)-1);
    }
    public void remove(int row) {
        this.rowIndex.remove(row);
        this.totalRowNumber--;
    }

    public long getTotalRowNumber() {
        return this.totalRowNumber;
    }
}
