package com.bhq.ius.domain.service;

import com.bhq.ius.domain.dto.DriverDto;
import com.bhq.ius.domain.dto.UserDto;
import com.bhq.ius.domain.dto.common.BaseResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ReportOneService {
    List<DriverDto> uploadFileXml(MultipartFile file);

    BaseResponseData<List<Long>> submitDriver(List<Long> listId);
    BaseResponseData<List<Long>> submitCourse(List<Long> listId);

}
