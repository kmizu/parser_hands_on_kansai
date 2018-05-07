package com.github.kmizu.parser_hands_on_kansai.answer;

import com.github.kmizu.parser_hands_on_kansai.ParseFailure;
import com.github.kmizu.parser_hands_on_kansai.integer.AbstractIntegerParser;

import java.text.ParseException;

public class MyIntegerParser extends AbstractIntegerParser {
    @Override
    public Integer parse(String input) {
        if(input.startsWith("0")) {
            if(input.length() > 1) throw new ParseFailure("If input starts with 0, input.length must be 1");
            return 0;
        }
        int result = 0;
        for(int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if(!('0' <= ch && ch <= '9'))
                throw new ParseFailure("input contains no digit character");
            result = result * 10 + (ch - '0');
        }
        return result;
    }
}
