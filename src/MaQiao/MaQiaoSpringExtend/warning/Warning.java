/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.warning;

/**
 * System.out.println --> 显示状态<br/>
 * 0:初始化状态<br/>
 * 1:运行状态<br/>
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class Warning {

	public static final void show(final String key, final short value) {
		show(WarningConsts.defaultState, key, value);
	}

	static final void show(final int state, final String key, final short value) {
		show(state, key, String.valueOf(value));
	}

	public static final void show(final String key, final char value) {
		show(WarningConsts.defaultState, key, value);
	}

	public static final void show(final int state, final String key, final char value) {
		show(state, key, String.valueOf(value));
	}

	public static final void show(final String key, final byte value) {
		show(WarningConsts.defaultState, key, value);
	}

	public static final void show(final int state, final String key, final byte value) {
		show(state, key, String.valueOf(value));
	}

	public static final void show(final String key, final float value) {
		show(WarningConsts.defaultState, key, value);
	}

	public static final void show(final int state, final String key, final float value) {
		show(state, key, String.valueOf(value));
	}

	public static final void show(final String key, final boolean value) {
		show(WarningConsts.defaultState, key, value);
	}

	public static final void show(final int state, final String key, final boolean value) {
		show(state, key, String.valueOf(value));
	}

	public static final void show(final String key, final double value) {
		show(WarningConsts.defaultState, key, value);
	}

	public static final void show(final int state, final String key, final double value) {
		show(state, key, String.valueOf(value));
	}

	public static final void show(final String key, final long value) {
		show(WarningConsts.defaultState, key, value);
	}

	public static final void show(final int state, final String key, final long value) {
		show(state, key, String.valueOf(value));
	}

	public static final void show(final String key, final int value) {
		show(WarningConsts.defaultState, key, value);
	}

	public static final void show(final int state, final String key, final int value) {
		show(state, key, String.valueOf(value));
	}

	public static final void show(final String key, final Object value) {
		show(WarningConsts.defaultState, key, value);
	}

	public static final void show(final int state, final String key, final Object value) {
		if (WarningConsts.ACC_MQInitViewwarning && state == 0) System.out.println(WarningConsts.ACC_MQInitHead + key + ":" + (value == null ? "Null" : value.toString()));
		if (WarningConsts.ACC_MQRunViewwarning && state == 1) System.out.println(WarningConsts.ACC_MQRunHead + key + ":" + (value == null ? "Null" : value.toString()));
	}
}
