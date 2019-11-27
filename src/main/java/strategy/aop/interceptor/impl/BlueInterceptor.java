package strategy.aop.interceptor.impl;


import strategy.aop.advice.BlueAdvice;
import strategy.aop.interceptor.ColorInterceptor;

public class BlueInterceptor implements ColorInterceptor {

    private BlueAdvice advice;

    public BlueInterceptor(BlueAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke() {
        return advice.paintBlue();
    }
}
