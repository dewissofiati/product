package com.tdi.northwind.config;

import com.tdi.northwind.model.DataTableRequest;
import com.tdi.northwind.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //untuk mendapatkan list product
    public List<Product> getListProduct(DataTableRequest request){
        Map<String, Object> map = (Map<String,Object>) request.getExtraParam();
        String query = "SELECT products.product_id, products.product_name, categories.category_name" +
                " FROM public.products, public.categories" +
                " WHERE products.product_name ilike concat('%', :nama, '%')" +
                " AND categories.category_id = products.category_id" +
                " ORDER BY " + (request.getSortCol()+1) + "" + request.getSortDir() + " limit :limit offset :offset";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("limit", request.getLength());
        paramMap.addValue("offset", request.getStart());
        paramMap.addValue("nama", map.get("nama"));
        return this.namedParameterJdbcTemplate.query(query.toString(), paramMap, (rs, i) -> {
            Product product = new Product();
            product.setProductId(rs.getInt("product_id"));
            product.setProductName(rs.getString("product_name"));
//           product.setKategori(rs.getInt("category_id"));
           return product;
        });
    }

    //untuk menghitung banyaknya data entry pas di tampilan web
    public Long countProductAll(DataTableRequest request){
        Map<String, Object> map = (Map<String, Object>) request.getExtraParam();
        String query = "SELECT count(product_id) as banyak" +
                " FROM public.products" +
                " WHERE product_name ilike concat('%', :nama, '%')";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("nama", map.get("nama"));
        return this.namedParameterJdbcTemplate.queryForObject(query, paramMap, Long.class);
    }

//    untuk mendapatkan id
    public Product getProductById(Integer id){
        String query = "SELECT product_id, product_name" +
                " FROM public.products" +
                " WHERE product_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("id", id);
        return (Product) this.namedParameterJdbcTemplate.queryForObject(query, paramMap, new BeanPropertyRowMapper(Product.class));
    }

    //untuk menyimpan product
    public Integer saveProduct(Product product){
        String query = "INSERT INTO public.products (product_id, product_name)" +
                " VALUES (:product_id, :product_name)";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("product_id", product.getProductId());
        paramMap.addValue("product_name", product.getProductName());
        return this.namedParameterJdbcTemplate.update(query, paramMap);
    }

    //update product
    public Integer updateProduct(Product product){
        String query = "UPDATE public.products set product_name= :product_name" +
                " WHERE product_id= :product_id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("product_id", product.getProductId());
        paramMap.addValue("product_name", product.getProductName());
        return this.namedParameterJdbcTemplate.update(query, paramMap);
    }
}
