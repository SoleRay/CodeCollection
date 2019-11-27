package strategy.aop.adapter.impl;

import strategy.aop.adapter.ColorAdapter;
import strategy.aop.advice.ColorAdvice;
import strategy.aop.advice.GreenAdvice;
import strategy.aop.interceptor.ColorInterceptor;
import strategy.aop.interceptor.impl.GreenInterceptor;

public class GreenAdapter implements ColorAdapter {

    @Override
    public boolean supportsAdvice(ColorAdvice advice) {
        return (advice instanceof GreenAdvice);
    }

    @Override
    public ColorInterceptor getColorInterceptor(ColorAdvice advice) {
        return new GreenInterceptor((GreenAdvice) advice);
    }
}
