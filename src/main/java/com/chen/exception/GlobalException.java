package com.chen.exception;

import com.chen.entity.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 *
 * @author bing  @create 2021/1/15-上午10:46
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SQLException.class)
    public ResponseResult mySqlException(SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return ResponseResult.error("该数据有关联数据，操作失败！");
        }
        return ResponseResult.error("数据库异常，操作失败！");
    }
}
