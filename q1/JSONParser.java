package q1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONParser {
    private String json;
    private int idx = 0;

    public JSONParser(String json) {
        this.json = json;
    }

    private void skipWhitespace() {
        while(Character.isWhitespace(json.charAt(idx))) {
            idx++;
        }
    }

    private Boolean parseBoolean() {
        if(json.startsWith("true", idx)) {
            idx+=4;
            return Boolean.TRUE;
        } else if(json.startsWith("false", idx)) {
            idx+=5;
            return Boolean.FALSE;
        }
        throw new Error("JSON Format Error: Expected 'true' or 'false'");
    }

    private Object parseNull() {
        if(json.startsWith("null", idx)) {
            idx+=4;
            return null;
        }
        throw new Error("JSON Format Error: Expected 'null'");
    }

    private Number parseNumber() {
        int start = idx;
        while(Character.isDigit(json.charAt(idx)) || json.charAt(idx) == '-' || json.charAt(idx) == '+' || json.charAt(idx) == '.' || json.charAt(idx) == 'e' || json.charAt(idx) == 'E') {
            idx++;
        }
        String num = json.substring(start, idx);
        if(num.contains(".")) {
            return Double.parseDouble(num);
        }
        return Integer.parseInt(num);
    }

    private String parseString() {
        String res = "";
        idx++; // skips '"'
        while(json.charAt(idx) != '"') {
            res+=json.charAt(idx++);
        }
        idx++;
        return res;
    }

    private List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        idx++; // skips '['
        skipWhitespace();
        while(json.charAt(idx) != ']') {
            list.add(parseValue());
            skipWhitespace();
            if(json.charAt(idx) == ',') {
                idx++;
            } else if(json.charAt(idx) != ']') {
                throw new Error("JSON Format Error: Expected ',' or ']'");
            }
        }
        idx++;
        return list;
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> map = new HashMap<>();
        idx++; // skip '{'
        skipWhitespace();
        while(json.charAt(idx) != '}') {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            if(json.charAt(idx) != ':') {
                throw new Error("JSON Format Error: Expected ':' after key");
            }
            idx++; // skips ':'
            skipWhitespace();
            Object val = parseValue();
            map.put(key, val);
            skipWhitespace();
            if(json.charAt(idx) == ',') {
                idx++;
            } else if(json.charAt(idx) != '}') {
                throw new Error("JSON Format Error: Expected ',' or '}'");
            }
        }
        idx++;
        return map;
    }

    private Object parseValue() {
        skipWhitespace();
        char curr = json.charAt(idx);
        if(curr == '{') {
            return parseObject();
        } else if(curr == '[') {
            return parseArray();
        } else if(curr == '"') {
            return parseString();
        } else if(curr == '-' || Character.isDigit(curr)) {
            return parseNumber();
        } else if(curr == 't' || curr == 'f') {
            return parseBoolean();
        } else if(curr == 'n') {
            return parseNull();
        }
        throw new Error("JSON Format Error:" + curr);
    }

    private Map<String, Object> parse() {
        skipWhitespace();
        if(json.charAt(idx) != '{') {
            throw new Error("JSON Format Error: Must start with '{'");
        }
        return parseObject();
    }

    public static Map<String, Object> parse(String json) {
        JSONParser parser = new JSONParser(json);
        return parser.parse();
    }
    public static void main(String[] args) {
        String input = "{\"debug\": \n\"on\",\"window\": {\"title\": \"sample\",\"size\": 500}}";
        Map<String, Object> output = JSONParser.parse(input);
        System.out.println(output.get("debug"));
        System.out.println((((Map<String, Object>)(output.get("window"))).get("title")));
        System.out.println((((Map<String, Object>)(output.get("window"))).get("size")));

        assert output.get("debug").equals("on");
        assert (((Map<String, Object>)(output.get("window"))).get("title")).equals("sample");
        assert (((Map<String, Object>)(output.get("window"))).get("size")).equals(500);
    }
}