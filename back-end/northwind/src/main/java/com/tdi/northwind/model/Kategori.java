package com.tdi.northwind.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kategori {
	private Integer categoryId;
	private String categoryName, description;
	private String imgLocation;
}
