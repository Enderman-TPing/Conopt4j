package io.github.et.conopt4j.launcher;

import io.github.et.conopt4j.launcher.streams.Err;
import io.github.et.conopt4j.launcher.streams.Out;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
class LoggerAspect {

    @Before("@annotation(io.github.et.conopt4j.launcher.annotations.UseLogger)")
    public void beforeMainMethod() {
        Out.initialize();
        Err.initialize();
    }
}