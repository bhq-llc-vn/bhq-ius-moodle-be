package com.bhq.ius.domain.service;

import com.bhq.ius.domain.dto.DriverDto;
import com.bhq.ius.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    Page<DriverDto> findBySearchParam(Optional<String> search, Pageable page);
    DriverDto create(DriverDto dto);
    DriverDto update(DriverDto dto);
    void deleteById(Long id);
    void deleteByListId(List<Long> listId);
    DriverDto findById(Long id);


}