package com.winterbe.react;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.fasterxml.jackson.databind.ObjectMapper;

public class React {
    private ThreadLocal<V8> v8EngineHolder = new ThreadLocal<V8>() {
        @Override
        protected V8 initialValue() {
            NodeJS nodeJS = NodeJS.createNodeJS();
            V8 v8 = nodeJS.getRuntime();
            try {
                v8.executeVoidScript(readAsString("static/v8-polyfill.js"));
                v8.executeVoidScript(readAsString("static/vendor/react.js"));
                v8.executeVoidScript(readAsString("static/vendor/showdown.min.js"));
                v8.executeVoidScript(readAsString("static/commentBox.js"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return v8;
        }
    };

    private ThreadLocal<ScriptEngine> nashornEngineHolder = new ThreadLocal<ScriptEngine>() {
        @Override
        protected ScriptEngine initialValue() {
            ScriptEngine nashornScriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
            try {
                nashornScriptEngine.eval(readAsString("static/nashorn-polyfill.js"));
                nashornScriptEngine.eval(readAsString("static/vendor/react.js"));
                nashornScriptEngine.eval(readAsString("static/vendor/showdown.min.js"));
                nashornScriptEngine.eval(readAsString("static/commentBox.js"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return nashornScriptEngine;
        }
    };

    private ThreadLocal<ScriptEngine> rhinoEngineHolder = new ThreadLocal<ScriptEngine>() {
        @Override
        protected ScriptEngine initialValue() {
            ScriptEngine rhinoScriptEngine = new ScriptEngineManager().getEngineByName("rhino");
            try {
                rhinoScriptEngine.eval(readAsString("static/rhino-polyfill.js"));
                rhinoScriptEngine.eval(readAsString("static/vendor/react.js"));
                rhinoScriptEngine.eval(readAsString("static/vendor/showdown.min.js"));
                rhinoScriptEngine.eval(readAsString("static/commentBox.js"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return rhinoScriptEngine;
        }
    };

    public String renderCommentBoxUsingV8(List<Comment> comments) {
        V8Array parameters = null;
        V8Array v8Array = null;
        try {
            V8 v8 = v8EngineHolder.get();
            parameters = new V8Array(v8);
            v8Array = new V8Array(v8);
            for (Comment comment : comments) {
                V8Object v8Value = new V8Object(v8);
                v8Value.add("author", comment.getAuthor());
                v8Value.add("text", comment.getText());
                v8Array.push(v8Value);
            }
            parameters.push(v8Array);
            return v8.executeStringFunction("renderServer", parameters);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component using v8", e);
        } finally {
            if (v8Array != null) {
                for (int i = 0, size = v8Array.length(); i < size; i++) {
                    V8Object v8Value = (V8Object) v8Array.get(i);
                    v8Value.release();
                }
                v8Array.release();
            }
            if (parameters != null) {
                parameters.release();
            }
        }
    }

    public String renderCommentBoxUsingNashorn(List<Comment> comments) {
        try {
            Invocable invocable = (Invocable) nashornEngineHolder.get();
            Object html = invocable.invokeFunction("renderServer", comments);
            return String.valueOf(html);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component using nashorn", e);
        }
    }

    public String renderCommentBoxUsingRhino(List<Comment> comments) {
        try {
            ScriptEngine engine = rhinoEngineHolder.get();
            Invocable invocable = (Invocable) engine;

            ObjectMapper mapper = new ObjectMapper();
            String strComments = mapper.writeValueAsString(comments);
            Object jsonComments = engine.eval(strComments);

            Object html = invocable.invokeFunction("renderServer", jsonComments);
            return String.valueOf(html);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component using rhino", e);
        }
    }

    public String renderCommentBoxUsingV8() {
        try {
            V8 v8 = v8EngineHolder.get();
            V8Array parameters = new V8Array(v8);
            return v8.executeStringFunction("renderServer2", parameters);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component using v8", e);
        }
    }

    public String renderCommentBoxUsingNashorn() {
        try {
            Invocable invocable = (Invocable) nashornEngineHolder.get();
            Object html = invocable.invokeFunction("renderServer2");
            return String.valueOf(html);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component using nashorn", e);
        }
    }

    public String renderCommentBoxUsingRhino() {
        try {
            Invocable invocable = (Invocable) rhinoEngineHolder.get();
            Object html = invocable.invokeFunction("renderServer2");
            return String.valueOf(html);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component using rhino", e);
        }
    }

    private String readAsString(String path) throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }
}