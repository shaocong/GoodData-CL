/*
 * Copyright (c) 2009, GoodData Corporation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of conditions and
 *        the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *        and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * Neither the name of the GoodData Corporation nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.gooddata.naming;

/**
 * Shared namings
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class N {

    // table generator constants
    public final static String SRC_PFX = "o_";
    public final static String LKP_PFX = "d_";
    public final static String NM_PFX = "nm_";
    public final static String FCT_PFX = "f_";
    public final static String DT = "dt";
    public final static String DT_PFX = DT + "_";
    public final static String TM = "tm";
    public final static String TM_PFX = TM + "_";
    public final static String ID = "id";
    public final static String DT_ATTR_NAME = "date";
    public final static String TM_ATTR_NAME = "attr.time.second.of.day.";

    public final static String DT_SLI_SFX = "_" + DT;
    public final static String TM_SLI_SFX = "_" + TM;

    public static final String FACTS_OF = "factsof";


    //public final static String FTP_SRV_SUFFIX = "-upload";
    public final static String FTP_SRV_SUFFIX = "-di";

}
