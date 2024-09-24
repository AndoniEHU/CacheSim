package com.ac.cachesim;

import java.util.List;

public class SimData {

    private List<TableRow> tableBody;

    private boolean hit;

    private int opTime;

    private int modifiedIndex;

    public SimData(List<TableRow> tableBody, boolean hit, int opTime) {
        this.tableBody = tableBody;
        this.hit = hit;
        this.opTime = opTime;
    }

    public SimData(List<TableRow> tableBody, boolean hit, int opTime, int modifiedIndex) {
        this.tableBody = tableBody;
        this.hit = hit;
        this.opTime = opTime;
        this.modifiedIndex = modifiedIndex;
    }

    public List<TableRow> getTableBody() {
        return tableBody;
    }

    public void setTableBody(List<TableRow> tableBody) {
        this.tableBody = tableBody;
    }

    public int getOpTime() {
        return opTime;
    }

    public void setOpTime(int opTime) {
        this.opTime = opTime;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public int getModifiedIndex() {
        return modifiedIndex;
    }

    public void setModifiedIndex(int modifiedIndex) {
        this.modifiedIndex = modifiedIndex;
    }
}
