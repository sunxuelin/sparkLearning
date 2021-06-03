package com.powersi.midd.api.medicalInsur.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: PowerSI
 * @date: 2021/4/30 16:08
 * @description:
 */
@Data
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private String  pid;
    private String item_id;
}
