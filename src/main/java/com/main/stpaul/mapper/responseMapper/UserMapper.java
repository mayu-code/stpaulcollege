package com.main.stpaul.mapper.responseMapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.main.stpaul.dto.response.UserResponse;
import com.main.stpaul.entities.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponse toUserResponse(User user);
    
}
