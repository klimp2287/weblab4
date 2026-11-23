package com.example.shop.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BcryptDemo {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "hardpass123";

        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);

        System.out.println("Хеш 1: " + hash1);
        System.out.println("Хеш 2: " + hash2);
        System.out.println("Хеши равны: " + hash1.equals(hash2));
        System.out.println("Верный пароль: " + encoder.matches(password, hash1));
        System.out.println("Неверный пароль: " + encoder.matches("wrongpass", hash1));
    }
}