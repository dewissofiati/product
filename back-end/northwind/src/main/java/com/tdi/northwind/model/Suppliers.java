package com.tdi.northwind.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suppliers {

    private Integer supplierId;
    private String companyName;
    private String contactName;
    private String address;
}
