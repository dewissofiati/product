package com.tdi.northwind;

import javax.servlet.http.HttpServletRequest;

import com.tdi.northwind.model.Kategori;
import com.tdi.northwind.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.tdi.northwind.config.KategoriRepository;
import com.tdi.northwind.model.DataTableRequest;
import com.tdi.northwind.model.DataTablesResponse;

import org.springframework.web.multipart.MultipartFile;
import util.Utility;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


@Controller
public class KategoriController {

	@Autowired
	private KategoriRepository kategoriRepository;
	@Autowired
	private StorageService storageService;
	@Value("${user.upload.location}")
	private String pathFile;


	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = "/listkategori", method = {RequestMethod.POST, RequestMethod.GET})
	public DataTablesResponse listUser(HttpServletRequest req) {
		String nama = req.getParameter("nama");
		int offset = Utility.tryParse(req.getParameter("start"));
		int limit = Utility.tryParse(req.getParameter("length"));
		int draw = Utility.tryParse(req.getParameter("draw"));
		int sortCol = Utility.tryParse(req.getParameter("order[0][column]"));
		String sortDir = req.getParameter("order[0][dir]");
		DataTableRequest dtreq = new DataTableRequest();
		dtreq.setLength(limit);
		dtreq.setStart(offset);
		dtreq.setDraw(draw);
		dtreq.setSortCol(sortCol);
		dtreq.setSortDir(sortDir);

		Map<String, Object> extraParam = new HashMap<>();
		extraParam.put("nama", nama);
		dtreq.setExtraParam(extraParam);

		DataTablesResponse dataTableRespon = new DataTablesResponse();
		dataTableRespon.setData(kategoriRepository.getListKategori(dtreq));
		Long total = kategoriRepository.countKategoriAll(dtreq);
		dataTableRespon.setRecordsFiltered(total);
		dataTableRespon.setRecordsTotal(total);
		dataTableRespon.setDraw(draw);
		return dataTableRespon;
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = "/simpankategori", method = {RequestMethod.POST, RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> simpanKategori(@RequestBody Kategori kategori) {
		kategoriRepository.simpanKategori(kategori);
		Map<String, Object> info = new HashMap<>();
		info.put("pesan", "Simpan berhasil");
		return ResponseEntity.status(HttpStatus.OK).body(info);
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = "/getkategoribyid/{id}", method = {RequestMethod.GET})
	public ResponseEntity<Kategori> getKategoriById(@PathVariable("id") Integer id) {
		return ResponseEntity.status(HttpStatus.OK).body(kategoriRepository.getKategoriById(id));
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = "/updatekategori", method = {RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> updateKategori(@RequestBody Kategori kategori) {
		kategoriRepository.updateKategori(kategori);
		Map<String, Object> info = new HashMap<>();
		info.put("pesan", "Update berhasil");
		return ResponseEntity.status(HttpStatus.OK).body(info);
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value = "/deletekategori/{id}", method = {RequestMethod.POST})
	public ResponseEntity<Kategori> deleteKategori(@PathVariable("id") Integer id) {
//		kategoriRepository.updateKategori(kategori);
//		Map<String, Object> info = new HashMap<>();
//		info.put("pesan", "Update berhasil");
		return ResponseEntity.status(HttpStatus.OK).body(kategoriRepository.deleteKategori(id));
	}

//	public ResponseEntity<?> delete(@RequestParam("id_kategori") String id){
//		service.delete(id);
//		return ResponseEuntity.ok().build();
//	}

	@CrossOrigin
	@PostMapping(value = {"/upload/post/{id}"}, consumes = {"multipart/form-data"})
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable("id") Integer id) {
		try {
			storageService.store(file, id);
			Kategori kategori = new Kategori();
			kategori.setCategoryId(id);
			kategori.setImgLocation(id + ".png");
			kategoriRepository.updateImageKategori(kategori);
			return ResponseEntity.status(HttpStatus.OK).body(file.getOriginalFilename());
		} catch (Exception e) {
			e.printStackTrace();
			String message = "FAIL to load" + file.getOriginalFilename() + e.getMessage();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}

	@CrossOrigin
	@GetMapping(value = "/api/image/{id}")
	public ResponseEntity<InputStreamResource> getImage(@PathVariable("id") Integer id) {
		try {
			File imgFile2 = new File(pathFile + id + ".png");
			InputStream targetStream = new FileInputStream(imgFile2);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(targetStream));
		} catch (Exception e) {
			File imgFile2 = new File(pathFile + "no_image.png");
			try {
				InputStream targetStream = new FileInputStream(imgFile2);
				return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(targetStream));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
		//ini coba cacri lagi return nya kemana
	}
}

