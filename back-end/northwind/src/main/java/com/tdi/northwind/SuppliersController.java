package com.tdi.northwind;

import com.tdi.northwind.config.SuppliersRepository;
import com.tdi.northwind.model.DataTableRequest;
import com.tdi.northwind.model.DataTablesResponse;
import com.tdi.northwind.model.Suppliers;
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
public class SuppliersController {

    @Autowired
    private SuppliersRepository suppliersRepository;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/listsuppliers", method = {RequestMethod.POST, RequestMethod.GET})
    public DataTablesResponse listsuppliers(HttpServletRequest req) {
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
        dataTableRespon.setData(suppliersRepository.getListSuppliers(dtreq));
        Long total = suppliersRepository.counrSuppliersAll(dtreq);
        dataTableRespon.setRecordsFiltered(total);
        dataTableRespon.setRecordsTotal(total);
        dataTableRespon.setDraw(draw);
        return dataTableRespon;
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/simpansuppliers", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Map<String, Object>> simpanSuppliers(@RequestBody Suppliers suppliers) {
        suppliersRepository.simpanSuppliers(suppliers);
        Map<String, Object> info = new HashMap<>();
        info.put("pesan", "Simpan berhasil");
        return ResponseEntity.status(HttpStatus.OK).body(info);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/getsuppliersbyid/{id}", method = {RequestMethod.GET})
    public ResponseEntity<Suppliers> getSuppliersById(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(suppliersRepository.getSuppliersById(id));
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/updatesuppliers", method = {RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> updateSuppliers(@RequestBody Suppliers suppliers) {
        suppliersRepository.updateSuppliers(suppliers);
        Map<String, Object> info = new HashMap<>();
        info.put("pesan", "Update berhasil");
        return ResponseEntity.status(HttpStatus.OK).body(info);
    }
}
