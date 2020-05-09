module com.hanu.ims {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.slf4j;
    requires org.apache.commons.configuration2;
    requires net.bytebuddy;
    requires org.controlsfx.controls;
    requires com.gluonhq.attach.util;
    requires com.gluonhq.charm.glisten;

    opens com.hanu.ims to javafx.fxml;
    exports com.hanu.ims;
    opens com.hanu.ims.base to javafx.fxml;
    exports com.hanu.ims.base;
    opens com.hanu.ims.controller to javafx.fxml;
    exports com.hanu.ims.controller;
    opens com.hanu.ims.db to javafx.fxml;
    exports com.hanu.ims.db;
    opens com.hanu.ims.model.repository to javafx.fxml;
    exports com.hanu.ims.model.repository;
    opens com.hanu.ims.model.domain to javafx.fxml;
    exports com.hanu.ims.model.domain;
    opens com.hanu.ims.view to javafx.fxml;
    exports com.hanu.ims.view;
}