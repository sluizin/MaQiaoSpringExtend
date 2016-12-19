/**
 * 
 */
package MaQiaoSpringExtend;

/**
 * System.out.println --> 显示状态<br/>
 * 0:初始化状态<br/>
 * 1:运行状态<br/>
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQWarning {
	/** 0:初始化 1:运行 */
	private static final int defaultState = 1;

	static final void show(final String key, final short value) {
		show(defaultState, key, value);
	}

	static final void show(final int state, final String key, final short value) {
		show(state, key, String.valueOf(value));
	}

	static final void show(final String key, final char value) {
		show(defaultState, key, value);
	}

	static final void show(final int state, final String key, final char value) {
		show(state, key, String.valueOf(value));
	}

	static final void show(final String key, final byte value) {
		show(defaultState, key, value);
	}

	static final void show(final int state, final String key, final byte value) {
		show(state, key, String.valueOf(value));
	}

	static final void show(final String key, final float value) {
		show(defaultState, key, value);
	}

	static final void show(final int state, final String key, final float value) {
		show(state, key, String.valueOf(value));
	}

	static final void show(final String key, final boolean value) {
		show(defaultState, key, value);
	}

	static final void show(final int state, final String key, final boolean value) {
		show(state, key, String.valueOf(value));
	}

	static final void show(final String key, final double value) {
		show(defaultState, key, value);
	}

	static final void show(final int state, final String key, final double value) {
		show(state, key, String.valueOf(value));
	}

	static final void show(final String key, final long value) {
		show(defaultState, key, value);
	}

	static final void show(final int state, final String key, final long value) {
		show(state, key, String.valueOf(value));
	}

	static final void show(final String key, final int value) {
		show(defaultState, key, value);
	}

	static final void show(final int state, final String key, final int value) {
		show(state, key, String.valueOf(value));
	}

	static final void show(final String key, final Object value) {
		show(defaultState, key, value);
	}

	 static final void show(final int state, final String key, final Object value) {
		if (state == 0 && MQConsts.ACC_MQInitViewwarning) System.out.println(MQConsts.ACC_MQInitHead + key + ":" + (value == null ? "Null" : value.toString()));
		if (state == 1 && MQConsts.ACC_MQRunViewwarning) System.out.println(MQConsts.ACC_MQRunHead + key + ":" + (value == null ? "Null" : value.toString()));
	}
}
