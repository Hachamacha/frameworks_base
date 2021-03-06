/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tools.layoutlib.create;

import org.objectweb.asm.AnnotationVisitor;
<<<<<<< HEAD
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
=======
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
>>>>>>> upstream/master
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;

/**
 * This class visitor renames a class from a given old name to a given new name.
 * The class visitor will also rename all inner classes and references in the methods.
 * <p/>
<<<<<<< HEAD
 *
 * For inner classes, this handles only the case where the outer class name changes.
 * The inner class name should remain the same.
 */
public class RenameClassAdapter extends ClassVisitor {


=======
 * 
 * For inner classes, this handles only the case where the outer class name changes.
 * The inner class name should remain the same. 
 */
public class RenameClassAdapter extends ClassAdapter {

    
>>>>>>> upstream/master
    private final String mOldName;
    private final String mNewName;
    private String mOldBase;
    private String mNewBase;

    /**
     * Creates a class visitor that renames a class from a given old name to a given new name.
     * The class visitor will also rename all inner classes and references in the methods.
     * The names must be full qualified internal ASM names (e.g. com/blah/MyClass$InnerClass).
     */
    public RenameClassAdapter(ClassWriter cv, String oldName, String newName) {
<<<<<<< HEAD
        super(Opcodes.ASM4, cv);
        mOldBase = mOldName = oldName;
        mNewBase = mNewName = newName;

=======
        super(cv);
        mOldBase = mOldName = oldName;
        mNewBase = mNewName = newName;
        
>>>>>>> upstream/master
        int pos = mOldName.indexOf('$');
        if (pos > 0) {
            mOldBase = mOldName.substring(0, pos);
        }
        pos = mNewName.indexOf('$');
        if (pos > 0) {
            mNewBase = mNewName.substring(0, pos);
        }
<<<<<<< HEAD

=======
        
>>>>>>> upstream/master
        assert (mOldBase == null && mNewBase == null) || (mOldBase != null && mNewBase != null);
    }


    /**
     * Renames a type descriptor, e.g. "Lcom.package.MyClass;"
     * If the type doesn't need to be renamed, returns the input string as-is.
     */
    String renameTypeDesc(String desc) {
        if (desc == null) {
            return null;
        }

        return renameType(Type.getType(desc));
    }
<<<<<<< HEAD

=======
    
>>>>>>> upstream/master
    /**
     * Renames an object type, e.g. "Lcom.package.MyClass;" or an array type that has an
     * object element, e.g. "[Lcom.package.MyClass;"
     * If the type doesn't need to be renamed, returns the internal name of the input type.
     */
    String renameType(Type type) {
        if (type == null) {
            return null;
        }

        if (type.getSort() == Type.OBJECT) {
            String in = type.getInternalName();
            return "L" + renameInternalType(in) + ";";
        } else if (type.getSort() == Type.ARRAY) {
            StringBuilder sb = new StringBuilder();
            for (int n = type.getDimensions(); n > 0; n--) {
                sb.append('[');
            }
            sb.append(renameType(type.getElementType()));
            return sb.toString();
        }
        return type.getDescriptor();
    }

    /**
     * Renames an object type, e.g. "Lcom.package.MyClass;" or an array type that has an
     * object element, e.g. "[Lcom.package.MyClass;".
     * This is like renameType() except that it returns a Type object.
     * If the type doesn't need to be renamed, returns the input type object.
     */
    Type renameTypeAsType(Type type) {
        if (type == null) {
            return null;
        }

        if (type.getSort() == Type.OBJECT) {
            String in = type.getInternalName();
            String newIn = renameInternalType(in);
            if (newIn != in) {
                return Type.getType("L" + newIn + ";");
            }
        } else if (type.getSort() == Type.ARRAY) {
            StringBuilder sb = new StringBuilder();
            for (int n = type.getDimensions(); n > 0; n--) {
                sb.append('[');
            }
            sb.append(renameType(type.getElementType()));
            return Type.getType(sb.toString());
        }
        return type;
    }

    /**
     * Renames an internal type name, e.g. "com.package.MyClass".
     * If the type doesn't need to be renamed, returns the input string as-is.
     * <p/>
     * The internal type of some of the MethodVisitor turns out to be a type
       descriptor sometimes so descriptors are renamed too.
     */
    String renameInternalType(String type) {
        if (type == null) {
            return null;
        }

        if (type.equals(mOldName)) {
            return mNewName;
        }

        if (mOldBase != mOldName && type.equals(mOldBase)) {
            return mNewBase;
        }
<<<<<<< HEAD

=======
    
>>>>>>> upstream/master
        int pos = type.indexOf('$');
        if (pos == mOldBase.length() && type.startsWith(mOldBase)) {
            return mNewBase + type.substring(pos);
        }

        // The internal type of some of the MethodVisitor turns out to be a type
        // descriptor sometimes. This is the case with visitTypeInsn(type) and
        // visitMethodInsn(owner). We try to detect it and adjust it here.
        if (type.indexOf(';') > 0) {
            type = renameTypeDesc(type);
        }

        return type;
    }

    /**
     * Renames a method descriptor, i.e. applies renameType to all arguments and to the
     * return value.
     */
    String renameMethodDesc(String desc) {
        if (desc == null) {
            return null;
        }

        Type[] args = Type.getArgumentTypes(desc);

        StringBuilder sb = new StringBuilder("(");
        for (Type arg : args) {
            String name = renameType(arg);
            sb.append(name);
        }
        sb.append(')');
<<<<<<< HEAD

=======
        
>>>>>>> upstream/master
        Type ret = Type.getReturnType(desc);
        String name = renameType(ret);
        sb.append(name);

        return sb.toString();
    }

<<<<<<< HEAD

    /**
     * Renames the ClassSignature handled by ClassVisitor.visit
=======
    
    /**
     * Renames the ClassSignature handled by ClassVisitor.visit 
>>>>>>> upstream/master
     * or the MethodTypeSignature handled by ClassVisitor.visitMethod.
     */
    String renameTypeSignature(String sig) {
        if (sig == null) {
            return null;
        }
        SignatureReader reader = new SignatureReader(sig);
        SignatureWriter writer = new SignatureWriter();
        reader.accept(new RenameSignatureAdapter(writer));
        sig = writer.toString();
        return sig;
    }

<<<<<<< HEAD

=======
    
>>>>>>> upstream/master
    /**
     * Renames the FieldTypeSignature handled by ClassVisitor.visitField
     * or MethodVisitor.visitLocalVariable.
     */
    String renameFieldSignature(String sig) {
        if (sig == null) {
            return null;
        }
        SignatureReader reader = new SignatureReader(sig);
        SignatureWriter writer = new SignatureWriter();
        reader.acceptType(new RenameSignatureAdapter(writer));
        sig = writer.toString();
        return sig;
    }

<<<<<<< HEAD

    //----------------------------------
    // Methods from the ClassAdapter

=======
    
    //----------------------------------
    // Methods from the ClassAdapter
    
>>>>>>> upstream/master
    @Override
    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces) {
        name = renameInternalType(name);
        superName = renameInternalType(superName);
        signature = renameTypeSignature(signature);
<<<<<<< HEAD

=======
        
>>>>>>> upstream/master
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        assert outerName.equals(mOldName);
        outerName = renameInternalType(outerName);
        name = outerName + "$" + innerName;
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
            String signature, String[] exceptions) {
        desc = renameMethodDesc(desc);
        signature = renameTypeSignature(signature);
        MethodVisitor mw = super.visitMethod(access, name, desc, signature, exceptions);
        return new RenameMethodAdapter(mw);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        desc = renameTypeDesc(desc);
        return super.visitAnnotation(desc, visible);
    }
<<<<<<< HEAD

=======
    
>>>>>>> upstream/master
    @Override
    public FieldVisitor visitField(int access, String name, String desc,
            String signature, Object value) {
        desc = renameTypeDesc(desc);
        signature = renameFieldSignature(signature);
        return super.visitField(access, name, desc, signature, value);
    }
<<<<<<< HEAD


=======
    
    
>>>>>>> upstream/master
    //----------------------------------

    /**
     * A method visitor that renames all references from an old class name to a new class name.
     */
<<<<<<< HEAD
    public class RenameMethodAdapter extends MethodVisitor {
=======
    public class RenameMethodAdapter extends MethodAdapter {
>>>>>>> upstream/master

        /**
         * Creates a method visitor that renames all references from a given old name to a given new
         * name. The method visitor will also rename all inner classes.
         * The names must be full qualified internal ASM names (e.g. com/blah/MyClass$InnerClass).
         */
        public RenameMethodAdapter(MethodVisitor mv) {
<<<<<<< HEAD
            super(Opcodes.ASM4, mv);
=======
            super(mv);
>>>>>>> upstream/master
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            desc = renameTypeDesc(desc);
<<<<<<< HEAD

=======
            
>>>>>>> upstream/master
            return super.visitAnnotation(desc, visible);
        }

        @Override
        public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
            desc = renameTypeDesc(desc);

            return super.visitParameterAnnotation(parameter, desc, visible);
        }

        @Override
        public void visitTypeInsn(int opcode, String type) {
            type = renameInternalType(type);
<<<<<<< HEAD

=======
            
>>>>>>> upstream/master
            super.visitTypeInsn(opcode, type);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            owner = renameInternalType(owner);
            desc = renameTypeDesc(desc);

            super.visitFieldInsn(opcode, owner, name, desc);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            owner = renameInternalType(owner);
            desc = renameMethodDesc(desc);

            super.visitMethodInsn(opcode, owner, name, desc);
        }
<<<<<<< HEAD

=======
        
>>>>>>> upstream/master
        @Override
        public void visitLdcInsn(Object cst) {
            // If cst is a Type, this means the code is trying to pull the .class constant
            // for this class, so it needs to be renamed too.
            if (cst instanceof Type) {
                cst = renameTypeAsType((Type) cst);
            }
            super.visitLdcInsn(cst);
        }

        @Override
        public void visitMultiANewArrayInsn(String desc, int dims) {
            desc = renameTypeDesc(desc);
<<<<<<< HEAD

=======
         
>>>>>>> upstream/master
            super.visitMultiANewArrayInsn(desc, dims);
        }

        @Override
        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
            type = renameInternalType(type);
<<<<<<< HEAD

=======
            
>>>>>>> upstream/master
            super.visitTryCatchBlock(start, end, handler, type);
        }

        @Override
        public void visitLocalVariable(String name, String desc, String signature,
                Label start, Label end, int index) {
            desc = renameTypeDesc(desc);
            signature = renameFieldSignature(signature);
<<<<<<< HEAD

=======
            
>>>>>>> upstream/master
            super.visitLocalVariable(name, desc, signature, start, end, index);
        }

    }

    //----------------------------------
<<<<<<< HEAD

    public class RenameSignatureAdapter extends SignatureVisitor {
=======
    
    public class RenameSignatureAdapter implements SignatureVisitor {
>>>>>>> upstream/master

        private final SignatureVisitor mSv;

        public RenameSignatureAdapter(SignatureVisitor sv) {
<<<<<<< HEAD
            super(Opcodes.ASM4);
            mSv = sv;
        }

        @Override
=======
            mSv = sv;
        }

>>>>>>> upstream/master
        public void visitClassType(String name) {
            name = renameInternalType(name);
            mSv.visitClassType(name);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public void visitInnerClassType(String name) {
            name = renameInternalType(name);
            mSv.visitInnerClassType(name);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public SignatureVisitor visitArrayType() {
            SignatureVisitor sv = mSv.visitArrayType();
            return new RenameSignatureAdapter(sv);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public void visitBaseType(char descriptor) {
            mSv.visitBaseType(descriptor);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public SignatureVisitor visitClassBound() {
            SignatureVisitor sv = mSv.visitClassBound();
            return new RenameSignatureAdapter(sv);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public void visitEnd() {
            mSv.visitEnd();
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public SignatureVisitor visitExceptionType() {
            SignatureVisitor sv = mSv.visitExceptionType();
            return new RenameSignatureAdapter(sv);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public void visitFormalTypeParameter(String name) {
            mSv.visitFormalTypeParameter(name);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public SignatureVisitor visitInterface() {
            SignatureVisitor sv = mSv.visitInterface();
            return new RenameSignatureAdapter(sv);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public SignatureVisitor visitInterfaceBound() {
            SignatureVisitor sv = mSv.visitInterfaceBound();
            return new RenameSignatureAdapter(sv);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public SignatureVisitor visitParameterType() {
            SignatureVisitor sv = mSv.visitParameterType();
            return new RenameSignatureAdapter(sv);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public SignatureVisitor visitReturnType() {
            SignatureVisitor sv = mSv.visitReturnType();
            return new RenameSignatureAdapter(sv);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public SignatureVisitor visitSuperclass() {
            SignatureVisitor sv = mSv.visitSuperclass();
            return new RenameSignatureAdapter(sv);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public void visitTypeArgument() {
            mSv.visitTypeArgument();
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> upstream/master
        public SignatureVisitor visitTypeArgument(char wildcard) {
            SignatureVisitor sv = mSv.visitTypeArgument(wildcard);
            return new RenameSignatureAdapter(sv);
        }

<<<<<<< HEAD
        @Override
        public void visitTypeVariable(String name) {
            mSv.visitTypeVariable(name);
        }

=======
        public void visitTypeVariable(String name) {
            mSv.visitTypeVariable(name);
        }
        
>>>>>>> upstream/master
    }
}
