/*
 * Recoded by deyo 
 */
package com.deyo.rbw.ingame;

import java.util.HashMap;

import com.deyo.rbw.Main;
import com.deyo.rbw.classes.Utils;

public class PINHandling {
    public static HashMap<Integer, String[]> loadedPins = new HashMap();

    public static int generatePin(String ID2, String ingamename) {
        int pin = Utils.getRandomNumber(1000, 9999);
        if (loadedPins.containsKey(pin)) {
            return PINHandling.generatePin(ID2, ingamename);
        }
        loadedPins.put(pin, new String[]{ID2, ingamename});
        Main.runTaskLater(() -> loadedPins.remove(pin), 300000L);
        return pin;
    }

    public static String[] getIdByPin(int pin) {
        return loadedPins.get(pin);
    }
}

