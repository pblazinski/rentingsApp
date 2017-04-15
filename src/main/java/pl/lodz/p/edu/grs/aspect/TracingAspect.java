package pl.lodz.p.edu.grs.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TracingAspect {

    private static final Logger logger = LoggerFactory.getLogger(TracingAspect.class);

    @Before("pl.lodz.p.edu.grs.aspect.GameRantingsPointcuts.asyncControllerMethod()")
    public void tracingController(JoinPoint joinPoint) {
        logger.info("{}", joinPoint.getSignature());
    }

}
