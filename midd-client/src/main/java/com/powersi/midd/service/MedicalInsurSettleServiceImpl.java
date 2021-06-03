package com.powersi.midd.service;

import cn.hutool.http.HttpUtil;
import com.powersi.midd.api.medicalInsur.pojo.Setl_medical_insur;
import com.powersi.midd.api.medicalInsur.service.MedicalInsurSettleService;
import com.powersi.midd.mapper.MedicalInsurSettleMapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.DateUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Author sunxuelin
 * @Description //医保数据抽取调用执行service
 * @Date 2021/1/21
 **/
@Service("medicalInsurSettleService")
public class MedicalInsurSettleServiceImpl implements MedicalInsurSettleService , Serializable {
    @Resource
    private MedicalInsurSettleMapper medicalInsurSettleMapper;
    private Logger logger = LoggerFactory.getLogger(MedicalInsurSettleServiceImpl.class);

    @Override
    public synchronized List<Setl_medical_insur> reqMedicalInsurSettleList(String start_time, String end_time, String treatment_type) {
        String paramBody = null;
        try {
            paramBody = createDocumentToString(start_time, end_time, treatment_type);
        } catch (Exception e) {
            logger.error("入参封装错误 "+e);
        }
        List<Setl_medical_insur> setl_medical_insurs = new ArrayList<>();
        String result = HttpUtil.post("http://10.141.136.25:7070/Insur_CZJM/ProcessAll", paramBody);
        if (StringUtils.isEmpty(result)) {
            logger.error("请求URLhttp://10.141.136.25:7070/Insur_CZJM/ProcessAll失败-------------");
        }
        try {
            resultDocumentToMedicalInsur(result, setl_medical_insurs);
        } catch (Exception e) {
            logger.error("解析结果失败:" + result);
            e.printStackTrace();
        }
        //分批插入数据
        if (!CollectionUtils.isEmpty(setl_medical_insurs)) {
            int numberBatch = 400; //每一次插入的最大行数
            double number = setl_medical_insurs.size() * 1.0 / numberBatch;
            int n = ((Double) Math.ceil(number)).intValue(); //向上取整
            for (int i = 0; i < n; i++) {
                int end = numberBatch * (i + 1);
                if (end > setl_medical_insurs.size()) {
                    end = setl_medical_insurs.size(); //如果end不能超过最大索引值
                }
                medicalInsurSettleMapper.insertList(setl_medical_insurs.subList(numberBatch * i, end));
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnT jobReqMedicalInsurSettleList(String treatment_type) {
        try {
            logger.info("定时任务抽取开启");
            logger.info("清空表.........");
            medicalInsurSettleMapper.truncateTable();
            logger.info("清空表完成");
            String start_time = "2020-01-01";
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String current_time = simpleDateFormat.format(date);
            List<String> dateList = com.powersi.midd.utils.DateUtil.cutDate("M", start_time, current_time);
            for (int i = 0; i < dateList.size() - 1; i++) {
                logger.info("抽取:" + dateList.get(i) + "-" + com.powersi.midd.utils.DateUtil.subDay(dateList.get(i + 1)) + "时间段的数据........");
                reqMedicalInsurSettleList(dateList.get(i), com.powersi.midd.utils.DateUtil.subDay(dateList.get(i + 1)), treatment_type);
                logger.info("抽取:" + dateList.get(i) + "-" + com.powersi.midd.utils.DateUtil.subDay(dateList.get(i + 1)) + "时间段的数据完成");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnT(500, "执行executeGetMedicalInsur出错！");

        }
        return new ReturnT(200, "执行医保数据抽取成功");
    }

    /**
     * 封装xml字符串参数
     *
     * @param start_time
     * @param end_time
     * @param treatment_type
     * @return
     */
    private static String createDocumentToString(String start_time, String end_time, String treatment_type) throws Exception {
        Document document = DocumentHelper.createDocument();
        Element program = document.addElement("Program");
        Element functionID = program.addElement("FunctionID");
        functionID.setText("BIZC140101");
        Element parameters = program.addElement("parameters");
        parameters.addElement("end_date").setText(end_time);
        parameters.addElement("oper_flag").setText("1");
        parameters.addElement("decl_staff").setText("东软");
        parameters.addElement("treatment_type").setText(treatment_type);
        parameters.addElement("decl_staff_id").setText("009");
        parameters.addElement("hospital_id").setText("4310000005");
        parameters.addElement("in_area").setText("A");
        parameters.addElement("oper_centerid").setText("431000");
        parameters.addElement("oper_hospitalid").setText("4310000005");
        parameters.addElement("in_dept").setText("A");
        parameters.addElement("center_id").setText("431000");
        parameters.addElement("oper_staffid").setText("009");
        parameters.addElement("decl_type").setText("22");
        parameters.addElement("save_flag").setText("0");
        parameters.addElement("reg_flag").setText("0");
        parameters.addElement("start_date").setText(start_time);
        return document.asXML();
    }

    /**
     * 从classpath读取入参配置
     *
     * @return
     * @throws Exception
     */
    public static Document getDocumentForURI() throws Exception {
        SAXReader reader = new SAXReader();
        org.springframework.core.io.Resource resource = new ClassPathResource("static/param.xml");
        InputStream is = resource.getInputStream();
        Document document = reader.read(is);
        return document;
    }

    private static String dayToMonth(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(time);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
        return sdf2.format(date);
    }

    /**
     * 请求返回结果解析成setl_medical_insur
     *
     * @param result
     * @param setl_medical_insurs
     * @throws Exception
     */
    private static void resultDocumentToMedicalInsur(String result, List<Setl_medical_insur> setl_medical_insurs) throws Exception {
        Document document = DocumentHelper.parseText(result);
        Element program = document.getRootElement();
        Element detailpay = program.element("detailpay");
        List<Element> elements = detailpay.elements();
        Setl_medical_insur setl_medical_insur = null;
        for (Element element :
                elements) {
            setl_medical_insur = new Setl_medical_insur();
            setl_medical_insur.setLarge_306_pay(getText(element.element("large_306_pay")));
            setl_medical_insur.setSerial_no(getText(element.element("serial_no")));
            setl_medical_insur.setIndi_id(getText(element.element("indi_id")));
            setl_medical_insur.setDistrict_code(getText(element.element("district_code")));
            setl_medical_insur.setAll_999_pay(getText(element.element("all_999_pay")));
            setl_medical_insur.setTotal_fee(new BigDecimal(getText(element.element("total_fee"))));
            setl_medical_insur.setAll_519_pay(getText(element.element("all_519_pay")));
            setl_medical_insur.setReimburse_flag(getText(element.element("reimburse_flag")));
            setl_medical_insur.setAll_411_pay(getText(element.element("all_411_pay")));
            setl_medical_insur.setIdcard(getText(element.element("idcard")));
            setl_medical_insur.setBegin_date(getText(element.element("begin_date")));
            setl_medical_insur.setFirst_self_pay(getText(element.element("first_self_pay")));
            setl_medical_insur.setBase_001_pay(new BigDecimal(getText(element.element("base_001_pay"))));
            setl_medical_insur.setExceed_996_pay(getText(element.element("exceed_996_pay")));
            setl_medical_insur.setCenter_name(getText(element.element("center_name")));
            setl_medical_insur.setQfx_306_pay(getText(element.element("qfx_306_pay")));
            setl_medical_insur.setExceed_306_pay(getText(element.element("exceed_306_pay")));
            setl_medical_insur.setAll_801_pay(getText(element.element("all_801_pay")));
            setl_medical_insur.setVillage_name(getText(element.element("village_name")));
            setl_medical_insur.setAll_003_pay(getText(element.element("all_003_pay")));
            setl_medical_insur.setAll_401_pay(getText(element.element("all_401_pay")));
            setl_medical_insur.setAll_005_pay(getText(element.element("all_005_pay")));
            setl_medical_insur.setPart_self_pay(getText(element.element("part_self_pay")));
            setl_medical_insur.setAll_802_pay(getText(element.element("all_802_pay")));
            setl_medical_insur.setDisease(getText(element.element("disease")));
            setl_medical_insur.setLarge_self_pay(getText(element.element("large_self_pay")));
            setl_medical_insur.setIn_dept_name(getText(element.element("in_dept_name")));
            setl_medical_insur.setAll_self_pay(getText(element.element("all_self_pay")));
            setl_medical_insur.setEnd_date(getText(element.element("end_date")));
            setl_medical_insur.setPart_self_pay_301(getText(element.element("part_self_pay_301")));
            setl_medical_insur.setAll_998_pay(getText(element.element("all_998_pay")));
            setl_medical_insur.setName(getText(element.element("name")));
            setl_medical_insur.setLarge_201_pay(getText(element.element("large_201_pay")));
            setl_medical_insur.setPers_type(getText(element.element("pers_type")));
            setl_medical_insur.setTreatment_type(getText(element.element("treatment_type")));
            setl_medical_insur.setFin_date(DateUtil.parse(getText(element.element("fin_date")), "yyyy-MM-dd"));
            setl_medical_insur.setPart_self_pay_306(getText(element.element("part_self_pay_306")));
            setl_medical_insur.setQfx_301_pay(getText(element.element("qfx_301_pay")));
            setl_medical_insur.setAll_201_pay(getText(element.element("all_201_pay")));
            setl_medical_insur.setAll_306_pay(getText(element.element("all_306_pay")));
            setl_medical_insur.setBase_301_pay(getText(element.element("base_301_pay")));
            setl_medical_insur.setExceed_self_pay(getText(element.element("exceed_self_pay")));
            setl_medical_insur.setAll_996_998_pay(getText(element.element("all_996_998_pay")));
            setl_medical_insur.setCard_no(getText(element.element("card_no")));
            setl_medical_insur.setBase_self_pay(getText(element.element("base_self_pay")));
            setl_medical_insur.setExceed_301_pay(getText(element.element("exceed_301_pay")));
            setl_medical_insur.setAll_self_pay_306(getText(element.element("all_self_pay_306")));
            setl_medical_insur.setBase_306_pay(getText(element.element("base_306_pay")));
            setl_medical_insur.setAll_self_pay_301(getText(element.element("all_self_pay_301")));
            setl_medical_insur.setHospital_id(getText(element.element("hospital_id")));
            setl_medical_insur.setAll_202_pay(getText(element.element("all_202_pay")));
            setl_medical_insur.setAll_511_pay(getText(element.element("all_511_pay")));
            setl_medical_insur.setAll_301_pay(getText(element.element("large_301_pay")));
            setl_medical_insur.setQfx_self_pay(getText(element.element("qfx_self_pay")));
            setl_medical_insur.setInjury_borth_sn(getText(element.element("injury_borth_sn")));
            setl_medical_insur.setPatient_id(getText(element.element("patient_id")));
            setl_medical_insur.setIn_days(getText(element.element("in_days")));
            setl_medical_insur.setAll_301_pay(getText(element.element("all_301_pay")));
            setl_medical_insur.setRc(getText(element.element("rc")));
            setl_medical_insur.setOrg_id("30015");
            setl_medical_insurs.add(setl_medical_insur);
            System.out.println(setl_medical_insur.toString());
        }

    }


    /**
     * xml元素为空时赋初值
     *
     * @param element
     * @return
     */
    private static String getText(Element element) {
        return element == null ? "" : element.getText();
    }

}
