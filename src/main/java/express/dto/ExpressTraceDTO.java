package express.dto;

import java.util.ArrayList;
import java.util.List;

public class ExpressTraceDTO {

    private String stateStr;

    private String expressCode;

    private String expressCompanyName;

    List<Trace> traces = new ArrayList<>();

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public String getExpressCompanyName() {
        return expressCompanyName;
    }

    public void setExpressCompanyName(String expressCompanyName) {
        this.expressCompanyName = expressCompanyName;
    }

    public List<Trace> getTraces() {
        return traces;
    }

    public void setTraces(List<Trace> traces) {
        this.traces = traces;
    }

    public void build(String time,String context){
        Trace trace = new Trace();
        trace.setTime(time);
        trace.setContext(context);

        traces.add(trace);
    }

    public void build(String time,String context,int index){
        Trace trace = new Trace();
        trace.setTime(time);
        trace.setContext(context);

        traces.add(index,trace);
    }

    private class Trace {
        private String time;

        private String context;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }
    }
}
