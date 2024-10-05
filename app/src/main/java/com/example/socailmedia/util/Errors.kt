package com.example.socailmedia.util

enum class AuthErrors : Error {
    UN_KNOWN,
    EMAIL_IS_WRONG,
    PASSWORD_IS_WRONG,
    INTERNAL_SERVER_ERROR,
    BAD_REQUEST
}

enum class PasswordError : Error {
    EMPTY,
    TOO_SHORT,
    NO_UPPERCASE,
    NO_DIGIT
}

enum class EmailError : Error {
    EMPTY,
    NOT_MATCH_PATTERN,
    EXIST
}

enum class CommonError : Error {
    EMPTY,
    NOT_MATCH_PATTERN
}
enum class GlobalError:Error{
    UN_KNOWN,
    BAD_REQUEST,
    INTERNAL_SERVER_ERROR
}
enum class ChatError:Error{
    UserALLREADYEXIST,
    UNKNOWN,
}