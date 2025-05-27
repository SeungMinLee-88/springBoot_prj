package com.spring.reserve.controller;

import com.spring.reserve.dto.BoardDTO;
import com.spring.reserve.dto.BoardFileDTO;
import com.spring.reserve.dto.BoardPostResponse;
import com.spring.reserve.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class RestBoardController {
    private final BoardService boardService;

    @PostMapping("/boardSave")
    public ResponseEntity<BoardPostResponse> boardSave(@RequestParam("boardTitle") String boardTitle, @RequestParam("boardWriter") String boardWriter, @RequestParam("boardContents") String boardContents, @RequestParam(name="boardFile", required = false) MultipartFile[] boardFile) throws IOException {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardTitle(boardTitle);
        boardDTO.setBoardWriter(boardWriter);
        boardDTO.setBoardContents(boardContents);
        boardDTO.setFileList(boardFile);
        boardService.boardSaveAtta(boardDTO);

        return ResponseEntity.ok(BoardPostResponse
                .builder()
                .resultMessage("save success")
                .resultCode("200")
                .id(1L)
                .build());
    }


    @GetMapping("/list")
    public List<BoardDTO> findAll(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();

        return boardDTOList;
    }

    @GetMapping("/detail/{id}")
    public BoardDTO boardDetail(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.boardDetail(id);

        return boardDTO;
    }

    @GetMapping("/fileList/{boardId}")
    public List<BoardFileDTO> fileList(@PathVariable Long boardId, HttpServletRequest request) {
            List<BoardFileDTO> boardFileDTOList = boardService.fileList(boardId);

            return boardFileDTOList;
    }

    @GetMapping("/fileDelete/{fileId}&{boardId}")
    public List<BoardFileDTO> fileDelete(@PathVariable Long fileId, @PathVariable Long boardId) {
        List<BoardFileDTO> boardFileDTOList = boardService.fileDelete(fileId, boardId);

        return boardFileDTOList;
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Object> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        try {
            Resource resource = this.boardService.fetchFileAsResource(fileName);
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (IOException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/update/{id}")
    public BoardDTO updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.boardDetail(id);
        model.addAttribute("boardUpdate", boardDTO);
        return boardService.boardDetail(id);
    }

    @PostMapping("/updateBoard")
    public ResponseEntity<BoardPostResponse> updateBoard(@RequestParam("boardId") Long id,
            @RequestParam("boardTitle") String boardTitle, @RequestParam("boardWriter") String boardWriter, @RequestParam("boardContents") String boardContents, @RequestParam(name="boardFile", required = false) MultipartFile[] boardFile) throws IOException {

        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(id);
        boardDTO.setBoardTitle(boardTitle);
        boardDTO.setBoardWriter(boardWriter);
        boardDTO.setBoardContents(boardContents);
        boardDTO.setFileList(boardFile);

        boardService.updateBoard(boardDTO);

        return ResponseEntity.ok(BoardPostResponse
                .builder()
                .resultMessage("update success")
                .resultCode("200")
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> boardDelete(@PathVariable Long id) {
        boardService.boardDelete(id);

        return ResponseEntity.status(HttpStatus.OK).body("delete success");
    }

    @GetMapping("/boardList")
    public Page<BoardDTO> boardList(@PageableDefault(page = 1) Pageable pageable, @RequestParam Map<String,String> params){
        Page<BoardDTO> boardList = boardService.boardList(pageable, params);

        return boardList;
    }

}










