/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes;

import java.util.HashMap;

import com.deyo.rbw.childclasses.Config;

public class Modifiers {
    public static HashMap<String, Integer[]> modifiers = new HashMap();

    public static void add(String ID2, Integer gamenumber, Types type) {
        int ty = type.i;
        Integer[] arg = new Integer[]{gamenumber, ty};
        modifiers.put(ID2, arg);
    }

    public static Integer get(String ID2, int gamenumber) {
        Integer[] arg = modifiers.get(ID2);
        if (!modifiers.containsKey(ID2)) {
            return 0;
        }
        modifiers.remove(ID2);
        if (!arg[0].equals(gamenumber)) {
            return 0;
        }
        if (arg[1] == 2) {
            return Integer.parseInt(Config.getValue("partyof2-modifier"));
        }
        if (arg[1] == 3) {
            return Integer.parseInt(Config.getValue("partyof3-modifier"));
        }
        if (arg[1] == 4) {
            return Integer.parseInt(Config.getValue("partyof4-modifier"));
        }
        return 0;
    }

    public static enum Types {
        partyOf2(2),
        partyOf3(3),
        partyOf4(4);

        public final int i;

        private Types(int i) {
            this.i = i;
        }
    }
}

