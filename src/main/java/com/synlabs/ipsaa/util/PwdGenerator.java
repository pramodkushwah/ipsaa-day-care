package com.synlabs.ipsaa.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by sushil on 06-12-2016.
 */
public class PwdGenerator
{

    public static void main(String args[]) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.printf("%s\n",encoder.encode("ipsaa"));
    }
}
