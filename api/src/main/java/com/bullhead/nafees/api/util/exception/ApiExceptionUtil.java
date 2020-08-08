package com.bullhead.nafees.api.util.exception;

import androidx.annotation.NonNull;

import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class ApiExceptionUtil {

    @NonNull
    public static Throwable loginException(@NonNull Throwable error) {
        if (error instanceof SocketTimeoutException) {
            return new NoInternetException();
        } else if (error instanceof HttpException) {
            int code = ((HttpException) error).code();
            switch (code) {
                case 403:
                    return new AccountExpiredException();
                case 401:
                    return new InvalidPasswordException();
                case 404:
                    return new NotFoundException();
            }
        }
        return error;
    }

    @NonNull
    public static Throwable generalException(@NonNull Throwable error) {
        if (error instanceof SocketTimeoutException) {
            return new NoInternetException();
        } else if (error instanceof HttpException) {
            int code = ((HttpException) error).code();
            switch (code) {
                case 404:
                    return new NotFoundException();
                case 401:
                    return new UnAuthorizedException();
            }
        }
        return error;
    }

}
