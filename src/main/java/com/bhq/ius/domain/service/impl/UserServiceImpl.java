package com.bhq.ius.domain.service.impl;

import com.bhq.ius.constant.RecordStatus;
import com.bhq.ius.domain.dto.UserDto;
import com.bhq.ius.domain.entity.User;
import com.bhq.ius.domain.repository.UserRepository;
import com.bhq.ius.domain.service.UserService;
import com.bhq.ius.domain.specification.GenericSpecificationBuilder;
import com.bhq.ius.domain.specification.criteria.SearchCriteria;
import com.bhq.ius.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;
    @Override
    public Page<UserDto> findBySearchParam(Optional<String> search, Pageable page) {
        GenericSpecificationBuilder<User> builder = new GenericSpecificationBuilder<>();
        // check chuỗi để tách các param search
        if (search.isPresent()) {
            Pattern pattern = Pattern.compile("(\\w+?)(\\.)(:|<|>|(\\w+?))(\\.)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search.get() + ",");
            while (matcher.find()) {
                builder.with(new SearchCriteria(matcher.group(1), matcher.group(3), matcher.group(6)));
            }
        }
        // specification
        builder.setClazz(User.class);
        Specification<User> spec = builder.build();
        Page<UserDto> listDTO = repository.findAll(spec, page).map(entity -> {
            UserDto dto = new UserDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
        return listDTO;
    }

    @Override
    public UserDto create(UserDto dto) {
        User entity = new User();
        String passwordEncrypt = new BCryptPasswordEncoder().encode(dto.getPassword());
        dto.setPassword(passwordEncrypt);
        dto.setStatus(RecordStatus.ACTIVE.name());
        BeanUtils.copyProperties(dto, entity);
        repository.save(entity);
        return dto;
    }

    @Override
    public UserDto update(UserDto dto) {
        Optional<User> entity = repository.findById(dto.getId());
        if(entity.isPresent()) {
            String passwordEncrypt = new BCryptPasswordEncoder().encode(dto.getPassword());
            dto.setPassword(passwordEncrypt);
            BeanUtils.copyProperties(dto, entity.get());
            repository.save(entity.get());
        }
        return dto;
    }

    @Override
    public void updateLoginTimeByUsername(String username) {
        User entity = repository.findByUsername(username);
        if (!DataUtil.isNullOrEmpty(entity)) {
            entity.setLastLoginTime(LocalDateTime.now());
            repository.save(entity);
        }

    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByListId(List<Long> listId) {
        repository.deleteAllById(listId);
    }

    @Override
    public UserDto findById(Long id) {
        UserDto dto = new UserDto();
        Optional<User> entity = repository.findById(id);
        entity.ifPresent(value -> BeanUtils.copyProperties(value, dto));
        return dto;
    }

    @Override
    public UserDto findByUsername(String username) {
        UserDto dto = new UserDto();
        User entity = repository.findByUsername(username);
        if(!DataUtil.isNullOrEmpty(entity)) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
