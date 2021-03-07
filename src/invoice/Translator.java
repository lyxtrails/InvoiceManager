package invoice;

import java.util.HashMap;

public class Translator {
    public static HashMap<String, String> floorTranslation = new HashMap<String, String>() {{
        put("1", "the first floor");
        put("2", "the second floor");
        put("3", "the third floor");
    }};
    public static HashMap<String, String> placeTranslation = new HashMap<String, String>() {{
        put("主卧", "master bedroom");
        put("主卧储衣间", "master bedroom closet");
        put("卧室", "bedroom");
        put("卧室储衣间", "bedroom closet");
        put("厕所", "bathroom");
        put("厨房", "kitchen");
        put("地下室", "basement");
        put("走廊", "hallway");
        put("阁楼", "attic");
    }};
    public static HashMap<String, String> actionTranslation = new HashMap<String, String>() {{
        put("维修", "fix");
        put("更换", "replace");
        put("油漆", "paint");
    }};
    public static HashMap<String, String> objectTranslation = new HashMap<String, String>() {{
        put("水龙头", "faucet");
        put("浴缸", "bathtub");
        put("水管", "water pipe");
        put("下水管", "sewer pipe");
        put("门", "door");
        put("灯", "light");
        put("灯泡", "light bulb");
        put("锁", "lock");
        put("柜子", "cabinet");
        put("柜门", "cabinet door");
        put("电源插座", "power outlet");
        put("墙", "wall");
        put("天花板", "ceiling");
    }};
}
