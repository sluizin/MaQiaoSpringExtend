/**
 * 
 */
package MaQiao.MaQiaoSpringExtend;

import java.util.ArrayList;
import java.util.List;

import MaQiao.MaQiaoSpringExtend.Element.ElementMethod;
import MaQiao.MaQiaoSpringExtend.MQArrayTable.MQArrayTable;
import MaQiao.MaQiaoSpringExtend.monitor.MonitorMap;
import MaQiao.MaQiaoSpringExtend.parameter.AbstractMQparameter;
import MaQiao.MaQiaoSpringExtend.parameter.ParameterMapmodel;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQRUN {
	/**
	 * 注册对象
	 * @param obj Object
	 */
	public static final void register(final Object obj){
		MQApplicationcontext.register(obj);
	}
	/**
	 * 注册类
	 * @param classzz Class&lt;?>
	 */
	public static final void register(final Class<?> classzz){
		MQApplicationcontext.register(classzz);
	}
	/**
	 * 移除类
	 * @param classzz Class&lt;?>
	 */
	public static final void unregister(final Class<?> classzz){
		MQApplicationcontext.unregister(classzz);
	}
	/**
	 * 移除对象
	 * @param obj Object
	 */
	public static final void unregister(final Object obj){
		MQApplicationcontext.unregister(obj);
	}
	/**
	 * 静态运行参数
	 * @param absMQparameter AbstractMQparameter
	 */
	public static final void staticRun(AbstractMQparameter absMQparameter) {
		privateRun(absMQparameter);
	}

	/**
	 * 静态运行参数<br/>
	 * 建立参数，默认含有Map&lt;String, Object> model元素
	 * @param jsonFile String
	 */
	public static final void staticRunMapmodel(String jsonFile) {
		ParameterMapmodel f = new ParameterMapmodel(jsonFile);
		staticRun(f);
	}

	/**
	 * 静态运行参数
	 * @param absMQparameter AbstractMQparameter
	 */
	private static final void privateRun(AbstractMQparameter absMQparameter) {
		if (absMQparameter.getParameter() == null || !absMQparameter.getParameter().islegitimate()) {
			//System.out.println("Error:未进入方法检索[没有条件]");
			new Exception("Error:未进入方法检索[没有条件]");
			return;
		}
		if (!isThreadSafe()) {
			new Exception("Error:对本线程的堆栈跟踪，发现此调用方法进入过容器基础表");
			return;
		}
		/* 按注解标识运行 */
		{
			runByValue(absMQparameter);
		}
	}

	/**
	 * 运行通过value查到的记录
	 * @param absMQparameter AbstractMQparameter
	 */
	static final void runByValue(AbstractMQparameter absMQparameter) {
		List<ElementMethod> commondList = getListByValue(absMQparameter.getParameter());
		if (commondList != null && commondList.size() > 0) {
			for(int i=0;i<commondList.size();i++){
				System.out.println("SelectCommondList["+i+"]:"+commondList.get(i).toString());
			}
			commondListRun(commondList, absMQparameter);
		}
	}

	/**
	 * 得到list值，或从缓存中提
	 * @param parameter MQparameter
	 * @return List &lt;ElementMethod>
	 */
	private static final List<ElementMethod> getListByValue(MQparameter parameter) {
		List<ElementMethod> commondList = null;
		if (MQConsts.ACC_useCache) commondList = MQContainer.cacheParam.getList(parameter);
		if (commondList == null) {
			commondList = getElementMethodListByValue(parameter);
			if (MQConsts.ACC_useCache && commondList != null && commondList.size() > 0) MQContainer.cacheParam.put(parameter, commondList);
		}
		return commondList;
	}

	/**
	 * 运行List&lt;ElementMethod&gt; MQparameter
	 * @param commondList List&lt;ElementMethod&gt;
	 * @param absMQparameter AbstractMQparameter
	 */
	private static final void commondListRun(List<ElementMethod> commondList, AbstractMQparameter absMQparameter) {
		if (commondList == null || commondList.size() == 0 || absMQparameter.getParameter() == null) return;
		/* 进行监控 */
		if (MQConsts.ACC_MonitorMap && absMQparameter.getDustbinObjectMap() != null) {
			//Table tabl=new Table();
			MQArrayTable table = new MQArrayTable();
			MonitorMap monitorMap = new MonitorMap();
			monitorMap.start(absMQparameter.getDustbinObjectMap());
			monitorMap.setTable(table.getTable());
			for (int i = 0, len = commondList.size(); i < len; i++) {
				table.appendUnline("run:[" + i + "]", commondList.get(i).classzz.getSimpleName(), commondList.get(i).method.getName());
				//System.out.println("run:("+i+")["+commondList.get(i).classzz.getName()+"]["+commondList.get(i).method.getName()+"]");
				commondList.get(i).Run(absMQparameter.getParameter());
				monitorMap.check(i, absMQparameter.getDustbinObjectMap());
			}
			//monitorMap.putArrayTable(table.table);
			//monitorMap.print();
			System.out.println(table.print());
			monitorMap = null;
			table = null;
		} else {
			for (int i = 0, len = commondList.size(); i < len; i++)
				commondList.get(i).Run(absMQparameter.getParameter());
		}
	}

	@Deprecated
	static final void getStackTrace() {
		StackTraceElement[] FallbackArray = Thread.currentThread().getStackTrace();
		for (int i = 0, len = FallbackArray.length; i < len; i++) {
			StackTraceElement ste = FallbackArray[i];
			System.out.println("Fallback[" + i + "]:" + ste.getClassName() + "->" + ste.getFileName() + "->" + ste.getMethodName() + "->" + ste.getLineNumber());
		}
	}

	/**
	 * 通过系统参数，得到list集，已经过滤
	 * @param parameter MQparameter
	 * @return List< ElementMethod >
	 */
	static final List<ElementMethod> getElementMethodListByValue(final MQparameter parameter) {
		if (parameter == null) return new ArrayList<ElementMethod>();
		List<ElementMethod> commondList = new ArrayList<ElementMethod>();
		if (parameter.value.order) commondList = MQContainer.mapValue.getListByMQparameter(parameter);
		else commondList = MQContainer.mapValue.getListByMQparameterRnd(parameter);
		//if (MQSpringConsts.ACC_MQRunViewwarning) for (int i = 0, len = commondList.size(); i < len; i++)
		//MQWarning.show("发现标识正确项", commondList.get(i).toString());
		//过滤参数不合法
		commondList = MQUtilsInit.filter(commondList, parameter);
		return commondList;
	}

	/**
	 * 判断当前线程的堆栈是否含有Map类<br/>
	 * 用于判断当前线程与系统是否相交
	 * @return boolean
	 */
	public static final boolean isThreadSafe() {
		StackTraceElement[] FallbackArray = Thread.currentThread().getStackTrace();
		//StackTraceElement ste = FallbackArray[i];
		for (int i = 0, len = FallbackArray.length; i < len; i++)
			if (MQContainer.isExistClass(FallbackArray[i].getClassName())) return false;
		return true;
	}
}
