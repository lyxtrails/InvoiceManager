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
        put("厨房", "kitchen");
    }};
    public static HashMap<String, String> actionTranslation = new HashMap<String, String>() {{
        put("维修", "repair");
        put("更换", "replace");
    }};
    public static HashMap<String, String> objectTranslation = new HashMap<String, String>() {{
        put("水龙头", "faucet");
        put("浴缸", "bathtub");
    }};
}
