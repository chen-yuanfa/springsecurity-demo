package com.chen.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    public List<Role> getRoleListByUserId(@Param("userId") Long userId);
}
