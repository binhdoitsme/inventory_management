package com.hanu.ims.model.domain;

public class Supplier {
	private int id;
	private String name;
	private String phone;
	private String address;
	private Boolean is_available;

	public Supplier(int id, String name, String phone, String address, Boolean is_available) {
		if (validated(id, name, phone, address, is_available)) {
			this.id = id;
			this.name = name;
			this.phone = phone;
			this.address = address;
			this.is_available = is_available;
		}
	}

	public Supplier(String name, String phone, String address, Boolean is_available) {
		if (validatedNoneId(name, phone, address, is_available)) {
			this.name = name;
			this.phone = phone;
			this.address = address;
			this.is_available = is_available;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		if (validatedId(id)) {
			this.id = id;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (validatedName(name)) {
			this.name = name;
		}
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		if (validatedPhone(phone)) {
			this.phone = phone;
		}
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if (validatedAddress(address)) {
			this.address = address;
		}
	}

	public Boolean getIs_available() {
		return is_available;
	}

	public void setIs_available(Boolean is_available) {
		if(validatedIsAvailable(is_available)) {
    		this.is_available = is_available;
    	}
	}

	private boolean validated(int id, String name, String phone, String address, Boolean is_available) {
		if (validatedId(id) && validatedName(name) && validatedPhone(phone) && validatedAddress(address)
				&& validatedIsAvailable(is_available)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validatedNoneId(String name, String phone, String address, Boolean is_available) {
		if (validatedName(name) && validatedPhone(phone) && validatedAddress(address)
				&& validatedIsAvailable(is_available)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validatedName(String name) {
		if (name == null || name.trim().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validatedPhone(String phone) {
		if (phone == null || phone.trim().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validatedAddress(String address) {
		if (address == null || address.trim().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validatedIsAvailable(Boolean is_available) {
		if (is_available == null) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validatedId(int id) {
		if (id < 0) {
			return false;
		} else {
			return true;
		}
	}
}
