package ws.zettabyte.ferretlib.initutils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraftforge.common.config.Configuration;

/**
 * Must be given a context before parsing.
 * Sets variables annotated with @Conf, in a given class, from a config file, 
 * and recursively does the same to variables annotated @Configgable.
 * @author Samuel "Gyro" Cutlip 
 */
public class ConfAnnotationParser {
	Configuration context;
	public ConfAnnotationParser() {};
	public ConfAnnotationParser(Configuration c) {
		context = c;
	}
	public void setContext(Configuration c) {
		context = c;
	}
	public void parse(Object instance, Class<?> clazz) throws Exception {
		String section = "";
		if(clazz.isAnnotationPresent(Configgable.class)) {
			Configgable confi = clazz.getAnnotation(Configgable.class);
			section = confi.section();
		}
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			//Is this field marked as configgable? Alternatively, is it of a type marked as configgable?
			if ((field.isAnnotationPresent(Configgable.class)) 
					|| (field.getClass().isAnnotationPresent(Configgable.class))) {
				//Recurse:
				this.parse(instance, field.getClass());				
			}
			//Conf annotation.
			if (field.isAnnotationPresent(Conf.class)) {
				Conf conf = field.getAnnotation(Conf.class);
				
				//Default to "Misc", 
				String sectionUse = "Misc";
				if(!section.equals("")) { //Give priority to the Configgable's overriding section value.
					sectionUse = section;
				}
				else {
					sectionUse = conf.section();
				}
				
				//Only accept primitive types. TODO: some sort of list support
				//Also, be on the lookout for copy-paste errors right here.
		        if(field.getType().equals(int.class)) {
					if(conf.comment() == "") {
			        	field.setInt(instance, 
			        			context.get(sectionUse, conf.name(), Integer.parseInt(conf.def()))
			        			.getInt(Integer.parseInt(conf.def())));
					}
					else {
			        	field.setInt(instance, 
			        			context.get(sectionUse, conf.name(), Integer.parseInt(conf.def()), conf.comment())
			        			.getInt(Integer.parseInt(conf.def())));
					}
		        }
		        else if(field.getType().equals(boolean.class)) {
					if(conf.comment() == "") {
			        	field.setBoolean(instance, 
			        			context.get(sectionUse, conf.name(), Boolean.parseBoolean(conf.def()))
			        			.getBoolean(Boolean.parseBoolean(conf.def())));
					}
					else {
			        	field.setBoolean(instance, 
			        			context.get(sectionUse, conf.name(), Boolean.parseBoolean(conf.def()), conf.comment())
			        			.getBoolean(Boolean.parseBoolean(conf.def())));
					}
		        }
		        else if(field.getType().equals(double.class)) {
					if(conf.comment() == "") {
			        	field.setDouble(instance, 
			        			context.get(sectionUse, conf.name(), Double.parseDouble(conf.def()))
			        			.getDouble(Double.parseDouble(conf.def())));
					}
					else {
			        	field.setDouble(instance, 
			        			context.get(sectionUse, conf.name(), Double.parseDouble(conf.def()), conf.comment())
			        			.getDouble(Double.parseDouble(conf.def())));
					}
		        }
		        else if(field.getType().equals(String.class)) {
					if(conf.comment() == "") {
			        	field.set(instance, 
			        			context.get(sectionUse, conf.name(), conf.def())
			        			.getString());
					}
					else {
			        	field.set(instance, 
			        			context.get(sectionUse, conf.name(), conf.def(), conf.comment())
			        			.getString());
					}
		        }
		        else {
		        	//TODO: Error handling for invalid data type
		        }
			}
		}
	}
	public void parse(Class<?> clazz) throws Exception {
		this.parse(null, clazz);
	}
	public void parse(Object instance) throws Exception {
		this.parse(instance, instance.getClass());
	}
}
