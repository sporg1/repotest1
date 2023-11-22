package com.journaldev.examples;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Helper {

    public String removeSpecialChars(String param) {

        return param.replaceAll("[^a-zA-Z0-9]", ""); 

    }


    public boolean validatePassword(String password) {

        if(password.length() > 10)
            return true;
        else
            return false;

    }




}
