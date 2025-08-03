package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.entity.*;
import jakarta.persistence.ManyToOne;

import java.lang.reflect.*;

public class RepositoryMethodGenerator {
    public static void main(String[] args) {
        generateFindByMethods(BillDetails.class);
    }

    public static void generateFindByMethods(Class<?> clazz) {
        String entityName = clazz.getSimpleName();
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            Class<?> type = field.getType();

            if (field.isAnnotationPresent(ManyToOne.class)) {
                System.out.println("List<" + entityName + "> findBy" + capitalize(fieldName) + "(" + type.getSimpleName() + " " + fieldName + ");");
                System.out.println("List<" + entityName + "> findBy" + capitalize(fieldName) + "_Id(Integer " + fieldName + "Id);");
            } else {
                System.out.println("List<" + entityName + "> findBy" + capitalize(fieldName) + "(" + type.getSimpleName() + " " + fieldName + ");");
            }
        }
    }

    private static String capitalize(String str) {
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
}
