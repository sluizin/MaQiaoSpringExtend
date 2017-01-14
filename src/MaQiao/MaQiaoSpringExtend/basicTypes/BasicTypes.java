/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.basicTypes;

import java.lang.reflect.Type;

/**
 * 基本类型操作
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class BasicTypes {

	@Deprecated
	private static final String[] ACC_BasicTypes = { "byte", "short", "int", "long", "float", "double", "boolean", "char" };

	/**
	 * 判断是否是基础类型
	 * @param classzz Class&lt;?>
	 * @return boolean
	 */
	public static final boolean isBasicTypes(final Class<?> classzz) {
		if (classzz == null) return false;
		return getBasicTypesObject(classzz.getName()) != null;
	}

	/**
	 * 得到基础类型的装箱对象，如果不是基础类型则返回null
	 * @param className String
	 * @return Object
	 */
	public static final Object getBasicTypesObject(final String className) {
		if (className == null) return null;
		if (className.equals("byte")) {
			Byte a = 0;
			return a;
		}
		if (className.equals("short")) {
			Short a = 0;
			return a;
		}
		if (className.equals("int")) {
			Integer a = new Integer(0);
			return a;
		}
		if (className.equals("long")) {
			Long a = new Long(0);
			return a;
		}
		if (className.equals("float")) {
			Float a = new Float(0);
			return a;
		}
		if (className.equals("double")) {
			Double a = new Double(0);
			return a;
		}
		if (className.equals("boolean")) {
			Boolean a = new Boolean(false);
			return a;
		}
		if (className.equals("char")) {
			Character a = new Character(' ');
			return a;
		}
		return null;
	}

	/**
	 * 得到装箱对象，如果不是装箱类型则返回null
	 * @param className String
	 * @return Object
	 */
	public static final Object getBasicObject(final String className) {
		if (className == null) return null;
		if (className.equals("java.lang.Byte")) {
			Byte a = 0;
			return a;
		}
		if (className.equals("java.lang.Short")) {
			Short a = 0;
			return a;
		}
		if (className.equals("java.lang.Integer")) {
			Integer a = new Integer(0);
			return a;
		}
		if (className.equals("java.lang.Long")) {
			Long a = new Long(0);
			return a;
		}
		if (className.equals("java.lang.Float")) {
			Float a = new Float(0);
			return a;
		}
		if (className.equals("java.lang.Double")) {
			Double a = new Double(0);
			return a;
		}
		if (className.equals("java.lang.Boolean")) {
			Boolean a = new Boolean(false);
			return a;
		}
		if (className.equals("java.lang.Character")) {
			Character a = new Character(' ');
			return a;
		}
		return null;
	}

	/**
	 * 判断是否是基础类型
	 * @param type Type
	 * @return boolean
	 */
	@Deprecated
	public static final boolean isBasicTypes(final Type type) {
		if (type == null) return false;
		return isEquals(type.toString(), ACC_BasicTypes);
	}

	/**
	 * 依次比较，如果有一个相同，则返回真
	 * @param key String
	 * @param array String[]
	 * @return boolean
	 */
	private static final boolean isEquals(final String key, String... array) {
		if (key == null || key.length() == 0 || array == null || array.length == 0) return false;
		for (int i = 0, len = array.length; i < len; i++)
			if (key.equals(array[i])) return true;
		return false;
	}

}
