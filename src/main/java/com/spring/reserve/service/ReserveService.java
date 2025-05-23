package com.spring.reserve.service;


import com.spring.reserve.dto.ReserveDTO;
import com.spring.reserve.dto.ReserveTimeDTO;
import com.spring.reserve.dto.TimeDto;
import com.spring.reserve.entity.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface ReserveService {


    ReserveDTO reserveSave(ReserveDTO reserveDTO) throws IOException;

    ReserveDTO updateReserve(ReserveDTO reserveDTO);

    List<ReserveDTO> reserveList(ReserveDTO reserveDTO);

    ReserveDTO reserveDetail(Long id);

    List<ReserveTimeDTO> reserveTimeList(ReserveTimeDTO reserveTimeDTO);

    List<TimeDto> timeList(Map<String, String> params);

    ReserveDTO convertToDto(ReserveEntity reserveEntity);

    void deleteReserve(ReserveDTO reserveDTO) throws IOException;

}
