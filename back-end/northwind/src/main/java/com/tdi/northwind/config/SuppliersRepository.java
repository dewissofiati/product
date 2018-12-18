package com.tdi.northwind.config;

import com.tdi.northwind.model.DataTableRequest;
import com.tdi.northwind.model.Suppliers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SuppliersRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    public List<Suppliers> getListSuppliers(DataTableRequest req) {
        Map<String, Object> map = (Map<String, Object>) req.getExtraParam();
        String sql = " SELECT supplier_id, company_name, contact_name, address" +
                " FROM public.suppliers" +
                " where category_name ilike concat('%',:nama,'%')" +
                " order by " + (req.getSortCol() + 1) + "" + req.getSortDir() + " limit :limit offset :offset ";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("limit", req.getLength());
        paramMap.addValue("offset", req.getStart());
        paramMap.addValue("nama", map.get("nama"));
        return this.namedJdbcTemplate.query(sql.toString(), paramMap, (rs, i) -> {
            Suppliers suppliers = new Suppliers();
            suppliers.setSupplierId(rs.getInt("supplier_id"));
            suppliers.setCompanyName(rs.getString("company_name"));
            suppliers.setContactName(rs.getString("contact_name"));
            suppliers.setAddress(rs.getString("address"));
            return suppliers;
        });

    }

    //untuk menghitung banyaknya data entry pas di tampilan web
    public Long counrSuppliersAll(DataTableRequest req) {
        Map<String, Object> map = (Map<String, Object>) req.getExtraParam();
        String query = " SELECT count(supplier_id) as banyak FROM public.suppliers " +
                "where company_name ilike concat('%',:nama,'%')";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("nama", map.get("nama"));
        return this.namedJdbcTemplate.queryForObject(query, paramMap, Long.class);
    }

    //untuk mendapatkan id nya
    public Suppliers getSuppliersById(Integer id) {
        String query = "SELECT supplier_id, company_name, contact_name, address" +
                " FROM public.suppliers " +
                "where supplier_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("id", id);
        return (Suppliers) this.namedJdbcTemplate.queryForObject(query, paramMap, new BeanPropertyRowMapper(Suppliers.class));
    }

    public Integer simpanSuppliers(Suppliers suppliers) {
        String query = " INSERT INTO public.suppliers (supplier_id, company_name, contact_name, address) " +
                "values (:supplier_id,:company_name,:contact_name,:address)";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("supplier_id", suppliers.getSupplierId());
        paramMap.addValue("company_name", suppliers.getCompanyName());
        paramMap.addValue("contact_name", suppliers.getContactName());
        paramMap.addValue("address", suppliers.getAddress());
        return this.namedJdbcTemplate.update(query, paramMap);
    }

    public Integer updateSuppliers(Suppliers suppliers) {
        String query = " UPDATE public.suppliers set company_name=:company_name, contact_name=:contact_name, address=:address " +
                "where supplier_id=:supplier_id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("supplier_id", suppliers.getSupplierId());
        paramMap.addValue("company_name", suppliers.getCompanyName());
        paramMap.addValue("contact_name", suppliers.getContactName());
        paramMap.addValue("address", suppliers.getAddress());
        return this.namedJdbcTemplate.update(query, paramMap);
    }
}
