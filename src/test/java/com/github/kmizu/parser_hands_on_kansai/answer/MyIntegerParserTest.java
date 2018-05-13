package com.github.kmizu.parser_hands_on_kansai.answer;

import com.github.kmizu.parser_hands_on_kansai.ParseFailure;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class MyIntegerParserTest {
    MyIntegerParserRevised parser = new MyIntegerParserRevised();
    @Test(expected = ParseFailure.class)
    public void confirmTestFailure1() {
        parser.parse("ab");
    }

    @Test(expected = ParseFailure.class)
    public void confirmTestFailure2() {
        parser.parse("1a");
    }

    @Test(expected = ParseFailure.class)
    public void confirmTestFailure3() {
        parser.parse("a1");
    }

    @Test(expected = ParseFailure.class)
    public void confirmTestFailure4() {
        parser.parse("01");
    }

    @Test(expected = ParseFailure.class)
    public void confirmTestFailure5() {
        parser.parse("123a456");
    }

    @Test(expected = ParseFailure.class)
    public void confirmTestFailure6() {
        parser.parse("123456a");
    }

    @Test(expected = ParseFailure.class)
    public void confirmTestFailure7() {
        parser.parse("ab12345b");
    }

    @Test
    public void confirm0IsParsed() {
        assertEquals((Integer)0, parser.parse("0"));
    }

    @Test
    public void confirm1IsParsed() {
        assertEquals((Integer)1, parser.parse("1"));
    }

    @Test
    public void confirm9IsParsed() {
        assertEquals((Integer)9, parser.parse("9"));
    }

    @Test
    public void confirm10IsParsed() {
        assertEquals((Integer)10, parser.parse("10"));
    }

    @Test
    public void confirm11IsParsed() {
        assertEquals((Integer)11, parser.parse("11"));
    }

    @Test
    public void confirm19IsParsed() {
        assertEquals((Integer)19, parser.parse("19"));
    }

    @Test
    public void confirm20IsParsed() {
        assertEquals((Integer)20, parser.parse("20"));
    }

    @Test
    public void confirm90IsParsed() {
        assertEquals((Integer)90, parser.parse("90"));
    }

    @Test
    public void confirm91IsParsed() {
        assertEquals((Integer)91, parser.parse("91"));
    }

    @Test
    public void confirm99IsParsed() {
        assertEquals((Integer)99, parser.parse("99"));
    }

    @Test
    public void confirm100IsParsed() {
        assertEquals((Integer)100, parser.parse("100"));
    }

    @Test
    public void confirm101IsParsed() {
        assertEquals((Integer)101, parser.parse("101"));
    }

    @Test
    public void confirm109IsParsed() {
        assertEquals((Integer)109, parser.parse("109"));
    }

    @Test
    public void confirm110IsParsed() {
        assertEquals((Integer)110, parser.parse("110"));
    }

    @Test
    public void confirm111IsParsed() {
        assertEquals((Integer)111, parser.parse("111"));
    }
}
