package com.microshop.thumbs;

import com.microshop.domain.Thumbs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by min on 25/10/2016.
 */

public interface ThumbsRepository extends JpaRepository<Thumbs, String> {

    Long countBySellersOpenid(String sellersOpenid);

    List<Thumbs> findBySellersOpenidOrderByCreatedDatetimeDesc(String sellersOpenid);

    List<Thumbs> findByOpenid(String openid);
}
