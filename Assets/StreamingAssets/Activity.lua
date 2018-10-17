-- java -> C#  = mainActivity:unitySendMessage(GameObjectName,MethodName,params)
-- local chunk = mainActivity:load('Custom.lua')()

local mainActivity = ...

local Toast = mainActivity:getClass('android.widget.Toast')
-- local obj = mainActivity:newObject(Toast)
local api,handler

function onCreate(savedInstanceState)
	print("create")
end

function Login()
    Toast:makeText(mainActivity,"Login",1):show()
end

function onNewIntent(intent)
	api:handleIntent(intent,handler)
end

function onKeyDown(keyCode, event)
	print('onKeyDown', keyCode, event)
end

function onKeyUp(keyCode, event)
	print('onKeyUp', keyCode, event)
end

function onTouchEvent(event)
	print('onTouchEvent', event)
	return true
end

function onTrackballEvent(event)
	print('onTrackballEvent', event)
	return true
end

function onWindowFocusChanged(hasWindowFocus)
	print('onWindowFocusChanged', hasWindowFocus)
end

function onWindowSystemUiVisibilityChanged(visible)
	print('onWindowSystemUiVisibilityChanged', visible)
end

function onActivityResult(requestCode,resultCode,data)
	print(onActivityResult,registerApp,resultCode,data)
end