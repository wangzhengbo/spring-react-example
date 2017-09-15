package com.winterbe.react;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;

public class React {
    private ThreadLocal<V8> v8EngineHolder = new ThreadLocal<V8>() {
        @Override
        protected V8 initialValue() {
            V8 v8 = V8.createV8Runtime();
            try {
                Console console = new Console();
                V8Object v8Console = new V8Object(v8);
                v8.add("console", v8Console);
                v8Console.registerJavaMethod(console, "log", "log", new Class<?>[] { Object.class });
                v8Console.registerJavaMethod(console, "error", "error", new Class<?>[] { Object.class });
                v8Console.release();

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
        try {
            V8 v8 = v8EngineHolder.get();
            V8Array parameters = new V8Array(v8);
            V8Array v8Array = new V8Array(v8);
            for (Comment comment : comments) {
                V8Object v8Value = new V8Object(v8);
                v8Value.add("author", comment.getAuthor());
                v8Value.add("text", comment.getText());
                v8Array.push(v8Value);
            }
            parameters.push(v8Array);
            return v8.executeStringFunction("renderServer", parameters);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component", e);
        }
    }

    public String renderCommentBoxUsingNashorn(List<Comment> comments) {
        try {
            Invocable invocable = (Invocable) nashornEngineHolder.get();
            Object html = invocable.invokeFunction("renderServer", comments);
            return String.valueOf(html);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component", e);
        }
    }

    public String renderCommentBoxUsingRhino(List<Comment> comments) {
        try {
            Invocable invocable = (Invocable) rhinoEngineHolder.get();

            Object[] arr = new Object[comments.size()];
            for (int i = 0; i < comments.size(); i++) {
                NativeObject obj = new NativeObject();
                obj.put("text", obj, comments.get(i).getText());
                obj.put("author", obj, comments.get(i).getAuthor());

                arr[i] = obj;
            }
            Object html = invocable.invokeFunction("renderServer", new NativeArray(arr));
            return String.valueOf(html);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component", e);
        }
    }

    public String renderCommentBoxUsingV8() {
        try {
            V8 v8 = v8EngineHolder.get();
            V8Array parameters = new V8Array(v8);
            return v8.executeStringFunction("renderServer2", parameters);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component", e);
        }
    }

    public String renderCommentBoxUsingNashorn() {
        try {
            Invocable invocable = (Invocable) nashornEngineHolder.get();
            Object html = invocable.invokeFunction("renderServer2");
            return String.valueOf(html);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component", e);
        }
    }

    public String renderCommentBoxUsingRhino() {
        try {
            Invocable invocable = (Invocable) rhinoEngineHolder.get();
            Object html = invocable.invokeFunction("renderServer2");
            return String.valueOf(html);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component", e);
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

    private class Console {
        public void log(final Object message) {
            System.out.println("[INFO] " + message.getClass().getName());
        }

        public void error(final Object message) {
            System.out.println("[ERROR] " + message);
        }
    }
}