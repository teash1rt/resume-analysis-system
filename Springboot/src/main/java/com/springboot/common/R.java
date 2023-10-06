package com.springboot.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class R<T> {
    private static final Integer success_code = 200;
    private static final Integer error_code = 400;
    private static final Integer custom_error_code = 401;
    private static final Integer token_error_code = 402;
    private static final Integer exception_code = 510;

    private Integer code;
    private String msg;
    private T data;

    public static <T> R<T> success(String msg, T data) {
        return new R<>(success_code, msg, data);
    }

    public static <T> R<T> success(String msg) {
        return new R<>(success_code, msg, null);
    }

    public static <T> R<T> error(String msg) {
        return new R<>(error_code, msg, null);
    }

    public static <T> R<T> permission_error(String msg) {
        return new R<>(token_error_code, msg, null);
    }

    public static <T> R<T> exception(String msg) {
        return new R<>(exception_code, msg, null);
    }
}
