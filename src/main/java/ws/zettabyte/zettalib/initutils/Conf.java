package ws.zettabyte.zettalib.initutils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation parsed to set static fields from values in a config file.
 * Valid datatypes are int, float, String, boolean
 * @author Samuel "Gyro" Cutlip 
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Conf {
	String name();
	/**
	 * Shorthand for default, as in default value.
	 */
	String def() default "0";
	String section() default "";
	String comment() default "";
}