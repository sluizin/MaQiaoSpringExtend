/**
 * 
 */
package MaQiao.MaQiaoSpringExtend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQparameterElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 字符串型入口参数
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class ClassString implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((array == null) ? 0 : array.hashCode());
			result = prime * result + (matching ? 1231 : 1237);
			result = prime * result + (order ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			ClassString other = (ClassString) obj;
			if (array == null) {
				if (other.array != null) return false;
			} else if (!array.equals(other.array)) return false;
			if (matching != other.matching) return false;
			if (order != other.order) return false;
			return true;
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
	public static final class ClassInteger implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		boolean order = false;
		int[] Array;
	}

	/**
	 * 系统设置
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class ClassSystem implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (allowMulti ? 1231 : 1237);
			result = prime * result + (ignoreCase ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			ClassSystem other = (ClassSystem) obj;
			if (allowMulti != other.allowMulti) return false;
			if (ignoreCase != other.ignoreCase) return false;
			return true;
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
	public static final class ClassParameter implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (allowNull ? 1231 : 1237);
			result = prime * result + (isMulti ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			ClassParameter other = (ClassParameter) obj;
			if (allowNull != other.allowNull) return false;
			if (isMulti != other.isMulti) return false;
			return true;
		}

	}

	/**
	 * 变量与参数对应
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class ClassParameterObject implements Serializable {
		private static final long serialVersionUID = 1L;
		Object obj = null;
		/**
		 * 名称[不为null] 
		*/
		String variable = "";

		public ClassParameterObject() {

		}

		public ClassParameterObject(String variable, Object obj) {
			if (variable != null && variable.length()>0) this.variable = variable;
			if (obj != null) this.obj = obj;
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((obj == null) ? 0 : obj.hashCode());
			result = prime * result + ((variable == null) ? 0 : variable.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			ClassParameterObject other = (ClassParameterObject) obj;
			if (this.obj == null) {
				if (other.obj != null) return false;
			} else if (!this.obj.equals(other.obj)) return false;
			if (variable == null) {
				if (other.variable != null) return false;
			} else if (!variable.equals(other.variable)) return false;
			return true;
		}
	}
}
