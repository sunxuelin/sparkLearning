package com.powersi.midd.mapper;

import com.powersi.midd.api.medicalInsur.pojo.Item;

import java.util.List;

public interface ItemMapper {
    void insertList(List<Item> items);

    List<Item> queryAll(Long size);
}
