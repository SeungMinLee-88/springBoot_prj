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
    public ResponseEntity<ReserveEntity> reserveSave(@RequestBody ReserveDTO reserveDTO) throws IOException {
        System.out.println("reserveDTO = " + reserveDTO);
        /*List<TimeDto> timeDtoList = reserveDTO.getReserveTime();*/
        ReserveDTO reserveDTO1 = reserveService.reserveSave(reserveDTO);
        return ResponseEntity.ok(ReserveEntity.builder().id(reserveDTO1.getId()).build());
    }

    @PostMapping("/update")
    public ResponseEntity<ReserveEntity> updateReserve(@RequestBody ReserveDTO reserveDTO) {
        System.out.println("update reserveDTO = " + reserveDTO);
        ReserveDTO reserveDTO1 = reserveService.updateReserve(reserveDTO);
        return ResponseEntity.ok(ReserveEntity.builder().id(reserveDTO1.getId()).build());
    }

    @GetMapping("/reserveList")
    public List<ReserveDTO> reserveList(@RequestParam Map<String, String> params) {
        /*StringBuilder sb = new StringBuilder();*/
        params.entrySet().forEach(map -> {
            /*sb.append(map.getKey() + " : " + map.getValue() + "\n");*/
            System.out.println("map : " + map.getKey() + " : " + map.getValue() + "\n");
        });

        ReserveDTO reserveDTO = new ReserveDTO();
        reserveDTO.setReserveDate(params.get("reserveDate"));
        reserveDTO.setReserveUserId(params.get("reserveUserId"));
        System.out.println("reserveList reserveDTO : " + reserveDTO.toString());
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<ReserveDTO> reserveDTOList = reserveService.reserveList(reserveDTO);
        System.out.println("reserveDTOList : " + reserveDTOList.toString());
        //model.addAttribute("boardList", boardDTOList);
        return reserveDTOList;
    }

    @GetMapping("reserveDetail/{id}")
    public ReserveDTO reserveDetail(@PathVariable Long id, Model model) {
        /*StringBuilder sb = new StringBuilder();*/

        ReserveDTO reserveDTO = new ReserveDTO();
        System.out.println("reserveDetail reserveDTO : " + reserveDTO.toString());
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        reserveDTO = reserveService.reserveDetail(id);
        System.out.println("reserveDTO : " + reserveDTO.toString());
        //model.addAttribute("boardList", boardDTOList);
        return reserveDTO;
    }

    @GetMapping("/reserveTimeList")
    public List<ReserveTimeDTO> reserveTimeList(@RequestParam Map<String, String> params) {
        /*StringBuilder sb = new StringBuilder();*/

        ReserveTimeDTO reserveTimeDTO = new ReserveTimeDTO();
        reserveTimeDTO.setReserveDate(params.get("reserveDate"));
        System.out.println("reserveList reserveDTO : " + reserveTimeDTO.toString());
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<ReserveTimeDTO> reserveTimeDTOList = reserveService.reserveTimeList(reserveTimeDTO);
        System.out.println("reserveDTOList : " + reserveTimeDTOList.toString());
        //model.addAttribute("boardList", boardDTOList);
        return reserveTimeDTOList;
    }

    @GetMapping("/timeList")
    public List<TimeDto> timeList(@RequestParam Map<String, String> params) {
        /*StringBuilder sb = new StringBuilder();*/

        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<TimeDto> timeDtoList = reserveService.timeList(params);
        System.out.println("reserveDTOList : " + timeDtoList.toString());
        //model.addAttribute("boardList", boardDTOList);
        return timeDtoList;
    }

    @DeleteMapping("/deleteReserve")
    public ResponseEntity<ReserveDTO> deleteReserve(@RequestBody ReserveDTO reserveDTO) throws IOException {
        System.out.println("reserveDTO = " + reserveDTO);
        reserveService.deleteReserve(reserveDTO);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reserveDTO);
    }


/*    @DeleteMapping("/deletetime")
    public ResponseEntity<TimeDto> deletetime(@RequestBody TimeDto timeDto) throws IOException {
        System.out.println("reserveDTO = " + timeDto);
        reserveService.deletetime(timeDto);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(timeDto);
    }*/

}
