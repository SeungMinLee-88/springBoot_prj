package com.spring.reserve.service.impl;

import com.spring.reserve.dto.ReserveDTO;
import com.spring.reserve.dto.ReserveTimeDTO;
import com.spring.reserve.dto.TimeDto;
import com.spring.reserve.entity.*;
import com.spring.reserve.repository.*;
import com.spring.reserve.service.ReserveService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService {
    private final ReserveRepository reserveRepository;
    private final UserRepository userRepository;
    private final HallRepository hallRepository;
    private final TimeRepository timeRepository;
    private final ReserveTimeRepository reserveTimeRepository;


    @Override
    public ReserveDTO reserveSave(ReserveDTO reserveDTO) throws IOException {

        System.out.println("reserveSave reserveDTO : " + reserveDTO);
        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.findByLoginId(reserveDTO.getReserveUserId()));
        Optional<HallEntity> optionalHallEntity = hallRepository.findById(reserveDTO.getHallId());
        if (optionalUserEntity.isPresent() && optionalHallEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            HallEntity hallEntity = optionalHallEntity.get();
            ReserveEntity reserveEntity = ReserveEntity.toSaveEntity(reserveDTO, userEntity, hallEntity);
            ReserveEntity reserveEntitys = reserveRepository.save(reserveEntity);

            //List<ReserveTimeDTO> timeDtoList = reserveDTO.getReserveTime();
            for(int i = 0; i < reserveDTO.getReserveTimeSave().size(); i++) {
                System.out.println("timeDtoList : " + reserveDTO.getReserveTimeSave().get(i));
                TimeEntity timeEntity = timeRepository.findById(reserveDTO.getReserveTimeSave() .get(i)).get();
                ReserveTimeEntity reserveTimeEntity = ReserveTimeEntity.toSaveEntity(reserveEntity, timeEntity,reserveDTO);
                ReserveTimeEntity reserveTimeEntitys = reserveTimeRepository.save(reserveTimeEntity);
            }
            ModelMapper mapper = new ModelMapper();

            System.out.println("reserveEntitys : " + reserveEntitys.toString());
            ReserveDTO reserveDTO1  = mapper.map(reserveEntitys, new TypeToken<ReserveDTO>(){}.getType());

            return reserveDTO1;

        } else {
            System.out.println("null case!!!!!!!!");
            return null;
        }
    }

    @Override
    public ReserveDTO updateReserve(ReserveDTO reserveDTO) {

        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.findByLoginId(reserveDTO.getReserveUserId()));
        Optional<HallEntity> optionalHallEntity = hallRepository.findById(reserveDTO.getHallId());
        if (optionalUserEntity.isPresent() && optionalHallEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            HallEntity hallEntity = optionalHallEntity.get();
            ReserveEntity reserveEntity = ReserveEntity.toSaveEntity(reserveDTO, userEntity, hallEntity);
            ReserveEntity reserveEntitys = reserveRepository.save(reserveEntity);

            //List<ReserveTimeDTO> timeDtoList = reserveDTO.getReserveTime();
            for(int i = 0; i < reserveDTO.getReserveTimeSave().size(); i++) {
                System.out.println("timeDtoList : " + reserveDTO.getReserveTimeSave().get(i));
                TimeEntity timeEntity = timeRepository.findById(reserveDTO.getReserveTimeSave() .get(i)).get();
                ReserveTimeEntity reserveTimeEntity = ReserveTimeEntity.toSaveEntity(reserveEntity, timeEntity,reserveDTO);
                ReserveTimeEntity reserveTimeEntitys = reserveTimeRepository.save(reserveTimeEntity);
            }
            ModelMapper mapper = new ModelMapper();

            System.out.println("reserveEntitys : " + reserveEntitys.toString());
            ReserveDTO reserveDTO1  = mapper.map(reserveEntitys, new TypeToken<ReserveDTO>(){}.getType());

            return reserveDTO1;

        } else {
            System.out.println("update null case!!!!!!!!");
            return null;
        }
    }

    @Override
    @Transactional
    public List<ReserveDTO> reserveList(ReserveDTO reserveDTO) {
        System.out.println("reserveList reserveDTO : " + reserveDTO);
        List<ReserveEntity> reserveEntityList = reserveRepository.findByReserveDateContainingAndReserveUserIdContaining(reserveDTO.getReserveDate(), reserveDTO.getReserveUserId());

        for(int i = 0; i < reserveEntityList.size(); i++) {
            System.out.println("reserveEntityList : " + reserveEntityList.get(i).getReserveTimeEntity().toString());
        }

        ModelMapper mapper = new ModelMapper();
        List<ReserveDTO> reserveDTOList  = mapper.map(reserveEntityList, new TypeToken<List<ReserveDTO>>(){}.getType());
        for(int i = 0; i < reserveDTOList.size(); i++) {
            System.out.println("getReserveTime : " + mapper.map(reserveEntityList.get(i).getReserveTimeEntity(), new TypeToken<List<ReserveTimeDTO>>(){}.getType()));
        }

        List<ReserveDTO> reserveDTOList2 = mapper.map(reserveEntityList, new TypeToken<List<ReserveDTO>>(){}.getType());

        return reserveDTOList2;
    }

    @Override
    @Transactional
    public ReserveDTO reserveDetail(Long id) {
        ModelMapper mapper = new ModelMapper();
        Optional<ReserveEntity> optionalReserveEntity = reserveRepository.findById(id);
        if (optionalReserveEntity.isPresent()) {
            ReserveEntity reserveEntity = optionalReserveEntity.get();
            ReserveDTO reserveDTO = mapper.map(reserveEntity, new TypeToken<ReserveDTO>(){}.getType());
            return reserveDTO;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public List<ReserveTimeDTO> reserveTimeList(ReserveTimeDTO reserveTimeDTO) {
        System.out.println("reserveTimeList reserveTimeDTO : " + reserveTimeDTO);
        List<ReserveTimeEntity> reserveTimeEntityList = reserveTimeRepository.findByReserveDate(reserveTimeDTO.getReserveDate());
        ModelMapper mapper = new ModelMapper();
        List<ReserveTimeDTO> reserveDTOList2 = mapper.map(reserveTimeEntityList, new TypeToken<List<ReserveTimeDTO>>(){}.getType());
        return reserveDTOList2;
    }

    @Override
    @Transactional
    public List<TimeDto> timeList(Map<String, String> params) {
        System.out.println("params.get : " + params.get("reserveDate"));
        System.out.println("reserveDate : " + params.get("reserveDate"));
        List<TimeEntity> timeEntityList = timeRepository.findByReserveDate(params.get("reserveDate"));
        ModelMapper mapper = new ModelMapper();
        List<TimeDto> timeDtoList = mapper.map(timeEntityList, new TypeToken<List<TimeDto>>(){}.getType());
        System.out.println("timeDtoList : " + timeDtoList);
        return timeDtoList;
    }

    @Override
    public ReserveDTO convertToDto(ReserveEntity reserveEntity) {
        ModelMapper mapper = new ModelMapper();
        ReserveDTO reserveDTO = mapper.map(reserveEntity, ReserveDTO.class);
        return reserveDTO;
    }

    @Override
    public void deleteReserve(ReserveDTO reserveDTO) throws IOException {
        reserveRepository.deleteById(reserveDTO.getId());
    }

}
