package com.chen.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色表(Role)实体类
 */
@TableName(value="t_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role implements Serializable {
    private static final long serialVersionUID = -54979041104113736L;
    @TableId
    private Long id;
    String  roleCode;         //路由路径
    String  roleName;        //组件路径
    boolean enabled;     //是否启用
}
