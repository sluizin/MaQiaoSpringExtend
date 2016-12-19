/**
 * 
 */
package MaQiaoSpringExtend;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.context.annotation.Scope;

/**
 * 本系统关于注解的操作
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQUtilsAnno {
	/**
	 * 判断此类是否含有过滤的注解
	 * @param classzz Class< ? >
	 * @return boolean
	 */
	static boolean isAnnotationfilterClass(final Class<?> classzz) {
		if (classzz == null) return false;
		for (int i = 0, len = MQConsts.classAnnotationFilter.size(); i < len; i++)
			if (classzz.isAnnotationPresent(MQConsts.classAnnotationFilter.get(i))) return true;
		return false;
	}
	/**
	 * 判断此类是否含有指定的注解
	 * @param classzz Class< ? >
	 * @return boolean
	 */
	static boolean isAnnotationAllowClass(Class<?> classzz) {
		if (classzz == null) return false;
		for (int i = 0, len = MQConsts.classAnnotationAllow.size(); i < len; i++)
			if (classzz.isAnnotationPresent(MQConsts.classAnnotationAllow.get(i))) return true;
		/* 
		 * 类注解中，含有@Scope("singleton") 或没有@Scope注解 
		 * 系统只支持单例
		  */
		if (MQUtilsAnno.isAnnotationScopeSingleton(classzz)) return true;
		return false;
	}
	/**
	 * 判断此类是否含有Scope("singleton")<br/>
	 * 默认没有为真，或 Scope("singleton") 为真
	 * @param classzz Class< ? >
	 * @return boolean
	 */
	static boolean isAnnotationScopeSingleton(Class<?> classzz) {
		if (classzz == null) return false;
		Class<? extends Annotation> classScope = Scope.class;
		System.out.println("isScopeSingleton:" + classzz.getName());
		if (classzz.isAnnotationPresent(classScope)) {
			Annotation p = classzz.getAnnotation(classScope);
			if (p == null) return true;
			String objStr = MQAnnotation.getString(p,"value");
			if(objStr==null)return false;
			System.out.println("isScopeSingletonobj:" + objStr.trim());
			if (objStr.trim().equalsIgnoreCase("singleton")) return true;
			return false;
		}
		return true;
	}
	/**
	 * 判断此对象是否含有指定扩展注解
	 * @param obj Object
	 * @return boolean
	 */
	static boolean isMQExtendClass(Object obj) {
		if (obj == null) return false;
		return obj.getClass().isAnnotationPresent(MQConsts.ACC_AnnotationClass);
	}

	/**
	 * 判断此类是否含有指定扩展注解
	 * @param classzz Class< ? >
	 * @return boolean
	 */
	static boolean isMQExtendClass(Class<?> classzz) {
		if (classzz == null) return false;
		return classzz.isAnnotationPresent(MQConsts.ACC_AnnotationClass);
	}
	/**
	 * 判断此方法是否含有指定扩展注解
	 * @param obj Object
	 * @return boolean
	 */
	static boolean isMQExtendMethod(Method method) {
		if (method == null) return false;
		return method.isAnnotationPresent(MQConsts.ACC_AnnotationMethod);
	}
}
