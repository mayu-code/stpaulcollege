package com.main.stpaul.mapper.requestMapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.main.stpaul.dto.request.RegisterRequest;
import com.main.stpaul.entities.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRequestMapper {
    User toUser(RegisterRequest register);
}
