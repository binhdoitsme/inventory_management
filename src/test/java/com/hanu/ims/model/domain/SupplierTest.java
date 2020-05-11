package com.hanu.ims.model.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class SupplierTest {
	int[] ids = { -1, 0, 1, 100000 };
	String[] lstname = { null, "", "name", "do hai binh" };
	String[] lstphone = { null, "", "phone" };
	String[] lstaddress = { null, "", "address" };
	boolean[] lstis_available = { true, false };

	@Test
	public void testinitShouldFail() {
		for (String name : lstname) {
			for (int id : ids) {
				for (String phone : lstphone) {
					for (String address : lstaddress) {
						for (boolean is_available : lstis_available) {
							if (id < 0 || name == null || name.equals("") || phone == null || phone.equals("")
									|| address == null || address.equals("")) {
								testInitShouldFailFor(id, name, phone, address, is_available);
							}
						}
					}
				}
			}
		}
	}
	
	@Test
	public void testinitShouldPass() {
		for (String name : lstname) {
			for (int id : ids) {
				for (String phone : lstphone) {
					for (String address : lstaddress) {
						for (boolean is_available : lstis_available) {
							if (id < 0 || name == null || name.equals("") || phone == null || phone.equals("")
									|| address == null || address.equals("")) {
							} else {
								testInitShouldPassFor(id, name, phone, address, is_available);
							}
						}
					}
				}
			}
		}
	}
	//helper
	void testInitShouldFailFor(int id, String name, String phone, String address, Boolean is_available) {
		try {
			Supplier s = new Supplier(id, name, phone, address, is_available);
			throw new AssertionError();
		} catch (Exception e) {
			
		}
	}
	//helper
	void testInitShouldPassFor(int id, String name, String phone, String address, Boolean is_available) {
		Supplier s = new Supplier(id, name, phone, address, is_available);
		assertEquals(id, s.getId());
		assertEquals(name, s.getName());
		assertEquals(phone, s.getPhone());
		assertEquals(address, s.getAddress());
		assertEquals(is_available, s.getIs_available());
	}
	
	

	@Test
	public void testSetIdShouldPass() {
		Supplier s = new Supplier(2, "quantrongtu", "02345215587", "KTX", true);
		s.setId(1);
		assertEquals(1, s.getId());
		s.setId(1000000);
		assertEquals(1000000, s.getId());
	}

	@Test
	public void testSetIdShouldFail() {
		try {
			Supplier s = new Supplier(2, "quantrongtu", "02345215587", "KTX", true);
			s.setId(-1);
			s.setId(-1100000);
			throw new AssertionError();
		} catch (Exception e) {

		}
	}

	@Test
	public void testSetNameShouldPass() {
		Supplier s = new Supplier(2, "quantrongtu", "02345215587", "KTX", true);
		s.setName("do hai binh");
		assertEquals("do hai binh", s.getName());
	}

	@Test
	public void testSetNameShouldFail() {
		try {
			Supplier s = new Supplier(2, "quantrongtu", "02345215587", "KTX", true);
			s.setName(null);
			s.setName(" ");
			throw new AssertionError();
		} catch (Exception e) {

		}
	}

	@Test
	public void testSetAddressShouldPass() {
		Supplier s = new Supplier(2, "quantrongtu", "02345215587", "KTX", true);
		s.setAddress("Hanoi");
		assertEquals("Hanoi", s.getAddress());
	}

	@Test
	public void testSetAddressShouldFail() {
		try {
			Supplier s = new Supplier(2, "quantrongtu", "02345215587", "KTX", true);
			s.setAddress(null);
			s.setAddress(" ");
			throw new AssertionError();
		} catch (Exception e) {

		}
	}

	@Test
	public void testSetAvailableShouldPass() {
		Supplier s = new Supplier(2, "quantrongtu", "02345215587", "KTX", true);
		s.setIs_available(true);
		assertEquals(true, s.getIs_available());
		s.setIs_available(false);
		assertEquals(false, s.getIs_available());
	}

	@Test
	public void testSetAvailableShouldFail() {
		try {
			Supplier s = new Supplier(2, "quantrongtu", "02345215587", "KTX", true);
			s.setIs_available(null);
			throw new AssertionError();
		} catch (Exception e) {

		}
	}
}
