package com.revature.banking.orm.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceORM {
    String TableName();

    String Schema();
}
