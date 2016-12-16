/**
 * 
 */
package MaQiaoSpringExtend;

import java.util.ArrayList;
import java.util.List;

import MaQiaoSpringExtend.Element.ElementMethod;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQRUN {
	public static final void run(final MQparameter parameter) {
		if (parameter == null || !parameter.islegitimate()) {
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
			runByIdentifier(parameter);
		}
	}

	/**
	 * 运行通过Identifier查到的记录
	 * @param parameter MQparameter
	 */
	static final void runByIdentifier(final MQparameter parameter) {
		List<ElementMethod> commondList = getElementMethodListByIdentifier(parameter);
		for (int i = 0, len = commondList.size(); i < len; i++)
			commondList.get(i).Run(parameter);

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
	static final List<ElementMethod> getElementMethodListByIdentifier(final MQparameter parameter) {
		if (parameter == null) return new ArrayList<ElementMethod>();
		List<ElementMethod> commondList = new ArrayList<ElementMethod>();
		if (parameter.identifier.order) commondList = MQSpringExtendContainer.mapIdentifier.getListByMQparameter(parameter);
		else commondList = MQSpringExtendContainer.mapIdentifier.getListByMQparameterRnd(parameter);
		//if (MQSpringConsts.ACC_MQRunViewwarning) for (int i = 0, len = commondList.size(); i < len; i++)
		//MQWarning.show("发现标识正确项", commondList.get(i).toString());
		//过滤参数不合法
		commondList = MQSpringUtils.filter(commondList, parameter);
		return commondList;
	}

	/**
	 * 判断当前线程的堆栈是否含有Map类<br/>
	 * 用于判断当前线程与系统是否相交
	 * @return boolean
	 */
	public static final boolean isThreadSafe() {
		StackTraceElement[] FallbackArray = Thread.currentThread().getStackTrace();
		for (int i = 0, len = FallbackArray.length; i < len; i++) {
			StackTraceElement ste = FallbackArray[i];
			//System.out.println("Fallback["+i+"]:"+ste.getClassName()+"->"+ste.getFileName()+"->"+ste.getMethodName()+"->"+ste.getLineNumber());
			if (MQSpringExtendContainer.isExistClass(ste.getClassName())) { return false; }
		}
		return true;
	}
}
