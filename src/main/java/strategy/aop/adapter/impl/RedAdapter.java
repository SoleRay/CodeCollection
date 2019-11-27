package strategy.aop.adapter.impl;

import strategy.aop.adapter.ColorAdapter;
import strategy.aop.advice.ColorAdvice;
import strategy.aop.advice.RedAdvice;
import strategy.aop.interceptor.ColorInterceptor;
import strategy.aop.interceptor.impl.RedInterceptor;

public class RedAdapter implements ColorAdapter {

    @Override
    public boolean supportsAdvice(ColorAdvice advice) {
        return (advice instanceof RedAdvice);
    }

    @Override
    public ColorInterceptor getColorInterceptor(ColorAdvice advice) {
        return new RedInterceptor((RedAdvice) advice);
    }
}
