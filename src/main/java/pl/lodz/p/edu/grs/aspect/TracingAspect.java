package pl.lodz.p.edu.grs.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Slf4j
@Component
public class TracingAspect {

    @Before("pl.lodz.p.edu.grs.aspect.GameRantingsPointcuts.asyncControllerMethod()")
    public void tracingController(final JoinPoint joinPoint) {
        log.info("Method invoked: {}", joinPoint.getSignature());
    }

    @Around("pl.lodz.p.edu.grs.aspect.GameRantingsPointcuts.monitored()")
    public Object monitor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Measuring time for method {}", proceedingJoinPoint.getSignature());
        StopWatch stopWatch = new StopWatch();
        Object result = countProceedingTime(proceedingJoinPoint, stopWatch);

        log.info("Took {} ms", stopWatch.getTotalTimeMillis());

        return result;
    }

    private Object countProceedingTime(ProceedingJoinPoint proceedingJoinPoint, StopWatch stopWatch) throws Throwable {
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        return result;
    }

}
