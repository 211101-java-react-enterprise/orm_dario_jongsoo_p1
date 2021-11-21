package com.revature.banking.orm.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnInORM {

    int Size() default 0;

    String Constraint() default "" ;

    String PRIMARY() default "";

    String UNIQUE() default "";

    String[] ForeignKey() default "";

    String DefaultValue() default "";

    String Check() default "";

}
