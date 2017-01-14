/**
 * 
 */
package MaQiao.MaQiaoSpringExtend;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import MaQiao.MaQiaoSpringExtend.MQparameterElement.ClassParameter;
import MaQiao.MaQiaoSpringExtend.MQparameterElement.ClassParameterObject;
import MaQiao.MaQiaoSpringExtend.MQparameterElement.ClassString;
import MaQiao.MaQiaoSpringExtend.MQparameterElement.ClassSystem;

/**
 * 输入参数<br/>
 * 标识符匹配：<br/>
 * 为false:直接几个标识符直接等于匹配<br/>
 * 为true :使用indexOf对标识符直接筛选<br/>
 * 参数匹配：<br/>
 * 为false:则直接把参数发给参数(多个方法时容易出现错误，除非只访问一个方法，参数确定)<br/>
 * 为true :则不确定参数的数量与类型，(适用于多个方法的访问，参数数量与顺序与类型不同时)<br/>
 * <br/>
 * 匹配规则：关键字[名称] > 顺序 > 类型 > null
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQparameter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ClassString value;
	//StringClass methodName;
	//IntegerClass groupid;
	ClassSystem setupSystem;
	ClassParameter setupParameter;
	// 关键字数组
	//List<String> keyStringList = new ArrayList<String>();
	/** 输入变量对象 */
	List<ClassParameterObject> parameterObjList = new ArrayList<ClassParameterObject>();

	/**
	 * 把输入变量对象列表转成对象数组，用于参数的匹配
	 * @return Object[]
	 */
	Object[] getParameterObjectArray() {
		int len = parameterObjList.size();
		Object[] array = new Object[len];
		for (int i = 0; i < len; i++)
			array[i] = parameterObjList.get(i).obj;
		return array;
	}
	/**
	 * 把输入变量对象和名称转成2维数组[variable][obj]
	 * @return Object[]
	 */
	Object[][] getParameterArray(){
		int len = parameterObjList.size();
		Object[][] paraArray = new Object[len][2];
		for (int i = 0; i < len; i++){
			paraArray[i][0] = parameterObjList.get(i).variable;
			paraArray[i][1] = parameterObjList.get(i).obj;
		}
		return paraArray;
	}

	public static final MQparameter getMQparameterFromHttp(String http) {
		if (!isConnect(http)) return null;
		try {
			return getMQparameterFromString(MQUtils.readFile(new URL(http), "\n", false).toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static MQparameter getMQparameterFromHttp(String http, ClassParameterObject... array) {
		MQparameter e = getMQparameterFromHttp(http);
		if (e == null) return null;
		for (int i = 0, len = array.length; i < len; i++)
			e.addVariableObj(array[i]);
		return e;
	}

	/**
	 * 检测当前URL是否可连接或是否有效,
	 * @param http String
	 * @return boolean
	 */
	private static boolean isConnect(String http) {
		if (http == null || http.length() <= 0) return false;
		try {
			URL url = new URL(http);
			HttpURLConnection con = null;
			for (int i = 0; i < 5; i++) {
				con = (HttpURLConnection) url.openConnection();
				if (con.getResponseCode() == 200) return true;
			}
		} catch (Exception ex) {
			System.out.println("URL不可用");
		}
		return false;
	}

	/**
	 * json 转成 MQparameter 暂无输入变量对象[只能写入java文件中(parameterObjList)]<br/>
	 * 
	 * <pre>
	 * 	{
	 *     "value-": "如果使用这个判断则直接取消前面 - 号",
	 *     "value": {
	 *         "matching-": "是否使用indeof如果为真则使用indexOf进行判断，如果为假则直接对字符串进行比较equals",
	 *         "matching": false,
	 *         "order-": "是否进行排序，如果为真则以Array组进行排序依次进入入口端寻找，如果为假则直接使用入口端List直接寻找",
	 *         "order": false,
	 *         "array-": "具体的扫描对象数组",
	 *         "array": [
	 *             "aa",
	 *             "ee",
	 * 	    "11",
	 *             "cc",
	 * 	    "00",
	 * 	    "AA"
	 *         ]
	 *     },
	 *     "methodName-": "如果使用这个判断则直接取消前面 - 号",
	 *     "-methodName": {
	 *         "matching-": "是否使用indeof如果为真则使用indexOf进行判断，如果为假则直接对字符串进行比较equals",
	 *         "matching": false,
	 *         "order-": "是否进行排序，如果为真则以Array组进行排序依次进入入口端寻找，如果为假则直接使用入口端List直接寻找",
	 *         "order": false,
	 *         "array-": "具体的扫描对象数组",
	 *         "array": [
	 *             "ex",
	 *             "te",
	 *             "na"
	 *         ]
	 *     },
	 *     "groupid-": "如果使用这个判断则直接取消前面 - 号",
	 *     "-groupid": {
	 *         "order-": "是否进行排序，如果为真则以Array组进行排序依次进入入口端寻找，如果为假则直接使用入口端List直接寻找",
	 *         "order": true,
	 *         "array-": "具体的扫描对象数组",
	 *         "array": [
	 *             12,
	 *             54,
	 *             24
	 *         ]
	 *     },
	 *     "setupSystem-": "系统设置",
	 *     "setupSystem": {
	 *         "allowMulti-": "系统允许运行多个对象[同类]",
	 *         "allowMulti": false,
	 * 	"ignoreCase-":"是否忽略大小写",
	 * 	"ignoreCase":false
	 *     },
	 *     "setupParameter-": "参数设置",
	 *     "setupParameter": {
	 *         "isMulti-": "允许一个变量同时给多个参数赋值",
	 *         "isMulti": false,
	 *         "allowNull-": "如果参数只没有找到相应的变量，则允许赋NULL",
	 *         "allowNull": true
	 *     }
	 * }
	 * </pre>
	 * @param json String
	 * @return MQparameter
	 */
	public static final MQparameter getMQparameterFromString(String json) {
		return (MQparameter) JSON.parseObject(json, MQparameter.class);
	}

	/**
	 * 输入变量对象
	 * @param obj Object
	 */
	public void addVariableObj(Object obj) {
		addVariableObj("",obj);
	}

	public MQparameter() {

	}

	/**
	 * 输入变量对象
	 * @param variable String
	 * @param obj Object
	 */
	public void addVariableObj(String variable,Object obj) {
		if (variable != null && variable.length()>0 && obj != null) 
		parameterObjList.add(new ClassParameterObject(variable,obj));
	}

	/**
	 * 输入变量对象
	 * @param e ClassParameterObject
	 */
	public void addVariableObj(ClassParameterObject e) {
		parameterObjList.add(e);
	}

	public Object getVariableObj(String variable) {
		if (variable == null) return null;
		for (int i = 0, len = parameterObjList.size(); i < len; i++) {
			ClassParameterObject e = parameterObjList.get(i);
			if (e.variable.equals(variable)) return e.obj;
		}
		return null;
	}

	/**
	 * 判断参数是否合法，是否有选择条件
	 * @return boolean
	 */
	boolean islegitimate() {
		return (value != null && value.array.size() > 0);
		/*
		 * if (value != null && value.list.size() > 0) {
		 * //from = 0;
		 * return true;
		 * }
		 * if (methodName != null && methodName.Array.length > 0) {
		 * from = 1;
		 * return true;
		 * }
		 * if (groupid != null && groupid.Array.length > 0) {
		 * from = 2;
		 * return true;
		 * }
		 * return false;
		 */
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MQparameter [");
		builder.append("value=");
		builder.append(value);
		builder.append(", parameterSet=");
		builder.append(setupParameter);
		builder.append(", parameterObjList=");
		builder.append(parameterObjList);
		builder.append("]");
		return builder.toString();
	}

	public final String toJson() {
		return JSON.toJSONString(this);
	}
	public final String toJsonZip(){
		String json=this.toJson();
		if(json==null ||json.length()==0)return json;
		return MQUtils.gzip(json);
	}

	public final ClassString getValue() {
		return value;
	}

	public final void setValue(ClassString value) {
		this.value = value;
	}

	public final ClassParameter getSetupParameter() {
		return setupParameter;
	}

	public final void setSetupParameter(ClassParameter setupParameter) {
		this.setupParameter = setupParameter;
	}

	public final List<ClassParameterObject> getParameterObjList() {
		return parameterObjList;
	}

	public final void setParameterObjList(List<ClassParameterObject> parameterObjList) {
		this.parameterObjList = parameterObjList;
	}

	public final ClassSystem getSetupSystem() {
		return setupSystem;
	}

	public final void setSetupSystem(ClassSystem setupSystem) {
		this.setupSystem = setupSystem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((parameterObjList == null) ? 0 : parameterObjList.hashCode());
		result = prime * result + ((setupParameter == null) ? 0 : setupParameter.hashCode());
		result = prime * result + ((setupSystem == null) ? 0 : setupSystem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		MQparameter other = (MQparameter) obj;
		if (!this.toJson().equals(other.toJson())) return false;
		//System.out.println("cache.equals:"+this.tojson());
		return true;
	}

}
