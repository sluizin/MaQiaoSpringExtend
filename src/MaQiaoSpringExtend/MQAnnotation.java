/**
 * 
 */
package MaQiaoSpringExtend;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQAnnotation {
	/**
	 * 从注解过得到注解值[String]有默认值，如为null，则返回默认值
	 * @param p Annotation
	 * @param value String
	 * @param defaultValue String
	 * @return String
	 */
	static String getString(Annotation p, String value, String defaultValue) {
		String str = getString(p, value);
		if (str == null) return defaultValue;
		return str;
	}

	/**
	 * 从注解过得到注解值[String]
	 * @param p Annotation
	 * @param value String
	 * @return String
	 */
	static String getString(Annotation p, String value) {
		if (p == null) return null;
		Object obj = getObject(p, value);
		if (obj == null) return null;
		return (String) obj;
	}
	/**
	 * 从注解过得到注解值[Integer]有默认值，如为null，则返回默认值
	 * @param p Annotation
	 * @param value String
	 * @param defaultValue Integer
	 * @return Integer
	 */
	static Integer getInteger(Annotation p, String value, Integer defaultValue) {
		Integer integer = getInteger(p, value);
		if (integer == null) return defaultValue;
		return integer;
	}

	/**
	 * 从注解过得到注解值[Integer]
	 * @param p Annotation
	 * @param value String
	 * @return Integer
	 */
	static Integer getInteger(Annotation p, String value) {
		if (p == null) return null;
		Object obj = getObject(p, value);
		if (obj == null) return null;
		return (Integer) obj;
	}
	/**
	 * 从注解过得到注解值[Boolean]有默认值，如为null，则返回默认值
	 * @param p Annotation
	 * @param value String
	 * @param defaultValue Boolean
	 * @return Boolean
	 */
	static Boolean getBoolean(Annotation p, String value, Boolean defaultValue) {
		Boolean bool = getBoolean(p, value);
		if (bool == null) return defaultValue;
		return bool;
	}

	/**
	 * 从注解过得到注解值[Boolean]
	 * @param p Annotation
	 * @param value String
	 * @return Boolean
	 */
	static Boolean getBoolean(Annotation p, String value) {
		if (p == null) return null;
		Object obj = getObject(p, value);
		if (obj == null) return null;
		return (Boolean) obj;
	}
	/**
	 * 
	 * @param p Annotation
	 * @param value String
	 * @return Object
	 */
	static Object getObject(Annotation p, String value) {
		if (p == null) return null;
		try {
			return p.getClass().getDeclaredMethod(value).invoke(p);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

}
