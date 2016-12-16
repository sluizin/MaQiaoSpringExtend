/**
 * 
 */
package MaQiaoSpringExtend;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQparameterElement {


	/**
	 * 字符串型入口参数
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class ClassString {
		boolean matching = false;
		boolean order = false;
		List<String> array = new ArrayList<String>();

		public ClassString() {

		}

		/**
		 * 得到关键字数组
		 * @return String[]
		 */
		String[] toKeyArray() {
			return array.toArray(new String[0]);
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("StringClass [Matching=");
			builder.append(matching);
			builder.append(", order=");
			builder.append(order);
			builder.append(", list=");
			builder.append(array);
			builder.append("]");
			return builder.toString();
		}

		public final boolean isMatching() {
			return matching;
		}

		public final void setMatching(boolean matching) {
			this.matching = matching;
		}

		public final boolean isOrder() {
			return order;
		}

		public final void setOrder(boolean order) {
			this.order = order;
		}

		public final List<String> getArray() {
			return array;
		}

		public final void setArray(List<String> list) {
			this.array = list;
		}

	}

	/**
	 * Integer型入口参数
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class ClassInteger {
		boolean order = false;
		int[] Array;
	}

	/**
	 * 系统设置
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class ClassSystem {
		/** 系统允许运行多个对象[同类] */
		boolean allowMulti = false;
		/** 是否忽略大小写 */
		boolean ignoreCase = false;

		public final boolean isAllowMulti() {
			return allowMulti;
		}

		public final void setAllowMulti(boolean allowMulti) {
			this.allowMulti = allowMulti;
		}

		public final boolean isIgnoreCase() {
			return ignoreCase;
		}

		public final void setIgnoreCase(boolean ignoreCase) {
			this.ignoreCase = ignoreCase;
		}

	}

	/**
	 * 参数入口设置<br/>
	 * isMulti<br/>
	 * allowNull<br/>
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class ClassParameter {
		/** 是否允许参数重复 */
		boolean isMulti = false;
		/** 是否允许Null，如果为假则输入Class.newInstance() */
		boolean allowNull = true;

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("ParameterClass [isMulti=");
			builder.append(isMulti);
			builder.append(", allowNull=");
			builder.append(allowNull);
			builder.append("]");
			return builder.toString();
		}

		public final boolean isMulti() {
			return isMulti;
		}

		public final void setMulti(boolean isMulti) {
			this.isMulti = isMulti;
		}

		public final boolean isAllowNull() {
			return allowNull;
		}

		public final void setAllowNull(boolean allowNull) {
			this.allowNull = allowNull;
		}

	}

	/**
	 * 变量与参数对应
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class ClassParameterObject {
		Object obj = null;
		String variable = null;

		public ClassParameterObject() {

		}

		public ClassParameterObject(Object obj) {
			this.obj = obj;
		}

		public ClassParameterObject(Object obj, String variable) {
			this.obj = obj;
			this.variable = variable;
		}
		public ClassParameterObject(String variable,Object obj) {
			this.obj = obj;
			this.variable = variable;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("ParameterObject [obj=");
			builder.append(obj);
			builder.append(", variable=");
			builder.append(variable);
			builder.append("]");
			return builder.toString();
		}

	}
}
