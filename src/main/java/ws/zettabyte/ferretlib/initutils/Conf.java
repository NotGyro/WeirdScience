package ws.zettabyte.ferretlib.initutils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Conf {
	String name();
	/*
	 * Shorthand for default, as in default value.
	 */
	String def() default "0";
	String section() default "";
	String comment() default "";
}