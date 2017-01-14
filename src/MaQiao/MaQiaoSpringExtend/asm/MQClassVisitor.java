/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.asm;

import java.lang.reflect.Method;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import aj.org.objectweb.asm.Type;

/**
 * 对ClassVisitor与Opcodes接口生成的新类
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQClassVisitor implements ClassVisitor, Opcodes {
	String[] paramNames = {};
	Method m = null;

	public MQClassVisitor(String[] paramNames, final Method m) {
		this.paramNames = paramNames;
		this.m = m;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
		if (desc == null) return null;
		final Type[] args = Type.getArgumentTypes(desc);
		// 方法名相同并且参数个数相同
		if (!name.equals(m.getName()) || !isSameType(args, m.getParameterTypes())) return null;
		/*
		 * for (int i = 0, len = args.length; i < len; i++)
		 * System.out.println(m.getName() + "->type[" + i + "]:" + args[i].getClassName());
		 */
		return new MQMethodVisitor(paramNames, m);
	}

	@Override
	public void visit(int arg0, int arg1, String arg2, String arg3, String arg4, String[] arg5) {
	}

	@Override
	public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
		return null;
	}

	@Override
	public void visitAttribute(Attribute arg0) {
	}

	@Override
	public void visitEnd() {
	}

	@Override
	public FieldVisitor visitField(int arg0, String arg1, String arg2, String arg3, Object arg4) {
		return null;
	}

	@Override
	public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
	}

	@Override
	public void visitOuterClass(String arg0, String arg1, String arg2) {
	}

	@Override
	public void visitSource(String arg0, String arg1) {
	}

	/**
	 * <p>
	 * 比较参数类型是否一致
	 * </p>
	 * @param types
	 *            asm的类型({@link Type})
	 * @param clazzes
	 *            java 类型({@link Class})
	 * @return
	 */
	private final static boolean isSameType(final Type[] types, final Class<?>[] clazzes) {
		if (types.length != clazzes.length) return false;
		for (int i = 0; i < types.length; i++)
			if (!Type.getType(clazzes[i]).equals(types[i])) return false;
		return true;
	}
}
