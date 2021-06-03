package com.powersi.midd.mapper;

import com.powersi.midd.api.medicalInsur.pojo.Setl_medical_insur;

import java.util.List;

public interface MedicalInsurSettleMapper {
    void insertList(List<Setl_medical_insur> setl_medical_insurs);
    void truncateTable();
}
