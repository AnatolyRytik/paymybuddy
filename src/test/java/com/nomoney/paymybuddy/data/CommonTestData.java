package com.nomoney.paymybuddy.data;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import com.nomoney.paymybuddy.model.User;

import java.util.Date;

public class CommonTestData {

    UserRegistrationDto userRegistrationDto = new UserRegistrationDto("Thomas", "bach", "toto", "thom@gmail.com");
    User user = new User("Thomas", "bach", "thom@gmail.com", "toto", 0.0, new Date());

    public static User getEmptyUser() {
        return new User("", "", "", "", 0.0, new Date());
    }

    public static User getUser() {
        return new User("Thomas", "bach", "thom@gmail.com", "toto", 0.0, new Date());
    }


}
