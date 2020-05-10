package com.hanu.ims.model.domain;

import com.hanu.ims.exception.InitializationException;

public class Supplier_Category {
	private int supplier_id;
	private int category_id;

	public Supplier_Category(int supplier_id, int category_id) {
		if (!validateCategory_id(category_id) || !validatedSupplier_id(supplier_id)) {
			throw new InitializationException();
		} else {
			this.supplier_id = supplier_id;
			this.category_id = category_id;
		}
	}

	public int getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(int supplier_id) {
		if (validatedSupplier_id(supplier_id)) {
			this.supplier_id = supplier_id;
		} else {
			throw new InitializationException();
		}
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		if (validateCategory_id(category_id)) {
			this.category_id = category_id;
		} else {
			throw new InitializationException();
		}
	}

	private boolean validatedSupplier_id(int supplier_id) {
		if (supplier_id >= 0) {
			return true;
		}
		return false;
	}

	private boolean validateCategory_id(int category_id) {
		if (supplier_id >= 0) {
			return true;
		}
		return false;
	}
}
