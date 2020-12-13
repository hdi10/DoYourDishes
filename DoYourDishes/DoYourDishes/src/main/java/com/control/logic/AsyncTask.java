package com.control.logic;

import android.util.Log;

import com.control.networkHttp.HttpRequest;
import com.model.dataModel.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
    private static final String TAG = "MyAsyncTask";
    final HttpRequest httpEngine = new HttpRequest();
    private final HashMap<String, String> stringValues = new HashMap<String, String>();
    private Boolean logInError = false;
    private Boolean exceptionThrown = false;
    private Boolean registerError = false;
    private HomeController homeController;
    private LoginController loginController;
    private RegisterController registerController;

    private final String BackendURL = "https://doyourdishes.herokuapp.com/api";


    public AsyncTask(String _token, String _method, HomeController _homeController) {
        stringValues.put("method", _method);
        homeController = _homeController;

        switch (stringValues.get("method")) {
            case "WHO_AM_I":

                this.stringValues.put("token", _token);
                Log.d(TAG, "AsyncTaskFactory: ");
                break;
        }
    }
    // state = ActiveState.WHOAMI;


    //AsyncLogin
    public AsyncTask(String _userName, String _password, String _method, LoginController _loginController) {
        stringValues.put("method", _method);
        loginController = _loginController;

        switch (stringValues.get("method")) {
            case "LOG_IN":

                stringValues.put("userName", _userName);
                stringValues.put("password", _password);
                break;
        }
    }

    //AsyncLogin
    public AsyncTask(String _userName, String _password, String _method, RegisterController _registerController) {
        stringValues.put("method", _method);
        registerController = _registerController;

        switch (stringValues.get("method")) {
            case "REGISTER_USER":
                stringValues.put("userName", _userName);
                stringValues.put("password", _password);
                break;
        }

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          makeRequestBody()                                 //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @NotNull
    private RequestBody makeRequestBody() {
        RequestBody requestBody = null;
        switch (stringValues.get("method")) {
            case "REGISTER_USER":
                requestBody = new FormBody.Builder()
                        .add("userName", stringValues.get("userName"))
                        .add("password", stringValues.get("password"))
                        .build();
                break;
            case "LOG_IN":
                requestBody = new FormBody.Builder()
                        .add("userName", stringValues.get("userName"))
                        .add("password", stringValues.get("password"))
                        .build();
                break;
            case "WHO_AM_I":
                requestBody = new FormBody.Builder()
                        .build();
                break;

        }
        return requestBody;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //                                         choose what to do doInBackround()                  //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Void doInBackground(Void... params) {
        switch (stringValues.get("method")) {
            case "REGISTER_USER":
                doWhenRegisterUser();
                break;
            case "LOG_IN":
                doWhenLoginBackGround();
                break;
            case "WHO_AM_I":
                doWhenWhoAmIBackground();
                break;
        }
        return null;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          doInBackround()                                   //
    ///////////////////////////////////////////////////////////////////////////////////////////////


    public void  doWhenLoginBackGround(Void... voids) {
        // https://stackoverflow.com/questions/26161538/throw-an-exception-in-doinbackground-and-catch-in-onpostexecute#:~:text=You%20cannot%20throw%20exceptions%20across,handle%20it%20in%20onPostExecute()%20.&text=No%2C%20you%20can't%20throw%20exception%20in%20the%20background%20thread.
        //Here you are in the worker thread and you are not allowed to access UI thread from here
        //Here you can perform network operations or any heavy operations you want.
        Log.d(TAG, "doWhenLoginBackGround: in");
        RequestBody requestBody = makeRequestBody();
        try {
            JSONObject response = httpEngine.POST(BackendURL + "/auth/login", requestBody, "");
            Log.d(TAG, "doWhenLogin response: " + response);
            if (response.has("token")) {
                stringValues.put("responseText", response.getString("token"));
                logInError = false;
            } else {
                stringValues.put("responseText", response.getString("customMessage"));
                logInError = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionThrown = true;
            stringValues.put("exceptionResponse", e.toString());
            Log.d(TAG, "AsyncLogin: " + e.toString());
        }
        Log.d(TAG, "doWhenLoginBackGround: out");
    }

    public void doWhenWhoAmIBackground(Void... voids) {
        //Here you are in the worker thread and you are not allowed to access UI thread from here
        //Here you can perform network operations or any heavy operations you want.
        JSONObject response = null;
        RequestBody requestBody = makeRequestBody();
        try {
            response = httpEngine.GET(BackendURL + "/auth/whoAmI", requestBody, stringValues.get("token"));
            if(response.has("data")){
                stringValues.put("responseUserName", response.getJSONObject("data").getString("userName"));
                stringValues.put("responseUserPlan", response.getJSONObject("data").getString("plan"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionThrown = true;
            stringValues.put("exceptionResponse", e.toString());
            Log.d(TAG, "doWhenWhoAmIBackground exception: " + e.toString());
        }
    }

    private void doWhenRegisterUser(){
        Log.d(TAG, "doWhenRegisterUser: in");

        //Here you are in the worker thread and you are not allowed to access UI thread from here
        //Here you can perform network operations or any heavy operations you want.
        JSONObject response = null;
        RequestBody requestBody = makeRequestBody();
        try {
            response = httpEngine.POST(BackendURL + "/user/createUser", requestBody, "");
            if (response.has("customMessage")) {
                stringValues.put("responseText", response.getString("customMessage"));
                registerError = true;
            } else {
                stringValues.put("responseText", "user created!");
                registerError = false;
                doWhenLoginBackGround();
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionThrown = true;
            stringValues.put("exceptionResponse", e.toString());
            Log.d(TAG, "AsyncWhoAmI: " + e.toString());
        }
        Log.d(TAG, "doWhenRegisterUser: out");
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //After completing execution of given task , control will return here.
        //Hence if you want to populate UI elements with fetched data, do it here
        switch (stringValues.get("method")) {
            case "REGISTER_USER":
                doWhenRegisterUserPostExecute();
                break;
            case "LOG_IN":
                doWhenLoginPostExecute();
                break;
            case "WHO_AM_I":
                doWhenWhoAmIPostExecute();
                break;
        }

    }
    private void doWhenRegisterUserPostExecute(){
        Log.d(TAG, "doWhenRegisterUserPostExecute: in");
        if (exceptionThrown) {
            registerController.showToast("network error");
        }  else if (registerError) {
        registerController.showToast(stringValues.get("responseText"));
    } else {
        registerController.startHomeView(stringValues.get("responseText"));
        Log.d(TAG, "onPostExecute: login: " + stringValues.get("responseText"));
    }
    }

    private void doWhenWhoAmIPostExecute() {
        Log.d(TAG, "doWhenWhoAmIPostExecute: in");
        String resUserName = stringValues.get("responseUserName");
        String resUserPlan = stringValues.get("responseUserPlan");
        homeController.updateUi(resUserName);
        homeController.updateUser(resUserName, resUserPlan);
        Log.d(TAG, "doWhenWhoAmIPostExecute: out");
    }

    private void doWhenLoginPostExecute() {
        Log.d(TAG, "doWhenLoginPostExecute: in");
        if (exceptionThrown) {
            loginController.showToast("network error");
        } else if (logInError) {
            loginController.showToast(stringValues.get("responseText"));
        } else {
            loginController.startHomeView(stringValues.get("responseText"));
            Log.d(TAG, "onPostExecute: login: " + stringValues.get("responseText"));
        }
        Log.d(TAG, "doWhenLoginPostExecute: out");
    }
}
