package com.hemaapp.xaar.adapter.lv;

import java.util.ArrayList;

public interface LvDataChangeListener<T> {
    void refresh(ArrayList<T> datas);

    void addAll(ArrayList<T> datas);

    void clear();


    void remove(int position);


    void add(T item);
}
