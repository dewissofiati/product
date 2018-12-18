package com.tdi.northwind.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTablesResponse {

	private List<?> data;
	private Integer draw;
	private Long recordsFiltered, recordsTotal;
}