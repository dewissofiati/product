package com.tdi.northwind;

import com.tdi.northwind.config.ProductRepository;
import com.tdi.northwind.model.DataTableRequest;
import com.tdi.northwind.model.DataTablesResponse;
import com.tdi.northwind.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import util.Utility;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/listproduct", method = {RequestMethod.POST, RequestMethod.GET})
    public DataTablesResponse listProduct(HttpServletRequest request){
        String nama = request.getParameter("nama");
        Integer offset = Utility.tryParse(request.getParameter("start"));
        Integer limit = Utility.tryParse(request.getParameter("length"));
        Integer draw = Utility.tryParse(request.getParameter("draw"));
        Integer sortCol = Utility.tryParse(request.getParameter("order[0][column]"));
        String sortDir = request.getParameter("order[0][dir]");
        DataTableRequest dataTableRequest = new DataTableRequest();
        dataTableRequest.setStart(limit);
        dataTableRequest.setLength(offset);
        dataTableRequest.setDraw(draw);
        dataTableRequest.setSortCol(sortCol);
        dataTableRequest.setSortDir(sortDir);

        Map<String, Object> extraParam = new HashMap<>();
        extraParam.put("nama", nama);
        dataTableRequest.setExtraParam(extraParam);

        DataTablesResponse dataTablesResponse = new DataTablesResponse();
        dataTablesResponse.setData(productRepository.getListProduct(dataTableRequest));
        Long total = productRepository.countProductAll(dataTableRequest);
        dataTablesResponse.setRecordsFiltered(total);
        dataTablesResponse.setRecordsTotal(total);
        dataTablesResponse.setDraw(draw);
        return dataTablesResponse;
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/saveproduct", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Map<String, Object>> saveProduct(@RequestBody Product product){
        productRepository.saveProduct(product);
        Map<String, Object> info = new HashMap<>();
        info.put("pesan", "Saved");
        return ResponseEntity.status(HttpStatus.OK).body(info);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/getproductbyid/{id}", method = {RequestMethod.GET})
    public ResponseEntity<Product> getProductById(@PathVariable("id") Integer id){
        return ResponseEntity.status(HttpStatus.OK). body(productRepository.getProductById(id));
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/updateproduct", method = {RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> updateProduct(@RequestBody Product product){
        productRepository.updateProduct(product);
        Map<String, Object> info = new HashMap<>();
        info.put("pesan", "Updated");
        return ResponseEntity.status(HttpStatus.OK). body(info);
    }
}
