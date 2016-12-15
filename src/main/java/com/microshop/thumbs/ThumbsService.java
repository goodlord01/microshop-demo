package com.microshop.thumbs;

import com.microshop.customers.CustomerRepository;
import com.microshop.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by min on 23/10/2016.
 */

@Service
@Transactional
public class ThumbsService {

    private static final Logger logger = LoggerFactory.getLogger(ThumbsService.class);

    @Autowired
    ThumbsRepository thumbsRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Boolean checkIfExists(ThumbsDTO thumbsDTO) {
        if (thumbsDTO == null || thumbsDTO.getOpenid() == null)
            return false;

        List<Thumbs> thumbsList = thumbsRepository.findByOpenid(thumbsDTO.getOpenid());

        if (thumbsList != null && thumbsList.size() > 0)
            return true;

        return false;
    }

    public ThumbsDTO createThumbs(ThumbsDTO thumbsDTO) {

        if (checkIfExists(thumbsDTO))
            return null;

        Thumbs thumbs = toThumbs(thumbsDTO);

        Thumbs reault = thumbsRepository.save(thumbs);
        return toThumbsDTO(reault, null);
    }

    public List<ThumbsDTO> getAllThumbsDTOBySellerOpenId(String sellerOpenId) {

        List<Thumbs> thumbsList = thumbsRepository.findBySellersOpenidOrderByCreatedDatetimeDesc(sellerOpenId);
        List<Customer> customers = customerRepository.findAll(thumbsList.stream().map(item->item.getOpenid())
                .collect(Collectors.toList()));

        List<ThumbsDTO> thumbsDTOList = new ArrayList<>();
        for (Thumbs thumbs : thumbsList) {

            Optional<Customer> customer = customers.stream().filter(cust -> cust.getOpenId().equals(thumbs.getOpenid())).findFirst();
            ThumbsDTO thumbsDTO;
            if(customer.isPresent()) {
                thumbsDTO = toThumbsDTO(thumbs, customer.get());
            }
            else
                thumbsDTO = toThumbsDTO(thumbs, null);

            thumbsDTOList.add(thumbsDTO);
        }

        return thumbsDTOList;
    }

    public Long getAllCountBySellerOpenId(String sellerOpenId) {

        return thumbsRepository.countBySellersOpenid(sellerOpenId);
    }

    private Thumbs toThumbs(ThumbsDTO thumbsDTO) {
        if (thumbsDTO == null)
            return null;

        Thumbs thumbs = new Thumbs();
        thumbs.setCreatedDatetime(thumbsDTO.getCreatedDatetime());
        thumbs.setSellersId(thumbsDTO.getSellersId());
        thumbs.setSellersOpenid(thumbsDTO.getSellersOpenid());
        thumbs.setOpenid(thumbsDTO.getOpenid());

        return thumbs;
    }

    private ThumbsDTO toThumbsDTO(Thumbs thumbs, Customer customer) {
        if (thumbs == null)
            return null;

        ThumbsDTO thumbsDTO = new ThumbsDTO();
        thumbsDTO.setId(thumbs.getId());
        thumbsDTO.setSellersId(thumbs.getSellersId());
        thumbsDTO.setSellersOpenid(thumbs.getSellersOpenid());
        thumbsDTO.setOpenid(thumbs.getOpenid());
        thumbsDTO.setCreatedDatetime(thumbs.getCreatedDatetime());
        if(customer != null) {
            thumbsDTO.setAge(customer.getAge());
            thumbsDTO.setProvince(customer.getProvince());
            thumbsDTO.setCity(customer.getCity());
            thumbsDTO.setAddress(customer.getAddress());
            thumbsDTO.setName(customer.getNickname());
            thumbsDTO.setGravatarUrl(customer.getGravatarUrl());
            thumbsDTO.setSex(customer.getSex());
        }
        return thumbsDTO;
    }
}
