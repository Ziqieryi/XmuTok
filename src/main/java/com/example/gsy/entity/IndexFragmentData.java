package com.example.gsy.entity;

import java.io.Serializable;
import java.util.List;

public class IndexFragmentData implements Serializable {
    public List<ItemFragmentData> data;
    public IndexFragmentData(List<ItemFragmentData> data){
        this.data = data;
    }
}
