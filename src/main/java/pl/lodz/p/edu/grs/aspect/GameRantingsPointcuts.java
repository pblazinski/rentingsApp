package pl.lodz.p.edu.grs.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class GameRantingsPointcuts {

    @Pointcut("execution(* *..controller..*(..))")
    public void asyncControllerMethod() {
    }


    @Pointcut("bean(*Controller)")
    public void asyncBeanController() {
    }

    @Pointcut("@annotation(Monitored)")
    public void monitor() {

    }

    @Pointcut("execution(* *..repository..*())")
    public void asynchCacheRepository() {

    }

}
