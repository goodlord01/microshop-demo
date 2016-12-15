package com.microshop.products;

import com.microshop.domain.Product;
import com.microshop.domain.ProductDTO;
import com.microshop.domain.TermDTO;
import com.microshop.file.FileService;
import com.microshop.utils.ObjectUtils;
import com.microshop.utils.ResourceInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Tuple;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by min on 21/11/2016.
 */


@Service
@Transactional
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    FileService fileService;

    public ProductService(){

    }

    public List<TermDTO> getProductServiceTerm(int productId){
        ResourceInputStream resourceInputStream = fileService.getFile("service_term/" + productId + "/term.json");
        try {
            return ObjectUtils.toObject(StreamUtils.copyToByteArray(resourceInputStream), List.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




    public Product getByProductId(Long productId){
        return productRepository.findOne(productId);
    }

    public List<ProductDTO> getAllProductBySellerId(String sellerOpenId){

        List<Tuple> tupleResult = productRepository.findAllProductBySellerId(sellerOpenId);
        List<ProductDTO> products = new ArrayList<ProductDTO>();
        for (Tuple t : tupleResult) {
            Long id = (Long) t.get(0);
            BigDecimal price = (BigDecimal) t.get(1);
            ProductDTO productDTO = new ProductDTO(id, price);
            products.add(productDTO);
        }

        return products;


    }

}
