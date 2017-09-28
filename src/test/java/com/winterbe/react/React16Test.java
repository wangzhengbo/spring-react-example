package com.winterbe.react;

import java.io.IOException;

import javax.script.CompiledScript;
import javax.script.ScriptException;

public class React16Test {
    public static void main(String[] args) throws ScriptException, IOException {
        React16 react16 = new React16();

        int count = 100;

        System.out.println("---------using v8 start---------");
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            react16.evalResourceUsingV8("react16/static/serverSideRender.js");
        }
        System.out.println("---------using v8 cost " + (System.currentTimeMillis() - start) + " milliseconds---------");
        System.out.println("---------using v8 end---------");

        System.out.println("---------using nashorn start---------");
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            react16.evalResourceUsingNashorn("react16/static/serverSideRender.js");
        }
        System.out.println(
                "---------using nashorn cost " + (System.currentTimeMillis() - start) + " milliseconds---------");
        System.out.println("---------using nashorn end---------");

        System.out.println();
        System.out.println("---------using rhino start---------");
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            react16.evalResourceUsingRhino("react16/static/serverSideRender.js");
        }
        System.out.println(
                "---------using rhino cost " + (System.currentTimeMillis() - start) + " milliseconds---------");
        System.out.println("---------using rhino end---------");

        System.out.println("---------using nashorn with compiled script start---------");
        CompiledScript compiledScript = react16.compileResourceUsingNashorn("react16/static/serverSideRender.js");
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            compiledScript.eval();
        }
        System.out.println(
                "---------using nashorn cost " + (System.currentTimeMillis() - start) + " milliseconds---------");
        System.out.println("---------using nashorn with compiled script end---------");

        System.out.println("---------using rhino with compiled script start---------");
        compiledScript = react16.compileResourceUsingRhino("react16/static/serverSideRender.js");
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            compiledScript.eval();
        }
        System.out.println("---------using rhino with compiled script cost " + (System.currentTimeMillis() - start)
                + " milliseconds---------");
        System.out.println("---------using rhino with compiled script end---------");
    }
}
