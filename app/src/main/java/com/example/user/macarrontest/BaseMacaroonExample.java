package com.example.user.macarrontest;

import com.github.nitram509.jmacaroons.Macaroon;
import com.github.nitram509.jmacaroons.MacaroonsBuilder;
import com.github.nitram509.jmacaroons.MacaroonsVerifier;

public class BaseMacaroonExample {

    public static void main(String[] args) {
        String location = "http://hascode";
        String secretKey = "thisisaverysecretsecretsecretkeythisisaverysecretsecretsecretkey";
        String identifier = "hascode-authentication";

        // create macaroon
        Macaroon macaroon = MacaroonsBuilder.create(location, secretKey, identifier);
        printInfo(macaroon);
        String serialized = macaroon.serialize();

        // deserialize macaroon
        Macaroon deserialize = MacaroonsBuilder.deserialize(serialized);
        printInfo(deserialize);

        // verify macaroon
        MacaroonsVerifier verifier = new MacaroonsVerifier(macaroon);
        System.out.printf("macaroon with id '%s' is valid: %s\n", macaroon.identifier,
                verifier.isValid(secretKey));
    }

    private static void printInfo(Macaroon macaroon) {
        System.out.println("-----------------------------------\n");
        System.out.printf("-- Human readable:\n%s\n", macaroon.inspect());
        System.out.printf("-- Serialized (Base64 URL safe):\n%s\n", macaroon.serialize());
        System.out.println("-----------------------------------\n");
    }
}

