/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.basicTypes;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import MaQiao.MaQiaoSpringExtend.MQUtils;

/**
 * 匹配
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class BasicTypesMatching {

	/**
	 * 得到基础类型的装箱对象，如果不是基础类型则返回null
	 * @param type Type
	 * @return Object
	 */
	public static final Object getBasicTypesDefault(final Type type) {
		if (type == null) return null;
		return BasicTypes.getBasicTypesObject(type.toString());
	}

	/**
	 * 得到基础类型的装箱对象，如果不是基础类型则返回null
	 * @param classzz Class&lt;?>
	 * @return Object
	 */
	public static final Object getBasicTypesDefault(final Class<?> classzz) {
		if (classzz == null) return null;
		return BasicTypes.getBasicTypesObject(classzz.getName());
	}

	/**
	 * 得到基础类型的装箱类型，如果不是基础类型则返回null
	 * @param classzz Class&lt;?>
	 * @return Class&lt;?>
	 */
	public static final Class<?> getBasicTypesClass(final Class<?> classzz) {
		Object obj = BasicTypes.getBasicTypesObject(classzz.getName());
		if (obj == null) return null;
		return obj.getClass();
	}

	/**
	 * 得到基础类型的装箱类型，如果不是基础类型则返回null
	 * @param type Type
	 * @return Class&lt;?>
	 */
	public static final Class<?> getBasicTypesClass(final Type type) {
		Object obj = BasicTypes.getBasicTypesObject(type.toString());
		if (obj == null) return null;
		return obj.getClass();
	}

	/**
	 * 得到基础类型的装箱类型，如果不是基础类型则返回null
	 * @param str String
	 * @return Class&lt;?>
	 */
	public static final Class<?> getBasicTypesClass(final String str) {
		Object obj = BasicTypes.getBasicTypesObject(str);
		if (obj == null) return null;
		return obj.getClass();
	}

	/**
	 * 得到基础类型的装箱类型，如果不是基础类型则返回null
	 * @param parameter Parameter
	 * @return Class&lt;?>
	 */
	public static final Class<?> getBasicTypesClass(final Parameter parameter) {
		return getBasicTypesClass(parameter.getType());
	}

	/**
	 * 得对某个类型的对象<br/>
	 * 或输出newInstance或null
	 * @param classzz Class&lt;?>
	 * @param allowNull boolean
	 * @return Object
	 */
	public static final Object getDefaultObject(final Class<?> classzz, final boolean allowNull) {
		if (classzz == null) return null;
		//System.out.println("classzz:" + classzz.getName());
		/* 基础类型，输出不能为null，直接输出装箱对象 */
		Object obj = getBasicTypesDefault(classzz);
		if (obj != null) return obj;
		if (allowNull) return null;
		try {
			//System.out.println("classzz:" + classzz.getName());
			obj = BasicTypes.getBasicObject(classzz.getName());
			if (obj != null) return obj;
			obj = Class.forName(classzz.getName()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 得对某个类型的对象<br/>
	 * 或输出newInstance
	 * @param classzz Class&lt;?>
	 * @return Object
	 */
	public static final Object getDefaultObject(final Class<?> classzz) {
		return getDefaultObject(classzz, false);
	}

	/**
	 * 得对某个类型的对象<br/>
	 * 如果Type为基础类型，则返回装箱对象<br/>
	 * 或输出newInstance或null
	 * @param type Type
	 * @param classzz Class&lt;?>
	 * @param allowNull boolean
	 * @return Object
	 */
	public static final Object getDefaultObject(final Type type, final Class<?> classzz, final boolean allowNull) {
		Object obj = null;
		/* 基础类型，输出不能为null，直接输出装箱对象 */
		obj = getBasicTypesDefault(type);
		if (obj != null) return obj;
		if (allowNull) {
			return null;
		} else {
			try {
				obj = Class.forName(classzz.getName()).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

	/**
	 * 参数类型进行匹配 允许装箱之前类型与装箱后类型比较<br/>
	 * 允许类与接口的匹配<br/>
	 * class1为主，并允许为接口<br/>
	 * @param class1 Class< ? >
	 * @param class2 Class< ? >
	 * @return boolean
	 */
	public static boolean isMatching(final Parameter parameter, final Class<?> class2) {
		if (parameter == null || class2 == null) return false;
		Class<?> class1 = parameter.getType().getClass();
		System.out.println("装箱前类型before：" + parameter.getType().getName());
		Class<?> classzz = getBasicTypesClass(parameter.getParameterizedType().toString());
		/* 判断是否是装箱前类型 */
		if (classzz != null) {
			System.out.println("装箱前类型not null：" + classzz.getName());
			class1 = classzz;
		} else {
			System.out.println("装箱前类型 is null：" + parameter.getType().getName());
			System.out.println("装箱前类型 is null：" + parameter.getParameterizedType());
		}

		System.out.println("class1：" + class1.getName());
		System.out.println("class2：" + class2.getName());
		if (class1 == null || class2 == null) return false;
		if (class1 == class2) return true;
		if (class1.getName().equals(class2.getName())) return true;
		if (class1.isInterface() && !class2.isInterface() && MQUtils.isInterface(class2, class1)) return true;
		return false;
	}

	/**
	 * 参数类型进行匹配 允许装箱之前类型与装箱后类型比较<br/>
	 * 允许类与接口的匹配<br/>
	 * class1为主，并允许为接口<br/>
	 * @param class1 Class< ? >
	 * @param class2 Class< ? >
	 * @return boolean
	 */
	public static boolean isMatching(final Class<?> class1, final Class<?> class2) {
		if (class1 == null || class2 == null) return false;
		if (class1 == class2) return true;
		if (class1.getName().equals(class2.getName())) return true;
		if (class1.isInterface() && !class2.isInterface() && MQUtils.isInterface(class2, class1)) return true;
		Class<?> classzz = getBasicTypesClass(class1);
		if (classzz != null) if (classzz == class2 || classzz.getName().equals(class2.getName())) return true;
		return false;
	}
}
