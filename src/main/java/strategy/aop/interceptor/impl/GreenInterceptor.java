package strategy.aop.interceptor.impl;

import strategy.aop.advice.GreenAdvice;
import strategy.aop.interceptor.ColorInterceptor;

public class GreenInterceptor implements ColorInterceptor {

    private GreenAdvice advice;

    public GreenInterceptor(GreenAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke() {
        return advice.paintGreen();
    }
}
