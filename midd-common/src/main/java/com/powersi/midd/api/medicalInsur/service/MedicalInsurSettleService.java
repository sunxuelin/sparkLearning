package com.powersi.midd.api.medicalInsur.service;

import com.powersi.midd.api.medicalInsur.pojo.Setl_medical_insur;
import com.xxl.job.core.biz.model.ReturnT;
import java.util.List;

/**
 * @Author sunxuelin
 * @Description //医保结算结果
 * @Date 2021/01/21
 **/
public interface MedicalInsurSettleService {
    /**
     * @param start_time
     * @param end_time
     * @param treatment_type
     * @return
     */
    List<Setl_medical_insur> reqMedicalInsurSettleList(String start_time, String end_time, String treatment_type);

    /**
     * @param treatment_type
     * @return
     */
    ReturnT jobReqMedicalInsurSettleList(String treatment_type);
}
