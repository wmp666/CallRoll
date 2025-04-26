package com.wmp.callroll.tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetCRInfo {
    public static String[] getInfo(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在");
            return null;
        }
        try {
            byte[] bytes = Files.readAllBytes(Path.of(path));
            return new String(bytes, StandardCharsets.UTF_8).split("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Map<String, Integer> getNameInfo(String... name) {
        Map<String, Integer> map = new HashMap<>();
        for (String s : name) {
            String[] temp = s.split(" ");
            if (temp.length == 1){
                if (!temp[0].isEmpty())
                    map.put(temp[0], 3);
            }else{
                map.put(temp[1], Integer.parseInt(temp[0]));
            }
        }
        return map;
    }


    public static ArrayList<String> getPunishInfo(String... name) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : name) {
            if (!s.isEmpty())
                    list.add(s);

        }
        return list;
    }
}
