package com.wmp.callroll.tools;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class CallRollTool {
    public static String getName(Map<String, Integer> nameInfo) {
        if (nameInfo.isEmpty())
            return "null";
        ArrayList<String> nameList = new ArrayList<>();
        nameInfo.keySet().forEach(nameList::add);
        Random random = new Random();
        return nameList.get(random.nextInt(nameList.size()));
    }
    public static String getPunish(String... Punish) {
        if (Punish.length == 0)
            return "null";
        Random random = new Random();
        return Punish[random.nextInt(Punish.length)];
    }
}
