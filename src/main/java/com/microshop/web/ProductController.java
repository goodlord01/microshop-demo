package com.microshop.web;

import com.microshop.domain.Product;
import com.microshop.domain.ProductDTO;
import com.microshop.domain.TermDTO;
import com.microshop.products.ProductService;
import com.microshop.utils.AuthUtils;
import com.microshop.utils.YSIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by min on 18/10/2016.
 */

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    private static final String VIEW_INDEX = "index";
    @Autowired
    ProductService productService;

    @RequestMapping(value = "/seller/{sellerOpenId}" , method = RequestMethod.GET)
    public @ResponseBody List<ProductDTO> getBySellerId(@PathVariable(value="sellerOpenId")String sellerOpenId) {
        List<ProductDTO> products = productService.getAllProductBySellerId(sellerOpenId);
        return products;
    }

    @RequestMapping(value = "/{productId}" , method = RequestMethod.GET)
    public @ResponseBody Product getProduct(@PathVariable(value="productId")String productId,
                                            HttpServletRequest request, HttpServletResponse response) {
        Product product = productService.getByProductId(Long.parseLong(productId));

        AuthUtils.setYSID(request, response);

        return product;
    }

    @RequestMapping(value = "/{id}/term" , method = RequestMethod.GET)
    public List<TermDTO> getAllProduct(@PathVariable(value="id")int productId) {
        return productService.getProductServiceTerm(productId);
    }




}
