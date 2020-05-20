package com.hanu.ims.controller;

import com.hanu.ims.app.MockStartUp;
import com.hanu.ims.model.domain.Supplier;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SupplierControllerTest {

    @BeforeClass
    public static void init() {
        try {
            // mock startup
            new MockStartUp().getConfigurations().configureDependencies();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateSupplierShouldPass() {
        Supplier s = new Supplier(4, "QuanTrongTu", "0832677917", "KTX", true);
        try {
            assertEquals(true, new SupplierController().createSupplier(s));
        } catch (Exception e) {

        }
    }

    @Test
    public void testInvalidateSupplier() {
        Supplier s = new Supplier(4, "QuanTrongTu", "0832677917", "KTX", true);
        try {
            assertEquals(true, new SupplierController().invalidateSupplier(s));
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetSupplierDetails() {
        int id = 1;
        try {
            assertEquals(1, new SupplierController().getSupplierDetails(1).getId());
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
    }

    @Test
    public void testUpdateSupplier() {
        Supplier s = new Supplier(4, "Hello", "0832677917", "KTX", true);
        try {
            assertEquals("Hello", new SupplierController().getSupplierDetails(s.getId()).getName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void findAll() {
        try {
            List<Supplier> lstSupplier = new SupplierController().getSupplierList();
            assertEquals(4, lstSupplier.size());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
