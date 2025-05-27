package com.spring.reserve.controller;

import com.spring.reserve.dto.*;
import com.spring.reserve.entity.ReserveEntity;
import com.spring.reserve.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reserve")
public class RestReserveController {

    private final ReserveService reserveService;

    @PutMapping("/save")
    public ResponseEntity<ReserveEntity> reserveSave(@RequestBody ReserveDTO reserveDTO) {

        ReserveDTO reserveDTO1 = reserveService.reserveSave(reserveDTO);
        return ResponseEntity.ok(ReserveEntity.builder().id(reserveDTO1.getId()).build());
    }

    @PostMapping("/update")
    public ResponseEntity<ReserveEntity> updateReserve(@RequestBody ReserveDTO reserveDTO) {

        ReserveDTO reserveDTO1 = reserveService.updateReserve(reserveDTO);
        return ResponseEntity.ok(ReserveEntity.builder().id(reserveDTO1.getId()).build());
    }

    @GetMapping("/reserveList")
    public List<ReserveDTO> reserveList(@RequestParam Map<String, String> params) {

        ReserveDTO reserveDTO = new ReserveDTO();
        reserveDTO.setReserveDate(params.get("reserveDate"));
        reserveDTO.setReserveUserId(params.get("reserveUserId"));

        List<ReserveDTO> reserveDTOList = reserveService.reserveList(reserveDTO);

        return reserveDTOList;
    }

    @GetMapping("reserveDetail/{id}")
    public ReserveDTO reserveDetail(@PathVariable Long id, Model model) {
        ReserveDTO reserveDTO = new ReserveDTO();
        reserveDTO = reserveService.reserveDetail(id);

        return reserveDTO;
    }

    @GetMapping("/reserveTimeList")
    public List<ReserveTimeDTO> reserveTimeList(@RequestParam Map<String, String> params) {
     ReserveTimeDTO reserveTimeDTO = new ReserveTimeDTO();
        reserveTimeDTO.setReserveDate(params.get("reserveDate"));
        List<ReserveTimeDTO> reserveTimeDTOList = reserveService.reserveTimeList(reserveTimeDTO);

        return reserveTimeDTOList;
    }

    @GetMapping("/timeList")
    public List<TimeDto> timeList(@RequestParam Map<String, String> params) {
        List<TimeDto> timeDtoList = reserveService.timeList(params);

        return timeDtoList;
    }

    @DeleteMapping("/deleteReserve")
    public ResponseEntity<ReserveDTO> deleteReserve(@RequestBody ReserveDTO reserveDTO){
        reserveService.deleteReserve(reserveDTO);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reserveDTO);
    }

}
