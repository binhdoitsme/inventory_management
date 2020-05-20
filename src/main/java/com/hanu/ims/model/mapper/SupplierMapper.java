package com.hanu.ims.model.mapper;

import com.hanu.ims.base.Mapper;
import com.hanu.ims.model.domain.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierMapper extends Mapper<Supplier> {
    public SupplierMapper() {
        super(SupplierMapper::fromResultSet);
    }

    private static Supplier fromResultSet(ResultSet rs) {
        try {
            Supplier s = new Supplier(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getBoolean("is_available"));
            return s;
        } catch (SQLException e) {
            return null;
        }
    }

    public String convert(Supplier supplier) {
        return new String("($name,$phone, $address, $isAvailable)")
                .replace("$name", "\'" + supplier.getName() + "\'")
                .replace("$phone", "\'" + supplier.getPhone() + "\'")
                .replace("$address", "\'" + supplier.getAddress() + "\'")
                .replace("$isAvailable", "" + supplier.isAvailable());
    }
}
