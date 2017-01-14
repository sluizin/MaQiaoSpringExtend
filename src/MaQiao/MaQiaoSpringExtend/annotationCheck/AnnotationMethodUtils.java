/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.annotationCheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 针对@MQExtendMethod注解进行操作
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class AnnotationMethodUtils {

	/**
	 * 判断此方法是否含有指定扩展注解
	 * @param method Method
	 * @return boolean
	 */
	public static final boolean isCorrectMethod(Method method) {
		if (method == null) return false;
		/* 判断 1是否出现扩展注解 2是否出现扩展状态注解失效 3是否出现同级允许注解 4是否出现同级限制注解 */
		return isExtendMethod(method) && (!AnnotationStateUtils.isStateInvalid(method)) && isMethodAllow(method) && (!isMethodfilter(method));
	}

	/**
	 * 判断此方法是否含有指定扩展注解
	 * @param method Method
	 * @return boolean
	 */
	private static boolean isExtendMethod(Method method) {
		return method.isAnnotationPresent(AnnotationConsts.ACC_AnnotationMethod);
	}

	/**
	 * 判断此方法是否含有指定的注解
	 * @param method Method
	 * @return boolean
	 */
	private static boolean isMethodAllow(Method method) {
		for (int i = 0, len = AnnotationConsts.methodAnno_Allow.size(); i < len; i++)
			if (method.isAnnotationPresent(AnnotationConsts.methodAnno_Allow.get(i))) return true;
		return true;
	}

	/**
	 * 判断此方法是否含有过滤的注解
	 * @param method Method
	 * @return boolean
	 */
	private static boolean isMethodfilter(final Method method) {
		for (int i = 0, len = AnnotationConsts.methodAnno_filter.size(); i < len; i++)
			if (method.isAnnotationPresent(AnnotationConsts.methodAnno_filter.get(i))) return true;
		return false;
	}

	/**
	 * 把方法注解转成状态对象，用于检索
	 * @param p Annotation
	 * @return MQExtendStateClass
	 */
	public static final MQExtendMethodClass getMethod(Annotation p) {
		if (p == null) return null;
		if (!AnnotationConsts.ACC_AnnotationMethod.isInstance(p)) return null;
		MQExtendMethodClass e = new MQExtendMethodClass();
		e.value = MQAnnotation.getString(p, "value", "");
		e.explain = MQAnnotation.getString(p, "explain", "");
		e.groupid = MQAnnotation.getInteger(p, "groupid", 0);
		return e;
	}

	/**
	 * 方法注解类
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class MQExtendMethodClass {
		String value = "";
		int groupid = 0;
		String explain = "";

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

	}
}
