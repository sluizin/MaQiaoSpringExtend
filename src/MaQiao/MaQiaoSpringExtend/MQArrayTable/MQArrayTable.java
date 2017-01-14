/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.MQArrayTable;

import java.util.ArrayList;
import java.util.List;

import MaQiao.MaQiaoSpringExtend.MQConsts;

/**
 * 模拟表格通用方法
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQArrayTable {
	/** 设置隔断线字符['\0'为没有隔断线] */
	public static char ACC_usePartitionChar = '-';
	/** 设置换行字符 */
	public static char ACC_newline = '\n';
	/** 设置表格间隔字符 */
	public static char ACC_unitInterval = '|';
	/** 表格填充字符 */
	public static char ACC_fillChar = ' ';

	/** 是否需要把单元字符截断 */
	public static boolean cutOff = true;

	TableClass table = new TableClass();

	/**
	 * 添加一行[含有下线]
	 * @param array Object[]
	 */
	public void append(Object... array) {
		table.append(false, array);
	}

	/**
	 * 添加一行[取消下线]<br/>
	 * @param array Object[]
	 */
	public void appendUnline(Object... array) {
		table.append(true, array);
	}

	/**
	 * 添加一行
	 * @param e LineClass
	 */
	public void append(LineClass e) {
		table.append(e);
	}

	/**
	 * 插入一行
	 * @param index int
	 * @param e LineClass
	 */
	public void insert(int index, LineClass e) {
		table.insert(index, e);
	}

	/**
	 * 输出表格
	 * @return String
	 */
	public String print() {
		return table.print();
	}

	/**
	 * 输出表格[按照指定宽度]
	 * @param widths int[]
	 * @return String
	 */
	public String print(int... widths) {
		return table.print(widths);
	}

	/**
	 * 设置哪些列的对齐方式[Align.left,Align.center,Align.right]统一更改
	 * @param align Align
	 * @param columns int[]
	 */
	public void setAlignColumns(Align align, int... columns) {
		table.setAlign(align, columns);
	}

	/**
	 * 设置哪些行列的值修改对齐方式[Align.left,Align.center,Align.right]逐对修改<br/>
	 * X,Y方式
	 * @param align Align
	 * @param location int[]
	 */
	public void setAlignColumnsLocation(Align align, int... location) {
		table.setAlignColumnsLocation(align, location);

	}

	/**
	 * Table表类
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static class TableClass {

		/** 行列表 */
		List<LineClass> rowList = new ArrayList<LineClass>();

		/**
		 * 添加一行<br/>
		 * cancel:取消下线<br/>
		 * @param cancel boolean
		 * @param array Object[]
		 */
		private void append(boolean cancel, Object... array) {
			if (array.length == 0) return;
			LineClass e = new LineClass(cancel, array);
			append(e);
		}

		/**
		 * 添加一行
		 * @param array Object[]
		 */
		public void append(Object... array) {
			append(false, array);
		}

		/**
		 * 添加一行
		 * @param array Object[]
		 */
		public void appendUnline(Object... array) {
			append(true, array);
		}

		/**
		 * 添加一行
		 * @param e LineClass
		 */
		public void append(LineClass e) {
			if (e != null) rowList.add(e);
		}

		/**
		 * 插入一行
		 * @param index int
		 * @param e LineClass
		 */
		public void insert(int index, LineClass e) {
			if (e == null) return;
			int len = rowList.size();
			if (index >= 0 && index < len) {
				List<LineClass> newRowList = new ArrayList<LineClass>(len + 1);
				if (index > 0) newRowList.addAll(rowList.subList(0, index));
				newRowList.add(e);
				if (index < len) newRowList.addAll(rowList.subList(index, len));
				rowList = newRowList;
			}
			if (index < 0) insert(0, e);
			if (index >= len) rowList.add(e);
		}

		/**
		 * 得到表中有多少行
		 * @return int
		 */
		public int getRowCount() {
			return rowList.size();
		}

		/**
		 * 得到单元格最多的记录行数
		 * @return int
		 */
		@Deprecated
		int getRowCountComplete() {
			int max = getUnitCountMax();
			int counts = 0;
			for (int i = 0, len = rowList.size(); i < len; i++)
				if (rowList.get(i).getCount() == max) counts++;
			return counts;
		}

		/**
		 * 得到表格最多单元格数量
		 * @return int
		 */
		public int getUnitCountMax() {
			int countM = -1;
			for (int i = 0, len = rowList.size(); i < len; i++) {
				int count = rowList.get(i).getCount();
				if (countM == -1) countM = count;
				else if (countM < count) countM = count;
			}
			return countM;
		}

		/**
		 * 得到表的最大宽度，用于隔断线的制作
		 * @return int
		 */
		public int getWidthCountMax() {
			int columnMax = getUnitCountMax();
			int sort = 0;
			for (int i = 0; i < columnMax; i++)
				sort += getLenColumnMax(i);
			if (ACC_unitInterval != '\0') sort += columnMax + 1;
			return sort;
		}

		/**
		 * 得到此表的各个字段的最长值，用于输出
		 * @return int[]
		 */
		public int[] getColumnLenArray() {
			int columnMax = getUnitCountMax();
			int[] result = new int[columnMax];
			for (int i = 0; i < columnMax; i++)
				result[i] = getLenColumnMax(i);
			return result;
		}

		/**
		 * 得到某列的最大长度，用于得到长度数组
		 * @param column int
		 * @return int
		 */
		public int getLenColumnMax(int column) {
			int lenMax = 0;
			int columnMax = getUnitCountMax();
			if (column >= columnMax || column < 0) return 0;
			for (int i = 0, len = rowList.size(); i < len; i++) {
				LineClass e = rowList.get(i);
				if (e.list.size() <= column) continue;
				int unitLen = e.list.get(column).len();
				if (unitLen > lenMax) lenMax = unitLen;
			}
			return lenMax;
		}

		/**
		 * 设置哪些列的对齐方式[Align.left,Align.center,Align.right]统一更改
		 * @param align Align
		 * @param columns int[]
		 */
		public void setAlign(Align align, int... columns) {
			for (int i = 0, len = rowList.size(); i < len; i++) {
				LineClass e = rowList.get(i);
				e.setUnitAlign(align, columns);
			}
		}

		/**
		 * 设置哪些行列的值修改对齐方式[Align.left,Align.center,Align.right]逐对修改<br/>
		 * X,Y方式
		 * @param align Align
		 * @param location int[]
		 */
		public void setAlignColumnsLocation(Align align, int... location) {
			/* 成对出现，用于定位表的位置 */
			for (int i = 0, x, y, len = (location.length - (location.length % 2)); i < len; i += 2) {
				x = location[i];
				y = location[i + 1];
				UnitClass e = getUnit(x, y);
				if (e != null) e.align = align;
			}
		}

		/**
		 * 通过X,Y值找到某个单元
		 * @param x int
		 * @param y int
		 * @return UnitClass
		 */
		private UnitClass getUnit(int x, int y) {
			if (x < 0 || y < 0) return null;
			if (x >= rowList.size()) return null;
			LineClass f = rowList.get(x);
			if (y >= f.getCount()) return null;
			UnitClass e = f.list.get(y);
			return e;
		}

		/**
		 * 输出表格
		 * @return String
		 */
		public String print() {
			if (rowList.size() == 0) return "";
			int[] widths = getColumnLenArray();
			return print(widths);
		}

		public String print(int... widths) {
			if (rowList.size() == 0) return "";
			StringBuilder sb = new StringBuilder(100);
			String partLine = partitionLineExtLine(widths);
			sb.append(partLine);
			for (int i = 0, len = rowList.size(); i < len; i++) {
				LineClass e = rowList.get(i);
				sb.append(e.print(widths));
				sb.append(ACC_newline);
				if (!e.cancel) sb.append(partLine);
			}
			return sb.toString();
		}

		/**
		 * 得到隔断行
		 * @return String
		 */
		@SuppressWarnings("unused")
		private String partitionLine() {
			int len = getWidthCountMax();
			if (ACC_usePartitionChar == '\0') return "";
			char[] newchar = repeatChar(ACC_usePartitionChar, len);
			return new String(newchar);
		}

		/**
		 * 得到隔断行[加入换行符]
		 * @return String
		 */
		@SuppressWarnings("unused")
		private String partitionLineExt() {
			int len = getWidthCountMax();
			char[] line = new char[len + 1];
			if (ACC_usePartitionChar == '\0') return "";
			char[] newchar = repeatChar(ACC_usePartitionChar, len);
			System.arraycopy(newchar, 0, line, 0, len);
			line[len] = ACC_newline;
			return new String(line);
		}

		/**
		 * 得到隔断行[加入换行符]按照数组
		 * @param widths int[]
		 * @return String
		 */
		private String partitionLineExtLine(int... widths) {
			int len = 0;
			for (int i = 0, lenA = widths.length; i < lenA; i++)
				len += (widths[i] + 1);
			if (len == 0) return new String("" + ACC_newline);
			len++;
			char[] line = new char[len + 1];
			if (ACC_usePartitionChar == '\0') return new String("" + ACC_newline);
			char[] newchar = repeatChar(ACC_usePartitionChar, len);
			System.arraycopy(newchar, 0, line, 0, len);
			line[len] = ACC_newline;
			return new String(line);
		}

		/**
		 * 得到某个字符的重复
		 * @param c char
		 * @param len int
		 * @return char[]
		 */
		private char[] repeatChar(final char c, final int len) {
			final char[] line = new char[len];
			if (len == 0) return line;
			for (int i = 0; i < len; i++)
				line[i] = c;
			return line;
		}
	}

	/**
	 * Table行
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static class LineClass {

		/** 设置一行中的各个单元格 */
		List<UnitClass> list = new ArrayList<UnitClass>();
		/** 是否取消下线 */
		boolean cancel = false;

		public LineClass(boolean cancel, Object... array) {
			this.cancel = cancel;
			for (int i = 0, len = array.length; i < len; i++)
				if (array[i] != null) list.add(new UnitClass(array[i]));
		}

		/**
		 * 得到一行有多少单元
		 * @return int
		 */
		int getCount() {
			return list.size();
		}

		/**
		 * 此行的长度，含前后间隔
		 * @return int
		 */
		int width() {
			int count = len();
			if (ACC_unitInterval != '\0') count += list.size() + 1;
			return count;
		}

		/**
		 * 此行的长度，不含间隔
		 * @return int
		 */
		int len() {
			int count = 0;
			for (int i = 0, len = list.size(); i < len; i++)
				count += list.get(i).len();
			return count;
		}

		/**
		 * 设置某列的对齐方式
		 * @param align Align
		 * @param columns int[]
		 */
		void setUnitAlign(Align align, int... columns) {
			for (int i = 0, len = columns.length; i < len; i++)
				if (columns[i] > -1 && columns[i] < list.size()) list.get(columns[i]).align = align;
		}

		/**
		 * 输出<br/>
		 * @param lineWidthArray int[]
		 * @return String
		 */
		public String print(int... lineWidthArray) {
			StringBuilder sb = new StringBuilder(50);
			int inputLen = lineWidthArray.length;
			int len = list.size();
			for (int i = 0; i < len; i++) {
				if (i == 0) sb.append(ACC_unitInterval);
				UnitClass e = list.get(i);
				int width = e.len();
				if (i < inputLen && (cutOff && width > lineWidthArray[i] || width < lineWidthArray[i])) width = lineWidthArray[i];
				if (i == len - 1 && i < inputLen) {
					for (width = 0; i < inputLen; i++)
						width += lineWidthArray[i] + unitIntervalLen();
					width -= unitIntervalLen();
				}
				sb.append(e.print(width));
				sb.append(ACC_unitInterval);
			}
			return sb.toString();
		}
	}

	/**
	 * 表格之间间隔长度
	 * @return int
	 */
	private static final int unitIntervalLen() {
		if (ACC_unitInterval != '\0') return 1;
		return 0;
	}

	/**
	 * Table单元
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	static class UnitClass {
		/** 内容值 */
		Object value = null;
		/** 对齐方式 */
		Align align = Align.left;

		UnitClass(Object value) {
			this.value = value;
		}

		UnitClass(Object value, Align align) {
			this.value = value;
			this.align = align;
		}

		String valueToString() {
			return value.toString();
		}

		/**
		 * 得到这个单元长度 使用.toString().length()
		 * @return int
		 */
		int len() {
			if (value == null) return -1;
			return valueToString().length();
		}

		/**
		 * 输出
		 * @param width int
		 * @return String
		 */
		String print(int width) {
			String valueToString = valueToString();
			if (width <= 0) return "";
			int len = len();
			if (len >= width) return valueToString.substring(0, width);
			char[] newArray = new char[width];
			/* 先填充数组 */
			for (int i = 0; i < width; i++)
				newArray[i] = ACC_fillChar;
			Object obj = MQConsts.UNSAFE.getObject(valueToString, MQConsts.StringArrayOffset);
			if (align == Align.center) System.arraycopy(obj, 0, newArray, (width - len) / 2, len);
			else if (align == Align.right) System.arraycopy(obj, 0, newArray, width - len, len);
			else System.arraycopy(obj, 0, newArray, 0, len);
			return new String(newArray);
		}
	}

	/**
	 * 对齐方式
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static enum Align {
		left, center, right
	}

	public final TableClass getTable() {
		return table;
	}

	public final void setTable(TableClass table) {
		this.table = table;
	}
	
}
