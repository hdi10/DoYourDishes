package com.control.networkHttp;


import org.json.JSONObject;

import okhttp3.RequestBody;

interface HttpRequest {
    JSONObject GET(String path, RequestBody data, String token) throws Exception;

    JSONObject POST(String path, RequestBody data, String token) throws Exception;

    JSONObject DELETE(String path, RequestBody data, String token) throws Exception;
}
