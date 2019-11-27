package strategy.aop.interceptor.impl;

import strategy.aop.advice.RedAdvice;
import strategy.aop.interceptor.ColorInterceptor;

public class RedInterceptor implements ColorInterceptor {

    private RedAdvice advice;

    public RedInterceptor(RedAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke() {
        return advice.paintRed();
    }
}
