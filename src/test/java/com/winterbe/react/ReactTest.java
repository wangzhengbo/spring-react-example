package com.winterbe.react;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class ReactTest {
    private React react;
    private List<Comment> comments;

    @Before
    public void setUp() {
        react = new React();

        comments = new ArrayList<Comment>();
        comments.add(new Comment("Peter Parker", "This is a comment."));
        comments.add(new Comment("John Doe", "This is *another* comment."));
    }

    @Test
    public void testRenderCommentBoxUsingV8() throws Exception {
        String html = react.renderCommentBoxUsingV8(comments);
        assertThat(html, startsWith("<div"));
        Document doc = Jsoup.parse(html);
        assertThat(doc.select("div.comment").size(), is(2));
    }

    @Test
    public void testRenderCommentBoxUsingNashorn() throws Exception {
        String html = react.renderCommentBoxUsingNashorn(comments);
        assertThat(html, startsWith("<div"));
        Document doc = Jsoup.parse(html);
        assertThat(doc.select("div.comment").size(), is(2));
    }

    @Test
    public void testRenderCommentBoxUsingRhino() throws Exception {
        String html = react.renderCommentBoxUsingRhino(comments);
        assertThat(html, startsWith("<div"));
        Document doc = Jsoup.parse(html);
        assertThat(doc.select("div.comment").size(), is(2));
    }

    @Test
    public void testRenderCommentBoxUsingV8AndNashornAndRhino() throws Exception {
        React react = new React();

        react.renderCommentBoxUsingV8(comments);
        react.renderCommentBoxUsingNashorn(comments);
        react.renderCommentBoxUsingRhino(comments);

        final int count = 1000;

        // J2V8
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            react.renderCommentBoxUsingV8(comments);
        }
        System.out.println("Calculate " + count + " times cost " + (System.currentTimeMillis() - start)
                + " milliseconds using v8.");

        // nashorn
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            react.renderCommentBoxUsingNashorn(comments);
        }
        System.out.println("Calculate " + count + " times cost " + (System.currentTimeMillis() - start)
                + " milliseconds using nashorn.");

        // rhino
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            react.renderCommentBoxUsingRhino(comments);
        }
        System.out.println("Calculate " + count + " times cost " + (System.currentTimeMillis() - start)
                + " milliseconds using rhino.");
    }

    @Test
    public void testRenderCommentBoxUsingV8AndNashornAndRhino2() throws Exception {
        React react = new React();

        react.renderCommentBoxUsingV8();
        react.renderCommentBoxUsingNashorn();
        react.renderCommentBoxUsingRhino();

        final int count = 1000;

        // J2V8
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            react.renderCommentBoxUsingV8();
        }
        System.out.println("Calculate " + count + " times cost " + (System.currentTimeMillis() - start)
                + " milliseconds using v8.");

        // nashorn
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            react.renderCommentBoxUsingNashorn();
        }
        System.out.println("Calculate " + count + " times cost " + (System.currentTimeMillis() - start)
                + " milliseconds using nashorn.");

        // rhino
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            react.renderCommentBoxUsingRhino();
        }
        System.out.println("Calculate " + count + " times cost " + (System.currentTimeMillis() - start)
                + " milliseconds using rhino.");
    }
}