package com.microshop.web;

import com.microshop.domain.ThumbsDTO;
import com.microshop.thumbs.ThumbsService;
import com.microshop.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping(value = "/thumbs")
@RestController
public class ThumbsController {

    @Autowired
    ThumbsService thumbsService;

    @RequestMapping(value = "/{sellerOpenId}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ThumbsDTO> getAllThumbs(@PathVariable(value = "sellerOpenId") String sellerOpenId) {
        List<ThumbsDTO> thumbsDTOList = thumbsService.getAllThumbsDTOBySellerOpenId(sellerOpenId);
        return thumbsDTOList;
    }

    @RequestMapping(value = "/count/{sellerOpenId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Long getAllThumbsCount(@PathVariable(value = "sellerOpenId") String sellerOpenId) {
        Long count = thumbsService.getAllCountBySellerOpenId(sellerOpenId);
        return count;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createThumbs(@RequestBody ThumbsDTO thumbsDTO) throws Exception {

        if (thumbsService.checkIfExists(thumbsDTO))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        ThumbsDTO thumbs = thumbsService.createThumbs(thumbsDTO);
        return new ResponseEntity<>(thumbs, HttpStatus.OK);
    }

    @RequestMapping(value = "/{sellerOpenId}/checkexist", method = RequestMethod.GET)
    public ResponseEntity<?> checkIfExist(HttpServletRequest request) throws Exception {
        String openId = AuthUtils.getOpenId(request);
        boolean exist = false;
        if (openId != null) {
            ThumbsDTO thumbsDTO = new ThumbsDTO();
            thumbsDTO.setOpenid(openId);
            exist = thumbsService.checkIfExists(thumbsDTO);
        }
        return new ResponseEntity<>(exist, HttpStatus.OK);
    }
}
