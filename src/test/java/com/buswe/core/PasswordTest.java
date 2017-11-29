package com.buswe.core;

import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class PasswordTest {
    public static void main(String [] gs)
    {


        StandardPasswordEncoder encoder=new StandardPasswordEncoder();
      String ss=  encoder.encode("1");
      System.out.println(ss);
    }
}
