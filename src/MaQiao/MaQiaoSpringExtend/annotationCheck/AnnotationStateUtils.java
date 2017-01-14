/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.annotationCheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 针对@MQExtendState注解进行操作
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class AnnotationStateUtils {
	/**
	 * 判断此注解属性是否失效[MQExtenState]<br/>
	 * 当invalid:true，则失效<br/>
	 * 当日期在start-end日期之间，则有效，反之无效，格式不正确时失效<br/>
	 * 默认为false<br/>
	 * @param classzz Class&lt;?>
	 * @return boolean
	 */
	public static final boolean isStateInvalid(Class<?> classzz) {
		if (classzz == null) return false;
		if (classzz.isAnnotationPresent(AnnotationConsts.ACC_AnnotationState)) {
			Annotation p = classzz.getAnnotation(AnnotationConsts.ACC_AnnotationState);
			return isStateInvalid(p);
		}
		return false;
	}

	/**
	 * 判断此注解属性是否失效[MQExtenState]<br/>
	 * 当invalid:true，则失效<br/>
	 * 当日期在start-end日期之间，则有效，反之无效，格式不正确时失效<br/>
	 * 默认为false<br/>
	 * @param method Method
	 * @return boolean
	 */
	public static final boolean isStateInvalid(Method method) {
		if (method == null) return false;
		if (method.isAnnotationPresent(AnnotationConsts.ACC_AnnotationState)) {
			Annotation p = method.getAnnotation(AnnotationConsts.ACC_AnnotationState);
			return isStateInvalid(p);
		}
		return false;
	}

	/**
	 * 判断此注解属性是否失效[MQExtenState]<br/>
	 * 当invalid:true，则失效<br/>
	 * 当日期在start-end日期之间，则有效，反之无效，两个格式都不正确时失效<br/>
	 * @param p Annotation
	 * @return boolean
	 */
	public static final boolean isStateInvalid(Annotation p) {
		if (p == null) return false;
		if (!AnnotationConsts.ACC_AnnotationState.isInstance(p)) return false;
		boolean invalid = MQAnnotation.getBoolean(p, "invalid", false);
		if (invalid) return true;
		String start = MQAnnotation.getString(p, "start");
		String end = MQAnnotation.getString(p, "end");
		String[] environment = MQAnnotation.getStringArrays(p, "environment");
		if (environment != null && environment.length > 0) {
			String env = System.getProperty("spring.profiles.active");
			//System.out.println("sjenv:"+env);
			for (int i = 0; i < environment.length; i++)
				if (env.equalsIgnoreCase(environment[i])) return false;
			return true;
		}
		return isNowBetweenState(start, end);
	}

	/**
	 * 暂时不用，因为有个顺序判断，所以建议使用不过期的那个方法<br/>
	 * 判断此注解属性是否失效[MQExtenState]<br/>
	 * 当invalid:true，则失效<br/>
	 * 当日期在start-end日期之间，则有效，反之无效，格式不正确时失效<br/>
	 * @param p Annotation
	 * @return boolean
	 */
	@Deprecated
	public static final boolean isStateInvalidDeprecated(Annotation p) {
		if (p == null) return false;
		MQExtendStateClass e = getState(p);
		if (e == null) return false;
		if (e.invalid) return true;
		return isNowBetweenIn(e.start, e.end);
	}

	/**
	 * 把状态注解转成状态对象，用于检索
	 * @param p Annotation
	 * @return MQExtendStateClass
	 */
	public static final MQExtendStateClass getState(Annotation p) {
		if (p == null) return null;
		if (!AnnotationConsts.ACC_AnnotationState.isInstance(p)) return null;
		MQExtendStateClass e = new MQExtendStateClass();
		e.author = MQAnnotation.getString(p, "author", "");
		e.invalid = MQAnnotation.getBoolean(p, "invalid", false);
		e.start = MQAnnotation.getString(p, "start", "");
		e.end = MQAnnotation.getString(p, "end", "");
		return e;
	}

	/**
	 * 注解状态类
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class MQExtendStateClass {
		String author = "";
		boolean invalid = false;
		String start = "";
		String end = "";

		public final String getAuthor() {
			return author;
		}

		public final void setAuthor(String author) {
			this.author = author;
		}

		public final boolean isInvalid() {
			return invalid;
		}

		public final void setInvalid(boolean invalid) {
			this.invalid = invalid;
		}

		public final String getStart() {
			return start;
		}

		public final void setStart(String start) {
			this.start = start;
		}

		public final String getEnd() {
			return end;
		}

		public final void setEnd(String end) {
			this.end = end;
		}

	}

	/**
	 * 判断当前日期是否在后面两个日期的之间。精确到日<br/>
	 * 格式："yyyy-MM-dd"<br/>
	 * 如果格式不正确则返回false<br/>
	 * @param date1 String
	 * @param date2 String
	 * @return boolean
	 */
	@Deprecated
	private static final boolean isNowBetweenIn(String date1, String date2) {
		return isDateBetweenIn((new Date()).getTime(), date1, date2);
	}

	/**
	 * 判断此日期是否在后面两个日期的之间。精确到日<br/>
	 * 格式："yyyy-MM-dd"<br/>
	 * 如果格式不正确则返回false<br/>
	 * @param longTime long
	 * @param date1 String
	 * @param date2 String
	 * @return boolean
	 */
	@Deprecated
	private static final boolean isDateBetweenIn(long longTime, String date1, String date2) {
		if (date1 == null || date1.length() == 0 || date2 == null || date2.length() == 0) return false;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			//Date dt1 = df.parse(date1);
			//Date dt2 = df.parse(date2);
			return (df.parse(date1).getTime() <= longTime && longTime <= df.parse(date2).getTime());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断状态与前后两个日期的关系。精确到日<br/>
	 * 1:"2014-01-01","" :当前日期在2014-01-01之后则返回true<br/>
	 * 2:"2014-01-01","2014-02-01" :当前日期在2014-01-01与2014-02-01之间则返回true<br/>
	 * 3:"","2014-02-01" :当前日期在2014-02-01之前则返回true<br/>
	 * 4:"","" :日期格式不正确或2个日期同时为空则返回false<br/>
	 * 格式：["yyyy-MM-dd"]<br/>
	 * @param date1 String
	 * @param date2 String
	 * @return boolean
	 */
	private static final boolean isNowBetweenState(final String date1, final String date2) {
		long long1 = getDateLong(date1);
		long long2 = getDateLong(date2);
		if (long1 > 0L || long2 > 0L) {
			long thisTime = (new Date()).getTime();
			if (long1 > 0L) {
				if (long2 > 0L) return (long1 <= thisTime && thisTime <= long2);
				else return long1 <= thisTime;
			} else if (long2 > 0L) return thisTime <= long2;
		}
		return false;
	}

	/**
	 * 得到日期型字符串的日期型Long，格式不正确返回0L<br/>
	 * 格式：["yyyy-MM-dd"]
	 * @param dateTime String
	 * @return long
	 */
	private static final long getDateLong(String dateTime) {
		if (dateTime == null || dateTime.length() == 0) return 0L;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return df.parse(dateTime).getTime();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0L;
	}
}
