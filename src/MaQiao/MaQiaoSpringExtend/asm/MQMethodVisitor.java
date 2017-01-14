/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.asm;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 对MethodVisitor与Opcodes接口生成的新类
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQMethodVisitor implements MethodVisitor, Opcodes {
	String[] paramNames = {};
	Method m = null;

	public MQMethodVisitor(String[] paramNames, final Method m) {
		this.paramNames = paramNames;
		this.m = m;
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		int i = index - 1;
		// 如果是静态方法，则第一就是参数
		// 如果不是静态方法，则第一个是"this"，然后才是方法的参数
		if (Modifier.isStatic(m.getModifiers())) i = index;
		if (i >= 0) for (int ii = 0, paraLen = paramNames.length; ii < paraLen; ii++) {
			if (paramNames[ii] == null) {
				paramNames[ii] = name;
				break;
			}
		}
	}

	@Override
	public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
		return null;
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		return null;
	}

	@Override
	public void visitAttribute(Attribute arg0) {
	}

	@Override
	public void visitCode() {
	}

	@Override
	public void visitEnd() {
	}

	@Override
	public void visitFieldInsn(int arg0, String arg1, String arg2, String arg3) {
	}

	@Override
	public void visitFrame(int arg0, int arg1, Object[] arg2, int arg3, Object[] arg4) {
	}

	@Override
	public void visitIincInsn(int arg0, int arg1) {
	}

	@Override
	public void visitInsn(int arg0) {
	}

	@Override
	public void visitIntInsn(int arg0, int arg1) {
	}

	@Override
	public void visitJumpInsn(int arg0, Label arg1) {
	}

	@Override
	public void visitLabel(Label arg0) {
	}

	@Override
	public void visitLdcInsn(Object arg0) {
	}

	@Override
	public void visitLineNumber(int arg0, Label arg1) {
	}

	@Override
	public void visitLookupSwitchInsn(Label arg0, int[] arg1, Label[] arg2) {
	}

	@Override
	public void visitMaxs(int arg0, int arg1) {
	}

	@Override
	public void visitMethodInsn(int arg0, String arg1, String arg2, String arg3) {
	}

	@Override
	public void visitMultiANewArrayInsn(String arg0, int arg1) {
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(int arg0, String arg1, boolean arg2) {
		return null;
	}

	@Override
	public void visitTableSwitchInsn(int arg0, int arg1, Label arg2, Label[] arg3) {
	}

	@Override
	public void visitTryCatchBlock(Label arg0, Label arg1, Label arg2, String arg3) {
	}

	@Override
	public void visitTypeInsn(int arg0, String arg1) {
	}

	@Override
	public void visitVarInsn(int arg0, int arg1) {
	}
}
