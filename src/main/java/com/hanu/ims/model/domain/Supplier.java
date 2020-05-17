package com.hanu.ims.model.domain;

import java.util.Objects;

public class Supplier {
	private int id;
	private String name;
	private String phone;
	private String address;
	private Boolean isAvailable;
	private Category category;

	public Supplier(int id, String name, String phone, String address, Boolean isAvailable) {
		if (validated(id, name, phone, address, isAvailable)) {
			this.id = id;
			this.name = name;
			this.phone = phone;
			this.address = address;
			this.isAvailable = isAvailable;
		}
	}

	public Supplier(String name, String phone, String address, Boolean isAvailable) {
		if (validatedNoneId(name, phone, address, isAvailable)) {
			this.name = name;
			this.phone = phone;
			this.address = address;
			this.isAvailable = isAvailable;
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

	public Boolean isAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		if(validatedIsAvailable(isAvailable)) {
    		this.isAvailable = isAvailable;
    	}
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	private boolean validated(int id, String name, String phone, String address, Boolean isAvailable) {
		if (validatedId(id) && validatedName(name) && validatedPhone(phone) && validatedAddress(address)
				&& validatedIsAvailable(isAvailable)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validatedNoneId(String name, String phone, String address, Boolean isAvailable) {
		if (validatedName(name) && validatedPhone(phone) && validatedAddress(address)
				&& validatedIsAvailable(isAvailable)) {
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

	private boolean validatedIsAvailable(Boolean isAvailable) {
		if (isAvailable == null) {
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("] ")
				.append(name.toUpperCase());
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Supplier supplier = (Supplier) o;
		return id == supplier.id &&
				Objects.equals(name, supplier.name) &&
				Objects.equals(phone, supplier.phone) &&
				Objects.equals(address, supplier.address) &&
				Objects.equals(isAvailable, supplier.isAvailable) &&
				Objects.equals(category, supplier.category);
	}
}
