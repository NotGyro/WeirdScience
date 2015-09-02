package ws.zettabyte.ferretlib.initutils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

//Used on classes whose fields may have @Conf on them. Recursion, here, is a thing.
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Configgable {
	/*
	 * Overrides the section in an @Conf contained within
	 */
	String section() default "";
}
