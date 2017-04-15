package pl.lodz.p.edu.grs.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Monitored {
}
