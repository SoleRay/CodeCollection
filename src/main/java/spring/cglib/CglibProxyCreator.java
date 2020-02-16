package spring.cglib;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.asm.Type;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.cglib.core.*;
import org.springframework.cglib.proxy.*;
import org.springframework.cglib.transform.ClassEmitterTransformer;
import org.springframework.cglib.transform.TransformingClassGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CglibProxyCreator {

    /** NoOp.INSTANCE 很重要，所有不需要增强的方法，就会匹配到它，因为过滤器accept必须匹配到它*/
    private static final Callback[] CALLBACKS = new Callback[] {
            new BeanMethodInterceptor(),
            new SetNameInterceptor(),
            NoOp.INSTANCE
    };

    private static final String NAME_FIELD = "$$name";

    private static final String SET_NAME_METHOD = "setName";

    private static final ConditionalCallbackFilter CALLBACK_FILTER = new ConditionalCallbackFilter(CALLBACKS);


    public static Enhancer newEnhancer(Class superclass,ClassLoader classLoader)  {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(superclass);
        enhancer.setInterfaces(new Class[] {EnhancedConfiguration.class});
        enhancer.setUseFactory(false);
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setStrategy(new NameGeneratorStrategy(classLoader));
        enhancer.setCallbackFilter(CALLBACK_FILTER);
        enhancer.setCallbackTypes(CALLBACK_FILTER.getCallbackTypes());
        return enhancer;
    }

    public static Class enhanceClass(Enhancer enhancer){
        Class subClass = enhancer.createClass();
        Enhancer.registerStaticCallbacks(subClass,CALLBACKS);
        return subClass;
    }

    public static Object createProxy(Enhancer enhancer){
        enhancer.setCallbacks(CALLBACKS);
        return enhancer.create();
    }

    private interface ConditionalCallback extends Callback {

        boolean isMatch(Method candidateMethod);
    }

    /** 代理子类如果要实现该方法，必须增加Callback，定义方法实际行为 */
    public interface EnhancedConfiguration  {

        String setName(String name);
    }

    private static class ConditionalCallbackFilter implements CallbackFilter {

        private final Callback[] callbacks;

        private final Class<?>[] callbackTypes;

        public ConditionalCallbackFilter(Callback[] callbacks) {
            this.callbacks = callbacks;
            this.callbackTypes = new Class<?>[callbacks.length];
            for (int i = 0; i < callbacks.length; i++) {
                this.callbackTypes[i] = callbacks[i].getClass();
            }
        }

        @Override
        public int accept(Method method) {
            for (int i = 0; i < this.callbacks.length; i++) {
                Callback callback = this.callbacks[i];
                if (!(callback instanceof ConditionalCallback) || ((ConditionalCallback) callback).isMatch(method)) {
                    return i;
                }
            }
            throw new IllegalStateException("No callback available for method " + method.getName());
        }

        public Class<?>[] getCallbackTypes() {
            return this.callbackTypes;
        }
    }

    public static class BeanMethodInterceptor implements MethodInterceptor,ConditionalCallback {

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println("do sth before "+method.getName());
            methodProxy.invokeSuper(o,objects);
            System.out.println("do sth after "+method.getName());
            return null;
        }

        @Override
        public boolean isMatch(Method candidateMethod) {
            return candidateMethod.isAnnotationPresent(Bean.class);
        }
    }

    public static class SetNameInterceptor implements MethodInterceptor,ConditionalCallback {

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            Field field = ReflectionUtils.findField(o.getClass(), NAME_FIELD);
            field.set(o,objects[0]);
            return null;
        }

        @Override
        public boolean isMatch(Method candidateMethod) {
            return candidateMethod.getName().equals(SET_NAME_METHOD);
        }
    }

    private static class NameGeneratorStrategy extends DefaultGeneratorStrategy {

        @Nullable
        private final ClassLoader classLoader;

        public NameGeneratorStrategy(@Nullable ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        @Override
        protected ClassGenerator transform(ClassGenerator cg) throws Exception {
            ClassEmitterTransformer transformer = new ClassEmitterTransformer() {
                @Override
                public void end_class() {
                    declare_field(Constants.ACC_PUBLIC, NAME_FIELD, Type.getType(String.class), null);

                    super.end_class();
                }

            };
            return new TransformingClassGenerator(cg, transformer);
        }

        @Override
        public byte[] generate(ClassGenerator cg) throws Exception {
            if (this.classLoader == null) {
                return super.generate(cg);
            }

            Thread currentThread = Thread.currentThread();
            ClassLoader threadContextClassLoader;
            try {
                threadContextClassLoader = currentThread.getContextClassLoader();
            }
            catch (Throwable ex) {
                // Cannot access thread context ClassLoader - falling back...
                return super.generate(cg);
            }

            boolean overrideClassLoader = !this.classLoader.equals(threadContextClassLoader);
            if (overrideClassLoader) {
                currentThread.setContextClassLoader(this.classLoader);
            }
            try {
                return super.generate(cg);
            }
            finally {
                if (overrideClassLoader) {
                    // Reset original thread context ClassLoader.
                    currentThread.setContextClassLoader(threadContextClassLoader);
                }
            }
        }
    }
}
