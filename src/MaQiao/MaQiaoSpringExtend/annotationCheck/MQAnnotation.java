/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.annotationCheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * 注解获取通用
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQAnnotation {
	/**
	 * 从注解过得到注解值[String]有默认值，如为null，则返回默认值[数组]
	 * @param p Annotation
	 * @param value String
	 * @param defaultValue String[]
	 * @return String[]
	 */
	static final String[] getStringArrays(Annotation p, final String value, final String[] defaultValue) {
		String[] str = getStringArrays(p, value);
		if (str == null) return defaultValue;
		return str;
	}

	/**
	 * 从注解过得到注解值[String[]]
	 * @param p Annotation
	 * @param value String
	 * @return String[]
	 */
	static final String[] getStringArrays(final Annotation p, final String value) {
		if (p == null) return null;
		Object obj = getObject(p, value);
		if (obj == null) return null;
		if (obj.getClass().isArray()) return (String[]) obj;
		return null;
	}

	/**
	 * 从注解过得到注解值[String]有默认值，如为null，则返回默认值
	 * @param p Annotation
	 * @param value String
	 * @param defaultValue String
	 * @return String
	 */
	static final String getString(Annotation p, final String value, final String defaultValue) {
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
	static final String getString(final Annotation p, final String value) {
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
	static final Integer getInteger(final Annotation p, final String value, final Integer defaultValue) {
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
	static final Integer getInteger(final Annotation p, final String value) {
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
	static final Boolean getBoolean(final Annotation p, final String value, final Boolean defaultValue) {
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
	static final Boolean getBoolean(final Annotation p, final String value) {
		if (p == null) return null;
		Object obj = getObject(p, value);
		if (obj == null) return null;
		return (Boolean) obj;
	}

	/**
	 * 从注解对象中得到注解值
	 * @param p Annotation
	 * @param value String
	 * @return Object
	 */
	private static final Object getObject(final Annotation p, final String value) {
		if (p == null || value == null || value.equals("")) return null;
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
