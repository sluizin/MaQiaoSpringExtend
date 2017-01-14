package MaQiao.MaQiaoSpringExtend.warning;

/**
 * 常量池
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class WarningConsts {
	/** 是否输出初始化状态 */
	public static boolean ACC_MQInitViewwarning = true;
	/** 初始化状态的前缀 */
	static final String ACC_MQInitHead = "MQExtend->Initialization->";
	/** 是否输出运行状态 */
	public static boolean ACC_MQRunViewwarning = true;
	/** 输出运行状态的前缀 */
	static final String ACC_MQRunHead = "MQExtend->Run->";
	/** 0:初始化 1:运行 */
	static final int defaultState = 1;
}
