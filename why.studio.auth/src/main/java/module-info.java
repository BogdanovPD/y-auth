open module why.studio.auth {

    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.boot.configuration.processor;
    requires spring.security.config;
    requires spring.security.core;
    requires spring.security.oauth2;
    requires spring.security.jwt;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.tx;
    requires spring.web;

    requires java.sql;
    requires java.persistence;

    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires org.apache.commons.io;

    requires net.bytebuddy;

    requires static lombok;
    requires org.hibernate.orm.core;

}