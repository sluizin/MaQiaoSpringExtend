/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.annotationCheck;

import java.lang.annotation.Annotation;

import org.springframework.context.annotation.Scope;

import MaQiao.MaQiaoSpringExtend.extendBean.InterfaceMQExtendBean;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class AnnotationClassUtils {
	/**
	 * 判断对象是否进入扩展
	 * @param Obj Object
	 * @return boolean
	 */
	public static final boolean isCorrectClass(Object obj) {
		if (obj == null) return false;
		return isCorrectClass(obj.getClass());
	}

	/**
	 * 判断类是否进入扩展
	 * @param classzz Class&lt;?>
	 * @return boolean
	 */
	public static final boolean isCorrectClass(Class<?> classzz) {
		if (classzz == null) return false;
		/* 判断 1是否出现扩展注解 2是否出现扩展状态注解失效 3是否出现同级允许注解 4是否出现同级限制注解 5是否是单例 */
		return isExtendClass(classzz) && (!AnnotationStateUtils.isStateInvalid(classzz)) && isClassAllow(classzz) && (!isClassfilter(classzz)) && isScopeSingleton(classzz);
	}

	/**
	 * 判断此类是否含有指定扩展注解[看有没有从InterfaceMQExtendBean接口]
	 * @param classzz Class< ? >
	 * @return boolean
	 */
	private static boolean isExtendClass(Class<?> classzz) {
		/* 先判断是否此类有注解[可以通过继承获取] */
		if(classzz.isAnnotationPresent(AnnotationConsts.ACC_AnnotationClass))return true;
		/* 判断此类是否有相应的接口 */
		return classzz.isAssignableFrom(InterfaceMQExtendBean.class);
	}

	/**
	 * 判断此类是否含有指定的注解
	 * @param classzz Class< ? >
	 * @return boolean
	 */
	private static boolean isClassAllow(Class<?> classzz) {
		for (int i = 0, len = AnnotationConsts.classAnno_Allow.size(); i < len; i++)
			if (classzz.isAnnotationPresent(AnnotationConsts.classAnno_Allow.get(i))) return true;
		return true;
	}

	/**
	 * 判断此类是否含有过滤的注解
	 * @param classzz Class< ? >
	 * @return boolean
	 */
	private static boolean isClassfilter(final Class<?> classzz) {
		for (int i = 0, len = AnnotationConsts.classAnno_filter.size(); i < len; i++)
			if (classzz.isAnnotationPresent(AnnotationConsts.classAnno_filter.get(i))) return true;
		return false;
	}

	/**
	 * 判断此类是否含有Scope("singleton")<br/>
	 * 默认没有为真，或 Scope("singleton") 为真
	 * @param classzz Class< ? >
	 * @return boolean
	 */
	private static boolean isScopeSingleton(Class<?> classzz) {
		Class<? extends Annotation> classScope = Scope.class;
		//System.out.println("isScopeSingleton:" + classzz.getName());
		if (classzz.isAnnotationPresent(classScope)) {
			Annotation p = classzz.getAnnotation(classScope);
			if (p == null) return true;
			String objStr = MQAnnotation.getString(p, "value");
			if (objStr == null) return false;
			//System.out.println("isScopeSingletonobj:" + objStr.trim());
			if (objStr.trim().equalsIgnoreCase("singleton")) return true;
			return false;
		}
		return true;
	}

	/**
	 * 把方法注解转成状态对象，用于检索
	 * @param p Annotation
	 * @return MQExtendStateClass
	 */
	public static final MQExtendClassClass getClass(Annotation p) {
		if (p == null) return null;
		if (!AnnotationConsts.ACC_AnnotationClass.isInstance(p)) return null;
		MQExtendClassClass e = new MQExtendClassClass();
		e.value = MQAnnotation.getString(p, "value", "");
		e.explain = MQAnnotation.getString(p, "explain", "");
		e.groupid = MQAnnotation.getInteger(p, "groupid", 0);
		e.value = MQAnnotation.getString(p, "value", "");
		return e;
	}

	/**
	 * 类注解类
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class MQExtendClassClass {
		String value = "";
		int groupid = 0;
		String explain = "";
		String version = "";

		public final String getValue() {
			return value;
		}

		public final void setValue(String value) {
			this.value = value;
		}

		public final int getGroupid() {
			return groupid;
		}

		public final void setGroupid(int groupid) {
			this.groupid = groupid;
		}

		public final String getExplain() {
			return explain;
		}

		public final void setExplain(String explain) {
			this.explain = explain;
		}

		public final String getVersion() {
			return version;
		}

		public final void setVersion(String version) {
			this.version = version;
		}

	}
}
