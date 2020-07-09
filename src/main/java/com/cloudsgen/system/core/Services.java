package com.cloudsgen.system.core;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD })
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Services {
    public String Name() default "";


}
