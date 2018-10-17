using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Test : MonoBehaviour {

    protected AndroidJavaObject _activity;

    void Start () {
        var jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        _activity = jc.GetStatic<AndroidJavaObject>("currentActivity");
    }
	
	void Update () {
		
	}

    public void onClick() {
        _activity.Call("callFunction", "Login");
    }
}
