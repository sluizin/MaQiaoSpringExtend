/**
 * 
 */
package MaQiaoSpringExtend;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import aj.org.objectweb.asm.Type;

/**
 * 通过asm分解类
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class asmReadClass {

	/**
	 * 得到某类中某个方法的参数数组
	 * @param classzz Class< ? >
	 * @param m Method
	 * @return String[]
	 */
	static final String[] getParaName(final Class<?> classzz, final Method m) {
		if (m == null) return new String[0];
		int len = m.getParameterTypes().length;
		if (len == 0) return new String[0];
		final String[] paramNames = new String[len];
		try {
			String classfile = classzz.getName();
			classfile = classfile.replaceAll("\\.", "/");
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL url = loader.getResource(classfile + ".class");
			InputStream is = url.openStream();
			ClassReader cr = new ClassReader(is);
			cr.accept(new ClassVisitor() {
				@Override
				public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
					//System.out.println("desc:"+desc);
					if (desc == null) return null;
					final Type[] args = Type.getArgumentTypes(desc);
					// 方法名相同并且参数个数相同
					if (!name.equals(m.getName()) || !sameType(args, m.getParameterTypes())) { return null; }//visitMethod(access, name, desc, signature, exceptions); }
					/*
					 * for (int i = 0, len = args.length; i < len; i++)
					 * System.out.println(m.getName() + "->type[" + i + "]:" + args[i].getClassName());
					 */
					final int paraLen = paramNames.length;
					//MethodVisitor v = visitMethod(access, name, desc, signature, exceptions);
					return new MethodVisitor() {
						@Override
						public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
							int i = index - 1;
							// 如果是静态方法，则第一就是参数
							// 如果不是静态方法，则第一个是"this"，然后才是方法的参数
							if (Modifier.isStatic(m.getModifiers())) i = index;
							//System.out.println("i:" + paraLen + "\\" + i + "->" + name);
							//if (i >= 0 && i < paraLen) paramNames[i] = name;
							if (i >= 0) for (int ii = 0; ii < paraLen; ii++) {
								if (paramNames[ii] == null) {
									paramNames[ii] = name;
									break;
								}
							}
							//visitLocalVariable(name, desc, signature, start, end, index);
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
					};
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
			}, 0);
			cr = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return paramNames;
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
	final static boolean sameType(final Type[] types, final Class<?>[] clazzes) {
		if (types.length != clazzes.length) return false;
		for (int i = 0; i < types.length; i++)
			if (!Type.getType(clazzes[i]).equals(types[i])) return false;
		return true;
	}

}
