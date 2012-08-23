/*
<<<<<<< HEAD
 * Copyright (C) 2008-2012 The Android Open Source Project
=======
 * Copyright (C) 2008 The Android Open Source Project
>>>>>>> upstream/master
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

package android.renderscript;

/**
 *
 **/
public class Script extends BaseObj {
    /**
     * Only intended for use by generated reflected code.
     *
     * @param slot
     */
    protected void invoke(int slot) {
<<<<<<< HEAD
        mRS.nScriptInvoke(getID(mRS), slot);
=======
        mRS.nScriptInvoke(getID(), slot);
>>>>>>> upstream/master
    }

    /**
     * Only intended for use by generated reflected code.
     *
     * @param slot
     * @param v
     */
    protected void invoke(int slot, FieldPacker v) {
        if (v != null) {
<<<<<<< HEAD
            mRS.nScriptInvokeV(getID(mRS), slot, v.getData());
        } else {
            mRS.nScriptInvoke(getID(mRS), slot);
=======
            mRS.nScriptInvokeV(getID(), slot, v.getData());
        } else {
            mRS.nScriptInvoke(getID(), slot);
>>>>>>> upstream/master
        }
    }

    /**
     * Only intended for use by generated reflected code.
     *
     * @param slot
     * @param ain
     * @param aout
     * @param v
     */
    protected void forEach(int slot, Allocation ain, Allocation aout, FieldPacker v) {
        if (ain == null && aout == null) {
            throw new RSIllegalArgumentException(
                "At least one of ain or aout is required to be non-null.");
        }
        int in_id = 0;
        if (ain != null) {
<<<<<<< HEAD
            in_id = ain.getID(mRS);
        }
        int out_id = 0;
        if (aout != null) {
            out_id = aout.getID(mRS);
=======
            in_id = ain.getID();
        }
        int out_id = 0;
        if (aout != null) {
            out_id = aout.getID();
>>>>>>> upstream/master
        }
        byte[] params = null;
        if (v != null) {
            params = v.getData();
        }
<<<<<<< HEAD
        mRS.nScriptForEach(getID(mRS), slot, in_id, out_id, params);
=======
        mRS.nScriptForEach(getID(), slot, in_id, out_id, params);
>>>>>>> upstream/master
    }


    Script(int id, RenderScript rs) {
        super(id, rs);
    }


    /**
     * Only intended for use by generated reflected code.
     *
     * @param va
     * @param slot
     */
    public void bindAllocation(Allocation va, int slot) {
        mRS.validate();
        if (va != null) {
<<<<<<< HEAD
            mRS.nScriptBindAllocation(getID(mRS), va.getID(mRS), slot);
        } else {
            mRS.nScriptBindAllocation(getID(mRS), 0, slot);
=======
            mRS.nScriptBindAllocation(getID(), va.getID(), slot);
        } else {
            mRS.nScriptBindAllocation(getID(), 0, slot);
>>>>>>> upstream/master
        }
    }

    /**
     * Only intended for use by generated reflected code.
     *
     * @param index
     * @param v
     */
    public void setVar(int index, float v) {
<<<<<<< HEAD
        mRS.nScriptSetVarF(getID(mRS), index, v);
=======
        mRS.nScriptSetVarF(getID(), index, v);
>>>>>>> upstream/master
    }

    /**
     * Only intended for use by generated reflected code.
     *
     * @param index
     * @param v
     */
    public void setVar(int index, double v) {
<<<<<<< HEAD
        mRS.nScriptSetVarD(getID(mRS), index, v);
=======
        mRS.nScriptSetVarD(getID(), index, v);
>>>>>>> upstream/master
    }

    /**
     * Only intended for use by generated reflected code.
     *
     * @param index
     * @param v
     */
    public void setVar(int index, int v) {
<<<<<<< HEAD
        mRS.nScriptSetVarI(getID(mRS), index, v);
=======
        mRS.nScriptSetVarI(getID(), index, v);
>>>>>>> upstream/master
    }

    /**
     * Only intended for use by generated reflected code.
     *
     * @param index
     * @param v
     */
    public void setVar(int index, long v) {
<<<<<<< HEAD
        mRS.nScriptSetVarJ(getID(mRS), index, v);
=======
        mRS.nScriptSetVarJ(getID(), index, v);
>>>>>>> upstream/master
    }

    /**
     * Only intended for use by generated reflected code.
     *
     * @param index
     * @param v
     */
    public void setVar(int index, boolean v) {
<<<<<<< HEAD
        mRS.nScriptSetVarI(getID(mRS), index, v ? 1 : 0);
=======
        mRS.nScriptSetVarI(getID(), index, v ? 1 : 0);
>>>>>>> upstream/master
    }

    /**
     * Only intended for use by generated reflected code.
     *
     * @param index
     * @param o
     */
    public void setVar(int index, BaseObj o) {
<<<<<<< HEAD
        mRS.nScriptSetVarObj(getID(mRS), index, (o == null) ? 0 : o.getID(mRS));
=======
        mRS.nScriptSetVarObj(getID(), index, (o == null) ? 0 : o.getID());
>>>>>>> upstream/master
    }

    /**
     * Only intended for use by generated reflected code.
     *
     * @param index
     * @param v
     */
    public void setVar(int index, FieldPacker v) {
<<<<<<< HEAD
        mRS.nScriptSetVarV(getID(mRS), index, v.getData());
    }

    /**
     * Only intended for use by generated reflected code.
     *
     * @param index
     * @param v
     * @param e
     * @param dims
     */
    public void setVar(int index, FieldPacker v, Element e, int[] dims) {
        mRS.nScriptSetVarVE(getID(mRS), index, v.getData(), e.getID(mRS), dims);
=======
        mRS.nScriptSetVarV(getID(), index, v.getData());
>>>>>>> upstream/master
    }

    public void setTimeZone(String timeZone) {
        mRS.validate();
        try {
<<<<<<< HEAD
            mRS.nScriptSetTimeZone(getID(mRS), timeZone.getBytes("UTF-8"));
=======
            mRS.nScriptSetTimeZone(getID(), timeZone.getBytes("UTF-8"));
>>>>>>> upstream/master
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Builder {
        RenderScript mRS;

        Builder(RenderScript rs) {
            mRS = rs;
        }
    }


    public static class FieldBase {
        protected Element mElement;
        protected Allocation mAllocation;

        protected void init(RenderScript rs, int dimx) {
            mAllocation = Allocation.createSized(rs, mElement, dimx, Allocation.USAGE_SCRIPT);
        }

        protected void init(RenderScript rs, int dimx, int usages) {
            mAllocation = Allocation.createSized(rs, mElement, dimx, Allocation.USAGE_SCRIPT | usages);
        }

        protected FieldBase() {
        }

        public Element getElement() {
            return mElement;
        }

        public Type getType() {
            return mAllocation.getType();
        }

        public Allocation getAllocation() {
            return mAllocation;
        }

        //@Override
        public void updateAllocation() {
        }
    }
}

