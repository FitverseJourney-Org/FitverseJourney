package com.example.data

enum class HttpUrlStrings(val url: String){
    URL_LOGIN("/login"),
    URL_REGISTER("/register"),
    URL_RESET_PASSWORD("http://10.0.2.2:8080/reset-password"),

    URL_GET_TOKEN("http://10.0.2.2:8080/get-token"),

}