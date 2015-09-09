package ws.zettabyte.zettalib.initutils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be parsed to set fields on a class or instance 
 * from values in a configuration file.
 * Presently, valid datatypes are int, float, String, and boolean
 * @author Sam "Gyro" Cutlip 
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