package express;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import express.dto.ExpressTraceDTO;
import express.enums.Kd100ExpressEnum;
import express.enums.Kd100StateEnum;
import express.enums.KdnExpressEnum;
import express.enums.KdnStateEnum;
import express.util.ExpressKd100Utils;
import express.util.ExpressKdnUtils;
import org.apache.commons.lang3.StringUtils;

//@Controller
//@RequestMapping("/express")
public class ExpressController  {

//    @RequestMapping(value="/trace",method= RequestMethod.POST)
//    @ResponseBody
    public Object expressTrace(String expressCode,String expressNo) throws Exception {

        ExpressTraceDTO dto = null;

        String traces = ExpressKd100Utils.getTraces(Kd100ExpressEnum.getCodeByShortCode(expressCode), expressNo);

        if(StringUtils.isNotBlank(traces)) {

            dto= parseKd100Traces(traces);

        }else {
            traces = ExpressKdnUtils.getOrderTracesByJson(expressCode, expressNo);
            dto =  parseKdnTraces(traces);
        }

        return dto;
//        return ResponseUtil.setSuccessDataResponse(dto);
    }

    private ExpressTraceDTO parseKd100Traces(String traces) {

        ExpressTraceDTO dto = new ExpressTraceDTO();

        JSONObject jsonObject = (JSONObject) JSON.parse(traces);
        int state = jsonObject.getInteger("state");
        String expressCompanyCode = jsonObject.getString("com");
        String expressCode = jsonObject.getString("nu");
        JSONArray data = jsonObject.getJSONArray("data");


        dto.setStateStr(Kd100StateEnum.getNameByCode(state));
        dto.setExpressCode(expressCode);
        dto.setExpressCompanyName(Kd100ExpressEnum.getNameByCode(expressCompanyCode));

        for(Object object : data){
            JSONObject trace = (JSONObject) object;

            dto.build(trace.getString("time"),trace.getString("context"),0);
        }
        return dto;
    }


    private ExpressTraceDTO parseKdnTraces(String traces) {

        ExpressTraceDTO dto = new ExpressTraceDTO();

        JSONObject jsonObject = (JSONObject) JSON.parse(traces);
        int state = jsonObject.getInteger("State");
        String expressCompanyCode = jsonObject.getString("ShipperCode");
        String expressCode = jsonObject.getString("LogisticCode");
        JSONArray data = jsonObject.getJSONArray("Traces");

        dto.setStateStr(KdnStateEnum.getNameByCode(state));
        dto.setExpressCode(expressCode);
        dto.setExpressCompanyName(KdnExpressEnum.getNameByCode(expressCompanyCode));

        for(Object object : data){
            JSONObject trace = (JSONObject) object;

            dto.build(trace.getString("AcceptTime"),trace.getString("AcceptStation"));
        }
        return dto;
    }
}
