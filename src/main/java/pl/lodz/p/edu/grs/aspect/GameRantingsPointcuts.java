package pl.lodz.p.edu.grs.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class GameRantingsPointcuts {

    @Pointcut("execution(* *..controller..*(..))")
    public void asyncControllerMethod() {
    }

    @Pointcut("@annotation(Monitored)")
    public void monitored() {

    }

}
