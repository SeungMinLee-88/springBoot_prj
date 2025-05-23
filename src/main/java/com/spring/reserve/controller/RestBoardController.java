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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class RestBoardController {
    private final BoardService boardService;

    //https://green-bin.tistory.com/44
    @PostMapping("/boardSave")
    public ResponseEntity<BoardPostResponse> boardSave(@RequestParam("boardTitle") String boardTitle, @RequestParam("boardWriter") String boardWriter, @RequestParam("boardContents") String boardContents, @RequestParam(name="boardFile", required = false) MultipartFile[] boardFile) throws IOException {
        System.out.println("boardTitle = " + boardTitle);
        System.out.println("boardFile = " + boardFile);

        LocalDateTime time = LocalDateTime.now();
        System.out.println("time = " + time);
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardTitle(boardTitle);
        boardDTO.setBoardWriter(boardWriter);
        boardDTO.setBoardContents(boardContents);
/*        boardDTO.setBoardPass(boardPass);*/
        boardDTO.setFileList(boardFile);
        boardService.boardSaveAtta(boardDTO);
        /*for (MultipartFile boardFiles : boardDTO.getFileList()) {
            //MultipartFile boardFile = boardDTO.getBoardFile(); // 1.
            String originalFilename = boardFiles.getOriginalFilename(); // 2.
            System.out.println("originalFilename : " + originalFilename);
        }*/

        return ResponseEntity.ok(BoardPostResponse
                .builder()
                .resultMessage("save success")
                .resultCode("200")
                .id(1L)
                .build());
    }


    @GetMapping("/list")
    public List<BoardDTO> findAll(Model model) {
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDTO> boardDTOList = boardService.findAll();
        System.out.println("boardDTOList : " + boardDTOList.toString());
        //model.addAttribute("boardList", boardDTOList);
        return boardDTOList;
    }

    @GetMapping("/detail/{id}")
    public BoardDTO boardDetail(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {
        System.out.println("call boardDetail");
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.boardDetail(id);
        /* 댓글 목록 가져오기 */
        /*List<CommentDTO> commentDTOList = commentServiceBak2.findAll(id);
        model.addAttribute("commentList", commentDTOList);*/
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        System.out.println("return boardDTO : " + boardDTO);
        return boardDTO;
    }

    @GetMapping("/fileList/{boardId}")
    public List<BoardFileDTO> fileList(@PathVariable Long boardId, HttpServletRequest request) {

            List<BoardFileDTO> boardFileDTOList = boardService.fileList(boardId);
            return boardFileDTOList;
    }

    @GetMapping("/fileDelete/{fileId}&{boardId}")
    public List<BoardFileDTO> fileDelete(@PathVariable Long fileId, @PathVariable Long boardId) {
        System.out.println("fileDelete fileId : " + fileId);
        System.out.println("fileDelete boardId : " + boardId);

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
            @RequestParam("boardTitle") String boardTitle, @RequestParam("boardWriter") String boardWriter, @RequestParam("boardContents") String boardContents, @RequestParam(name="boardFile", required = false) MultipartFile[] boardFile) {

        System.out.println("boardTitle = " + boardTitle);
        System.out.println("boardFile = " + boardFile);

        LocalDateTime time = LocalDateTime.now();
        System.out.println("time = " + time);
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(id);
        boardDTO.setBoardTitle(boardTitle);
        boardDTO.setBoardWriter(boardWriter);
        boardDTO.setBoardContents(boardContents);
/*        boardDTO.setBoardPass(boardPass);*/
        boardDTO.setFileList(boardFile);

        try {
            /*BoardDTO board = boardService.updateBoard(boardDTO);*/
            boardService.updateBoard(boardDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(BoardPostResponse
                .builder()
                .resultMessage("update success")
                .resultCode("200")
                .build());
//        return "redirect:/board/" + boardDTO.getId();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> boardDelete(@PathVariable Long id) {

        try {
            boardService.boardDelete(id);
        }catch (Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.OK).body("delete success");
    }

    /*@PostMapping("/pagingList")
    public Page<BoardDTO> paging(@PageableDefault(page = 1) Pageable pageable, Model model, @RequestBody RequestParameters requestParameters){
        System.out.println("call pagingList");
        //pageable.getPageNumber();
        *//*
{
  "sortfield": "board_title",
  "searchfield": "board_title",
  "searchtext": "11"
}
        *//*
        System.out.println("reuestparam : " + requestParameters.getSortfield());
        String sortfield ="";
        String searchfield ="";
        String searchtext ="";

        Page<BoardDTO> boardList = boardService.paging(pageable, requestParameters);
        int blockLimit = 3;

        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();
*//*        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);*//*
        return boardList;
    }*/


    @GetMapping("/boardList")
    public Page<BoardDTO> boardList(@PageableDefault(page = 1) Pageable pageable, @RequestParam Map<String,String> params){

        System.out.println("GetMapping pageable.getSort() : " + pageable.getSort());
        System.out.println("GetMapping pageable : " + pageable.toString());
        System.out.println("paging params : " + params.toString());

        Page<BoardDTO> boardList = boardService.pagingList(pageable, params);
        int blockLimit = 3;

       /*{
          "searchKey": "boardTitle",
          "searchValue": "",
          "sortfield": "createdTime",
          "sortdirection": "desc"
        }*/
        System.out.println("pageable.getPageNumber() : " + pageable.getPageNumber());
        System.out.println("boardList.getTotalPages() : " + boardList.getTotalPages());
        System.out.println("pageable.getPageSize() : " + pageable.getPageSize());

        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / boardList.getTotalPages()))) - 1) * boardList.getTotalPages() + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + pageable.getPageSize() - 1) < boardList.getTotalPages()) ? startPage + pageable.getPageSize() - 1 : boardList.getTotalPages();

        System.out.println("va11 : " + (double)pageable.getPageNumber() / boardList.getTotalPages());
        System.out.println("va22 : " + Math.ceil((double)pageable.getPageNumber() / boardList.getTotalPages()));

        System.out.println("(((int)(Math.ceil((double)pageable.getPageNumber() / boardList.getTotalPages()))) - 1) : " + (((int)(Math.ceil((double)pageable.getPageNumber() / boardList.getTotalPages()))) - 1));

        System.out.println("startPage : " + startPage);
        System.out.println("endPage : " + endPage);

/*        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);*/
        return boardList;
    }

}










