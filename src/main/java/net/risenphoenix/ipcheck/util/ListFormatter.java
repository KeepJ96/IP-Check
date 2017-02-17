/*
 * Copyright © 2014 Jacob Keep (Jnk1296). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 *
 *  * Neither the name of JuNK Software nor the names of its contributors may 
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.risenphoenix.ipcheck.util;

import java.util.ArrayList;

public class ListFormatter {

    private ArrayList<String> input;
    private FormatFilter filter = null;

    public ListFormatter(ArrayList<String> input) {
        this.input = input;
    }

    public ListFormatter(ArrayList<String> input, FormatFilter filter) {
        this.input = input;
        this.filter = filter;
    }

    public StringBuilder getFormattedList() {
        // Convert the Input into a fixed Array
        ArrayList<String> filtered = new ArrayList<String>();
        String[] convert;

        // Filter Input if so requested
        if (filter != null) {
            for (String s:input) {
                String result = this.filter.execute(s);
                if (result != null && !result.equals("")) filtered.add(result);
            }

            convert = new String[filtered.size()];
            filtered.toArray(convert);
        } else {
            convert = new String[input.size()];
            input.toArray(convert);
        }

        // New String Builder
        StringBuilder sb = new StringBuilder();

        // Format the List
        for (int i = 0; i < convert.length; i++) {
            if (convert.length == 1) {
                sb.append(convert[0]);
                break;
            } else if (convert.length == 2) {
                sb.append(convert[0]);
                sb.append(" and ");
                sb.append(convert[1]);
                break;
            } else if (convert.length > 2) {
                sb.append(convert[i]);

                if (i == (convert.length - 2)) {
                    sb.append(" and ");
                } else if (i == (convert.length - 1)) {
                    sb.append(".");
                } else {
                    sb.append(", ");
                }
            }
        }

        return sb;
    }

}
