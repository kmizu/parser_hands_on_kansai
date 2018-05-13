package com.github.kmizu.parser_hands_on_kansai.answer;

import com.github.kmizu.parser_hands_on_kansai.ParseFailure;
import com.github.kmizu.parser_hands_on_kansai.json.AbstractJSONParser;
import com.github.kmizu.parser_hands_on_kansai.json.JSONNode;
import static com.github.kmizu.parser_hands_on_kansai.json.JSONNode.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyJSONParser extends AbstractJSONParser {
    private String input;
    private int position;

    // ch にマッチ
    public String accept(char ch) {
        if(position < input.length() && input.charAt(position) == ch) {
            position++;
            return ((Character)ch).toString();
        }
        throw new ParseFailure("current position is over range or current character is not " + ch);
    }

    // notExpected 以外の１文字にマッチ
    public char acceptExcept(char notExpected) {
        if(position >= input.length()) {
            throw new ParseFailure("unexpected EOF");
        } else {
            char ch = input.charAt(position);
            if(ch != notExpected) {
                position++;
                return ch;
            } else {
                throw new ParseFailure("unexpected character: " + ch);
            }
        }
    }

    // 任意の１文字にマッチ
    public char accept() {
        if(position < input.length()) {
            char ch = input.charAt(position);
            position++;
            return ch;
        }
        throw new ParseFailure("unexpected EOF");
    }

    public char acceptRange(char from, char to) {
        if(position < input.length()){
            char ch = input.charAt(position);
            if(from <= ch && ch <= to) {
                position++;
                return ch;
            } else {
                throw new ParseFailure("current character is out of range: [" + from + "..." + to + "]");
            }
        } else {
            throw new ParseFailure("unexpected EOF");
        }
    }

    @Override
    public JSONNode parse(String input) {
        this.input = input;
        this.position = 0;
        return jvalue();
    }

    // jvalue = jobject
    //        | jarray
    //        | jnull
    //        | jboolean
    //        | jnumber
    //        | jstring
    public JSONNode jvalue() {
        try {
            save();
            return jobject();
        } catch (ParseFailure e1) {
            restore();;
        }

        try {
            save();
            return jarray();
        } catch (ParseFailure e2) {
            restore();
        }

        try {
            save();
            return jnull();
        } catch (ParseFailure e3) {
            restore();
        }

        try {
            save();
            return jboolean();
        } catch (ParseFailure e4) {
            restore();
        }

        try {
            save();
            return jnumber();
        } catch (ParseFailure e5) {
            restore();
            return jstring();
        }
    }

    public JSONNode.JSONObject jobject() {
        accept('{');
        Map<String, JSONNode> properties = new HashMap<>();
        try {
            String fkey = jstring().value;
            accept(':');
            JSONNode fvalue = jvalue();
            properties.put(fkey, fvalue);
        } catch(ParseFailure e) {
            accept('}');
            return new JSONNode.JSONObject(properties);
        }
        while(true) {
            save();
            try {
                accept(',');
                String key = jstring().value;
                accept(':');
                JSONNode value = jvalue();
                properties.put(key, value);
            } catch (ParseFailure e) {
                restore();
                break;
            }
        }
        accept('}');
        return new JSONNode.JSONObject(properties);
    }

    public JSONNode.JSONString jstring() {
        StringBuilder content = new StringBuilder();
        accept('"');
        while(true) {
            save();
            try {
                accept('\\');
                char code = accept();
                code = escapeSequence(code);
                content.append(code);
            } catch (ParseFailure e1) {
                restore();
                try {
                    char code = acceptExcept('"');
                    content.append(code);
                } catch (ParseFailure e2) {
                    break;
                }
            }
        }
        accept('"');
        return new JSONNode.JSONString(new String(content));
    }

    char escapeSequence(char ch) {
        switch (ch) {
            case 'r':
                return '\r';
            case 'n':
                return '\n';
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case '\\':
                return '\\';
            case '"':
                return '"';
            default:
                throw new ParseFailure("unknown escape sequence");
        }
    }

    public JSONNode.JSONArray jarray() {
        accept('[');
        List<JSONNode> elements = new ArrayList<>();
        try {
            elements.add(jvalue());
        } catch (ParseFailure e) {
            accept(']');
            return new JSONNode.JSONArray(elements);
        }
        while(true) {
            save();
            try {
                accept(',');
                elements.add(jvalue());
            } catch (ParseFailure e) {
                restore();
                break;
            }
        }
        accept(']');
        return new JSONNode.JSONArray(elements);
    }

    public JSONNode.JSONNull jnull() {
        accept('n');
        accept('u');
        accept('l');
        accept('l');
        return JSONNode.JSONNull.getInstance();
    }

    public JSONNode.JSONBoolean jboolean() {
        save();
        try {
            accept('t');
            accept('r');
            accept('u');
            accept('e');
            return new JSONNode.JSONBoolean(true);
        } catch (ParseFailure e) {
            accept('f');
            accept('a');
            accept('l');
            accept('s');
            accept('e');
            return new JSONNode.JSONBoolean(false);
        }
    }

    public JSONNode.JSONNumber jnumber() {
        int result = (acceptRange('0', '9') - '0');
        if(result == 0) {
            if(position >= input.length()){
                return new JSONNode.JSONNumber(result);
            } else {
                char ch = input.charAt(position);
                if('0' <= ch && ch <= '9') {
                    throw new ParseFailure("if number starts with 0, it cannot be follow by any digit");
                }
                return new JSONNode.JSONNumber(result);
            }
        }
        while(true) {
            try {
                result = result * 10 + (acceptRange('0', '9') - '0');
            } catch (ParseFailure e) {
                return new JSONNode.JSONNumber(result);
            }
        }
    }
}
