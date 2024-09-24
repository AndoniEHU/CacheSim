package com.ac.cachesim;


public class CacheBlock {

    private int block;
    private int set;
    private int tag;


    public CacheBlock(int block, int tag, int set) {
        this.block = block;
        this.tag = tag;
        this.set = set;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}

