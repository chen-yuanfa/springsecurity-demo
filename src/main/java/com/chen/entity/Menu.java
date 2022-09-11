package com.chen.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单表(Menu)实体类
 */
@TableName(value="t_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu implements Serializable {
    private static final long serialVersionUID = -54979041104113736L;
    
    @TableId
    private Long id;
    String  url;         //路由路径
    String  path;        //组件路径
    String  component;   //组件名
    String  name;        //菜单名
    String  iconCls;     //图标
    boolean keepAlive;  //是否保持激活
    boolean requireAuth; //是否要求权限
    boolean visible;    //是否显示菜单
    Long    parentId;    //父id
    boolean enabled;     //是否启用

    List<Role> roles;

}