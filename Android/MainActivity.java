package com.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MainActivity extends UnityPlayerActivity implements ResourceFinder {

    private Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globals = JsePlatform.standardGlobals();
        globals.finder = this;
        try {
            LuaValue mainActivity = CoerceJavaToLua.coerce(this);
            load("Activity.lua").call(mainActivity);
            globals.get("onCreate").call(CoerceJavaToLua.coerce(savedInstanceState));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LuaValue load(String file){
        return globals.loadfile(file);
    }

    public InputStream findResource(String name) {
        try {
            return this.getAssets().open(name);
        } catch (IOException ioe) {
        }
        return null;
    }

    public Class<?> getClass(String name) {
        Class<?> cls = null;
        try {
            cls = Class.forName(name, true, getClass().getClassLoader());
            //cls = Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    public Object newObject(Class<?> cls){
        Object obj = null;
        try {
            obj = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public Object newObject(Class<?> cls, Object... args) {
        if(args == null || args.length == 0){
            return newObject(cls);
        }

        Constructor[] cons = cls.getDeclaredConstructors();
        for (int i = 0; i < cons.length; i++) {
            Constructor con = cons[i];
            Class[] parameterTypes = con.getParameterTypes();
            Constructor<?> ctr = null;
            try {
                if (parameterTypes.length == 0) {
                    return newObject(cls);
                } else {
                    if (parameterTypes.length == args.length) {
                        ctr = cls.getConstructor(parameterTypes);
                        return ctr.newInstance(args);
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void callFunction(String funcName,String values){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    globals.get(funcName).call(values);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void callFunction(String funcName){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    globals.get(funcName).call();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void unitySendMessage(String gameObjectName,String callbackMethod,String param){
        UnityPlayer.UnitySendMessage(gameObjectName,callbackMethod,param);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        globals.get("onNewIntent").call(CoerceJavaToLua.coerce(intent));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        LuaValue f = globals.get("onWindowFocusChanged");
        if (!f.isnil())
            try {
                f.call(CoerceJavaToLua.coerce(Boolean.valueOf(hasFocus)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LuaValue f = globals.get("onKeyUp");
        if (!f.isnil())
            try {
                return f.call(CoerceJavaToLua.coerce(keyCode),
                        CoerceJavaToLua.coerce(event)).toboolean();
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        return super.onKeyUp(keyCode,event);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LuaValue f = globals.get("onKeyDown");
        if (!f.isnil()) {
            try {
                f.call(CoerceJavaToLua.coerce(Integer.valueOf(keyCode)),
                        CoerceJavaToLua.coerce(event))
                        .toboolean();
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LuaValue f = globals.get("onTouchEvent");
        if (!f.isnil()) {
            try {
                f.call(CoerceJavaToLua.coerce(event)).toboolean();
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        globals.get("onActivityResult").call(
                CoerceJavaToLua.coerce(Integer.valueOf(requestCode)),
                CoerceJavaToLua.coerce(Integer.valueOf(resultCode)),
                CoerceJavaToLua.coerce(data));
    }
}
