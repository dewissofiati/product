package com.tdi.northwind.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tdi.northwind.model.DataTableRequest;
import com.tdi.northwind.model.Kategori;



@Repository
public class KategoriRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	public List<Kategori> getListKategori(DataTableRequest req) {
		Map<String, Object> map = (Map<String, Object>) req.getExtraParam();
		String sql = " SELECT category_id, category_name, description FROM public.categories " +
				"where category_name ilike concat('%',:nama,'%')" +
				" order by "+(req.getSortCol()+1)+""+req.getSortDir()+" limit :limit offset :offset ";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("limit", req.getLength());
		paramMap.addValue("offset", req.getStart());
		paramMap.addValue("nama", map.get("nama"));
		return this.namedJdbcTemplate.query(sql.toString(), paramMap, (rs, i) -> {
			Kategori kategori = new Kategori();
			kategori.setCategoryId(rs.getInt("category_id"));
			kategori.setCategoryName(rs.getString("category_name"));
			kategori.setDescription(rs.getString("description"));
//			kategori.setPicture(rs.getBytes("picture"));
			return kategori;
		});

	}

	//untuk menghitung banyaknya data entry pas di tampilan web
	public Long countKategoriAll(DataTableRequest req) {
		Map<String, Object> map = (Map<String, Object>) req.getExtraParam();
		String query = " SELECT count(category_id) as banyak FROM public.categories " +
				"where category_name ilike concat('%',:nama,'%')";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("nama", map.get("nama"));
		return this.namedJdbcTemplate.queryForObject(query, paramMap, Long.class);
	}

	//untuk mendapatkan id nya
	public Kategori getKategoriById (Integer id) {
		String query = "SELECT category_id, category_name, description" +
				" FROM public.categories " +
				"where category_id = :id";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("id", id);
		return (Kategori) this.namedJdbcTemplate.queryForObject(query, paramMap, new BeanPropertyRowMapper(Kategori.class));
	}

	public Integer simpanKategori(Kategori kategori) {
		String query = " INSERT INTO categories (category_id, category_name, description) " +
				"values (:category_id,:category_name,:description)";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("category_id", kategori.getCategoryId());
		paramMap.addValue("category_name", kategori.getCategoryName());
		paramMap.addValue("description", kategori.getDescription());
		return this.namedJdbcTemplate.update(query,paramMap);
	}

	public Integer updateKategori(Kategori kategori) {
		String query = " UPDATE public.categories set category_name=:category_name, description=:description " +
				"where category_id=:category_id";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("category_id", kategori.getCategoryId());
		paramMap.addValue("category_name", kategori.getCategoryName());
		paramMap.addValue("description", kategori.getDescription());
		return this.namedJdbcTemplate.update(query,paramMap);
	}

	public Kategori deleteKategori(Integer id){
		String query = " DELETE from public.categories where category_id=:id";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("category_id", id);
		return (Kategori) this.namedJdbcTemplate.queryForObject(query, paramMap, new BeanPropertyRowMapper(Kategori.class));
	}

	public Integer updateImageKategori(Kategori kategori){
		String query = "UPDATE public.categories set img_location=:img_location " +
				"where category_id= :category_id";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("category_id", kategori.getCategoryId());
		paramMap.addValue("img_location", kategori.getImgLocation());
		return this.namedJdbcTemplate.update(query, paramMap);
	}
}
