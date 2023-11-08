module javafx {

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.apache.logging.log4j;
    requires jakarta.inject;
    requires jakarta.cdi;
    requires io.vavr;
    requires com.zaxxer.hikari;
    requires MaterialFX;
    requires jakarta.xml.bind;
    requires java.sql;
    requires commons.dbcp2;
    exports ui.main to javafx.graphics;
    exports ui.screens.principal;
    exports ui.screens.login;
    exports ui.screens.common;
    exports ui.screens.customers;
    exports ui.screens.orders;
    exports model;
    exports dao.impl;
    exports ui.screens.welcome;
    exports model.errors;
    exports dao;
    exports services;
    exports common;

    opens ui.screens.login;
    opens ui.screens.principal;
    opens ui.screens.customers;
    opens ui.main;
    opens css;
    opens fxml;
    opens services;
    opens common;
    opens dao.impl;
    opens dao;
    exports services.impl;
    opens services.impl;
}
