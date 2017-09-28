package com.winterbe.react;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;

public class React16 {
    private ThreadLocal<V8> v8EngineHolder = new ThreadLocal<V8>() {
        @Override
        protected V8 initialValue() {
            NodeJS nodeJS = NodeJS.createNodeJS();
            V8 v8 = nodeJS.getRuntime();
            try {
                v8.executeVoidScript(readAsString("static/v8-polyfill.js"));
                v8.executeVoidScript(readAsString("react16/vendor/react.development.js"));
                v8.executeVoidScript(readAsString("react16/vendor/react-dom-server.browser.development.js"));
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
                evalResource(nashornScriptEngine, "static/nashorn-polyfill.js");
                evalResource(nashornScriptEngine, "react16/vendor/react.development.js");
                evalResource(nashornScriptEngine, "react16/vendor/react-dom-server.browser.development.js");
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
                evalResource(rhinoScriptEngine, "static/rhino-polyfill.js");
                evalResource(rhinoScriptEngine, "react16/vendor/react.development.js");
                evalResource(rhinoScriptEngine, "react16/vendor/react-dom-server.browser.development.js");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return rhinoScriptEngine;
        }
    };

    public Object evalResourceUsingV8(String resource) throws ScriptException, IOException {
        return v8EngineHolder.get().executeObjectScript(readAsString(resource));
    }

    public Object evalResourceUsingNashorn(String resource) throws ScriptException, IOException {
        return evalResource(nashornEngineHolder.get(), resource);
    }

    public Object evalResourceUsingRhino(String resource) throws ScriptException, IOException {
        return evalResource(rhinoEngineHolder.get(), resource);
    }

    public CompiledScript compileResourceUsingNashorn(String resource) throws ScriptException, IOException {
        return compileResource(nashornEngineHolder.get(), resource);
    }

    public CompiledScript compileResourceUsingRhino(String resource) throws ScriptException, IOException {
        return compileResource(rhinoEngineHolder.get(), resource);
    }

    private Object evalResource(ScriptEngine scriptEngine, String resource) throws ScriptException, IOException {
        return scriptEngine.eval(readAsString(resource));
    }

    private CompiledScript compileResource(ScriptEngine scriptEngine, String resource)
            throws ScriptException, IOException {
        Compilable compilable = (Compilable) scriptEngine;
        return compilable.compile(readAsString(resource));
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
