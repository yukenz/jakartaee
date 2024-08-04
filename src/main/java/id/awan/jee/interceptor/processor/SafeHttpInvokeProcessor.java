package id.awan.jee.interceptor.processor;

import id.awan.jee.interceptor.annotation.SafeHttpInvoke;
import id.awan.jee.model.entity.GenericResponse;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@SafeHttpInvoke
public class SafeHttpInvokeProcessor {

    @AroundInvoke
    Object safeHttpInvoke(InvocationContext ic) throws Exception {

        try {
            return ic.proceed();
        } catch (Throwable e) {
            return GenericResponse.sysErr(e);
        }

    }

}
