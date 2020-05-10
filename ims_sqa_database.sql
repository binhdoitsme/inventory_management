CREATE DATABASE ims_sqa;
USE ims_sqa;
CREATE TABLE account (
  `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
  `username` VARCHAR(64) NOT NULL UNIQUE,
  `password` VARCHAR(64) NOT NULL,
  `role` ENUM('Admin', 'InventoryManager', 'Salesperson') NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

INSERT INTO account (`id`, `username`, `password`, `role`) VALUES (NULL, 'admin', 'admin', 'Admin');
INSERT INTO account (`id`, `username`, `password`, `role`) VALUES (NULL, 'inventorymanager', 'inventorymanager', 'InventoryManager');
INSERT INTO account (`id`, `username`, `password`, `role`) VALUES (NULL, 'salesperson', 'salesperson', 'Salesperson');


CREATE TABLE category (
  `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;
INSERT INTO category (`id`, `name`) VALUES (NULL, 'Books');
INSERT INTO category (`id`, `name`) VALUES (NULL, 'Tools');
INSERT INTO category (`id`, `name`) VALUES (NULL, 'Materials');

CREATE TABLE supplier (
  `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
  `name` VARCHAR(127) NOT NULL,
  `phone` char(10) NOT NULL, /*should be varchar*/
  `address` VARCHAR(255),
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

INSERT INTO supplier (`id`, `name`, `phone`, `address`) VALUES (NULL, 'M-TP Production', '0123456789', '49 Lac Troi St.');
INSERT INTO supplier (`id`, `name`, `phone`, `address`) VALUES (NULL, 'ACME Corp.', '0123251389', 'Planet Earth');


CREATE TABLE supplier_category(
	`supplier_id` INT NOT NULL,
	`category_id` INT NOT NULL,
	CONSTRAINT FK_SupplierCategorySupplier FOREIGN KEY (supplier_id)
	REFERENCES supplier(id),
	CONSTRAINT FK_SupplierCategoryCategory FOREIGN KEY (category_id)
	REFERENCES category(id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

INSERT INTO supplier_category (`supplier_id`, `category_id`) VALUES (1, 3);
INSERT INTO supplier_category (`supplier_id`, `category_id`) VALUES (1, 2);
INSERT INTO supplier_category (`supplier_id`, `category_id`) VALUES (2, 1);

CREATE TABLE product(
	`sku` VARCHAR(30) NOT NULL UNIQUE,
	`name` VARCHAR(255) NOT NULL,
	`description` VARCHAR(1023) NOT NULL,
	`category_id` INT NOT NULL,

	PRIMARY KEY (`sku`),
	FOREIGN KEY (category_id) REFERENCES category(id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;
INSERT INTO product (`sku`, `name`, `description`, `category_id`) 
VALUES ('KR13213BW1', 'Korean Barbequeing Wireframe', 'Grill meat really fast with this product', 2);

CREATE TABLE batch(
	`id` INT NOT NULL UNIQUE AUTO_INCREMENT,
	`sku` VARCHAR(30) NOT NULL,
	`quantity` INT NOT NULL,
	`import_price` INT NOT NULL,
	`msrp` INT NOT NULL,
	`import_date` Date NOT NULL,
	`supplier_id` INT NOT NULL,

	PRIMARY KEY(id),
	
	CONSTRAINT FK_BatchSku FOREIGN KEY (sku)
	REFERENCES product(sku),
	CONSTRAINT FK_BatchSupplier FOREIGN KEY (supplier_id)
	REFERENCES supplier(id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

INSERT INTO batch (`id`, `sku`, `quantity`, `import_price`
	, `msrp`, `import_date`, `supplier_id`) VALUES (NULL, 'KR13213BW1',
	10, 930, 1050, '2010-12-31', 2);


CREATE TABLE _order(
  `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
  `timestamp` TIMESTAMP NOT NULL, 
  `cashier_id` INT NOT NULL, 
  PRIMARY KEY (`id`),

  FOREIGN KEY (cashier_id) REFERENCES account(id)

)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


INSERT INTO _order (`id`, `timestamp`, `cashier_id`) VALUES (NULL, '2008-01-01 00:00:01', 3);

CREATE TABLE _order_line(
	`quantity` INT,
	`batch_id` INT,
	`_order_id` INT,

	CONSTRAINT FK_OrderLineBatch FOREIGN KEY (batch_id)
	REFERENCES batch(id),
	CONSTRAINT FK_OrderLineOrder FOREIGN KEY (_order_id)
	REFERENCES _order(id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

INSERT INTO _order_line (`quantity`, `batch_id`, `_order_id`) VALUES (7, 1, 1);

/* comment */


