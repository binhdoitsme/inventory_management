package com.hanu.ims.model.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class Supplier_CategoryTest {

	int[] lstidSupplier = { -1, 0, 1, 100000 };
	int[] lstidCategory = { -1, 0, 1, 100000 };

	@Test
	public void testinitShouldFail() {
		for (int idSupplier : lstidSupplier) {
			for (int idCategory : lstidCategory) {
				if (idSupplier < 0 || idCategory < 0) {
					testInitShouldFailFor(idSupplier, idCategory);
				}
			}
		}
	}

	void testInitShouldFailFor(int idSupplier, int idCategory) {
		try {
			Supplier_Category s = new Supplier_Category(idSupplier, idCategory);
			throw new AssertionError();
		} catch (Exception e) {

		}
	}

	@Test
	public void testinitShouldPass() {
		for (int idSupplier : lstidSupplier) {
			for (int idCategory : lstidCategory) {
				if (idSupplier < 0 || idCategory < 0) {
				} else {
					testInitShouldPassFor(idSupplier, idCategory);
				}
			}
		}
	}

	void testInitShouldPassFor(int idSupplier, int idCategory) {
		Supplier_Category s = new Supplier_Category(idSupplier, idCategory);
		assertEquals(idCategory, s.getCategory_id());
		assertEquals(idSupplier, s.getSupplier_id());
	}

	@Test
	public void testSetidSupplierShouldFail() {
		Supplier_Category s = new Supplier_Category(1, 1);
		try {
			s.setSupplier_id(-1);
			throw new AssertionError();
 		} catch (Exception e) {
 			
 		}
	}
	
	@Test
	public void testSetidCate_goryShouldFail() {
		Supplier_Category s = new Supplier_Category(1, 1);
		try {
			s.setCategory_id(-1);
			throw new AssertionError();
 		} catch (Exception e) {
 			
 		}
	}
}
