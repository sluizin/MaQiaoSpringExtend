/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import MaQiao.MaQiaoSpringExtend.MQArrayTable.MQArrayTable.TableClass;

/**
 * 控制流程Map值是否有变化
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MonitorMap {
	/** 记录更改表 */
	Map<String, String> map = new HashMap<String, String>();
	List<resultClass> result = new ArrayList<resultClass>();
	/* 输出记录 */
	TableClass table=null;

	private void update(Map<Object, Object> map) {
		this.map.clear();
		if (map == null) return;
		Iterator<Entry<Object, Object>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<Object, Object> entry = entries.next();
			Object key = entry.getKey();
			if (key == null) continue;
			Object value = entry.getValue();
			String json = JSON.toJSONString(value);
			this.map.put(key.toString(), json);
		}
	}

	public void start(Map<Object, Object> map) {
		if (map == null) return;
		update(map);
	}

	/**
	 * 检索map更改内容
	 * @param map Map&lt;Object, Object>
	 */
	void check(Map<Object, Object> map) {
		if (map == null) return;
		resultClass result = new resultClass();
		Iterator<Entry<Object, Object>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<Object, Object> entry = entries.next();
			Object key = entry.getKey();
			if (key == null) continue;
			String keys = (String) key;
			if (keys.length() == 0) continue;
			Object value = entry.getValue();
			String newJson = JSON.toJSONString(value);
			if (this.map.containsKey(keys)) {
				//发现相同key
				String oldJson = this.map.get(keys);
				if (!oldJson.equals(newJson)) {
					/* 修改 */
					result.put(keys, oldJson, newJson);
				} else {
					/* 正常 */
					result.put(keys, oldJson, oldJson);
				}
			} else {
				//没有发现key的，添加
				result.put(keys, "", newJson);
			}
		}
		Iterator<Entry<String, String>> entriesOld = this.map.entrySet().iterator();
		while (entriesOld.hasNext()) {
			Entry<String, String> entry = entriesOld.next();
			String key = entry.getKey();
			if (key == null || key.length() == 0) continue;
			if (map.containsKey(key)) continue;
			String oldJson = this.map.get(key);
			//删除
			//System.out.println("删除:" + key);
			result.put(key, oldJson, "");
		}
		this.result.add(result);
		//System.out.println("result:" + this.result.size());
		update(map);
	}
	public void check(int i,Map<Object, Object> map) {
		regHead(i);
		check(map);
	}
	void regHead(int i){
		table.appendUnline("step:" + i);
		table.append("Record(Map):state","keyword", "oldValue", "newValue");
	}
	@Deprecated
	void print() {
		/*
		 * final String line = "-----------------------------------------------------------------------------------------";
		 * for (int i = 0, len = result.size(); i < len; i++) {
		 * resultClass e = result.get(i);
		 * System.out.println(line);
		 * List<resultUnitClass> resultList = e.resultList;
		 * System.out.println(String.format("|步骤:" + i + "%"+(resultUnitClass.sizeSort()+10)+"s", "|"));
		 * System.out.println(line);
		 * String str = String.format("|记录(Map):状态|%-" + resultUnitClass.size[1] + "s|%-" + resultUnitClass.size[2] + "s|%-" + resultUnitClass.size[3] + "s|",
		 * "keyword", "oldValue", "newValue");
		 * System.out.println(str);
		 * System.out.println(line);
		 * for (int ii = 0, len2 = resultList.size(); ii < len2; ii++) {
		 * resultUnitClass f = resultList.get(ii);
		 * System.out.println(f.print());
		 * }
		 * System.out.println(line);
		 * }
		 */
		System.out.println(printString());
	}
	
	@Deprecated
	String printString() {
		StringBuilder sb = new StringBuilder(2000);
		final String line = "---------------------------------------------------------------------------------------------";
		for (int i = 0, len = result.size(); i < len; i++) {
			resultClass e = result.get(i);
			List<resultUnitClass> resultList = e.resultList;
			sb.append(String.format("|step:" + i + "%" + (resultUnitClass.sizeSort() + 8) + "s", "|"));
			sb.append('\n');
			sb.append(line);
			sb.append('\n');
			String str = String.format("|Record(Map):state |%-" + resultUnitClass.size[1] + "s|%-" + resultUnitClass.size[2] + "s|%-" + resultUnitClass.size[3] + "s|", "keyword", "oldValue", "newValue");
			sb.append(str);
			sb.append('\n');
			sb.append(line);
			sb.append('\n');
			for (int ii = 0, len2 = resultList.size(); ii < len2; ii++) {
				resultUnitClass f = resultList.get(ii);
				sb.append(f.print());
				sb.append('\n');
			}
			sb.append(line);
			sb.append('\n');
		}
		return sb.toString();
	}
	@Deprecated
	public void putArrayTable(TableClass table){
		for (int i = 0, len = result.size(); i < len; i++) {
			resultClass e = result.get(i);
			List<resultUnitClass> resultList = e.resultList;
			table.appendUnline("step:" + i);
			table.append("Record(Map):state","keyword", "oldValue", "newValue");
			for (int ii = 0, len2 = resultList.size(); ii < len2; ii++) {
				resultUnitClass f = resultList.get(ii);
				table.append(f.toArrayObject());
			}
		}
		
		
	}

	String[][] toArray() {
		int len = result.size();
		if (len == 0) return new String[0][];
		int sort = 0;
		for (int i = 0; i < len; i++) {
			sort += 2;
			resultClass e = result.get(i);
			List<resultUnitClass> resultList = e.resultList;
			sort += resultList.size();

		}
		String[][] array = new String[sort][4];
		return array;
	}

	/**
	 * 更新修改删除报告<br/>
	 * 报告列表
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	class resultClass {
		List<resultUnitClass> resultList = new ArrayList<resultUnitClass>();

		/**
		 * 添加监控单元
		 * @param arrays String[]
		 */
		void put(String... arrays) {
			resultUnitClass unit = new resultUnitClass(arrays);
			if (unit.state() == -1) return;
			if (resultList.contains(unit)) return;
			//System.out.println(unit.print());
			resultList.add(unit);
			table.append(unit.toArrayObject());
		}
	}

	/**
	 * 更新修改删除报告<br/>
	 * 格式:"key","oldValue","newValue"<br/>
	 * 例:"key","oldValue", "oldValue" ==> normal 0<br/>
	 * 例:"key","", "newValue" ==> append 1<br/>
	 * 例:"key","oldValue","newValue" ==> modify 2<br/>
	 * 例:"key","oldValue","" ==> delete 3<br/>
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	static class resultUnitClass {

		final static int[] size = { 8, 20, 25, 25 };
		/** {"key", "oldJson", "newJson"} */
		final String[] result = { "key", "", "" };

		/**
		 * 禁止空构造函数
		 */
		@SuppressWarnings("unused")
		private resultUnitClass() {

		}

		/**
		 * 构造函数
		 * @param array String[]
		 */
		resultUnitClass(String... array) {
			if (array.length == 0 || array.length > 3 || array[0] == null || array[0].length() == 0) return;
			for (int i = 0, len = array.length; i < len; i++)
				result[i] = array[i];
		}

		/**
		 * 整理
		 */
		private void split() {
			for (int i = 0; i < 3; i++)
				result[i] = (result[i] != null) ? result[i].trim() : "";
		}

		/**
		 * 状态:<br/>
		 * -1:mistake<br/>
		 * 0:normal<br/>
		 * 1:append<br/>
		 * 2:modify<br/>
		 * 3:delete<br/>
		 * @return int
		 */
		int state() {
			split();
			if (result.length == 0 || result.length > 3 || result[0].length() == 0) return -1;
			return checkState(result[1], result[2]);
		}

		private int checkState(String str1, String str2) {
			if (str1.length() > 0) if (str2.length() > 0) if (str1.equals(str2)) return 0;
			else return 2;
			else return 3;
			else if (str2.length() > 0) return 1;
			else return -1;
		}

		/**
		 * 返回状态中文{"normal","append","modify","delete","mistake"}
		 * @return String
		 */
		String stateString() {
			int state = state();
			if (state == 0) return "normal";
			if (state == 1) return "append";
			if (state == 2) return "modify";
			if (state == 3) return "delete";
			return "mistake";
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			resultUnitClass other = (resultUnitClass) obj;
			if (result.length != other.result.length) return false;
			if (!result[0].equals(other.result[0])) return false;
			return true;
		}

		/**
		 * 输出print
		 * @return String
		 */
		String print() {
			String str = String.format("|Record(Map):%-2s|%-" + size[1] + "s|%-" + size[2] + "s|%-" + size[3] + "s|", stateString(), result[0], result[1], result[2]);
			return str;
			//return "记录(Map):" + stateString() + "\tkey[" + result[0] + "]\t" + result[1] + "\t" + result[2];
		}

		String[] toArray() {
			String[] array = { "Record(Map):" + stateString(), result[0], result[1], result[2] };
			return array;
		}
		Object[] toArrayObject() {
			Object[] array = { "Record(Map):" + stateString(), result[0], result[1], result[2] };
			return array;
		}

		/**
		 * 各单元总长度
		 * @return int
		 */
		static int sizeSort() {
			int sort = 0;
			for (int i = 0, len = size.length; i < len; i++)
				sort += size[i];
			return sort;
		}

	}

	public final TableClass getTable() {
		return table;
	}

	public final void setTable(TableClass table) {
		this.table = table;
	}
	
}
