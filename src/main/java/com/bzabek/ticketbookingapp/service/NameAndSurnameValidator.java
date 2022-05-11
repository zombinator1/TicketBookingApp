package com.bzabek.ticketbookingapp.service;



import com.bzabek.ticketbookingapp.exceptions.NameSurnameException;

import java.util.regex.Pattern;

class NameAndSurnameValidator {

    private NameAndSurnameValidator() {
    }

    public static void validate(String str) {
        Pattern pattern = Pattern.compile("[^a-zA-ząćęłńóśźżĄĆĘŁŃÓŚŹŻ\\s]");
        Pattern capitalLetterPattern = Pattern.compile("[A-ZĄĆĘŁŃÓŚŹŻ]");
        str = str.trim();
        str = str.replaceAll(" +", " ");

        int numberOfSpacebars = 0;
        int secondPart = 0;
        int i = 0;
        for (Character ch : str.toCharArray()) {
            i++;
            if (ch.toString().matches(pattern.pattern())) {
                throw new NameSurnameException("Please don't use non alphabet signs");
            }
            if (ch.toString().equals(" ")) {
                secondPart = i;
                numberOfSpacebars++;
                if (numberOfSpacebars > 1) {
                    throw new NameSurnameException("Can consist only two parts");
                }
            }
        }
        //checking required length
        if (numberOfSpacebars > 0) {
            String[] twoPartsSurname = str.split("\\s");
            if (twoPartsSurname[0].length() < 3 || twoPartsSurname[1].length() < 3) {
                throw new NameSurnameException("Every part must be at least 3 letters long");
            }
        } else {
            if (str.length() < 3) {
                throw new NameSurnameException("Must be at least 3 letters long");
            }
        }

        if(!Character.valueOf(str.charAt(0)).toString().matches(capitalLetterPattern.pattern())){
            throw new NameSurnameException("Must start with capital letter");
        }
        if(!Character.valueOf(str.charAt(secondPart)).toString().matches(capitalLetterPattern.pattern())){
            throw new NameSurnameException("Must start with capital letter");
        }
    }

}
