package com.example.user.macarrontest;

import com.github.nitram509.jmacaroons.Macaroon;
import com.github.nitram509.jmacaroons.MacaroonsBuilder;
import com.github.nitram509.jmacaroons.MacaroonsVerifier;
import com.github.nitram509.jmacaroons.verifier.TimestampCaveatVerifier;

public class ThirdPartyCaveatExample {

    public static void main(String[] args) {
        String baseLocation = "http://hascode/";
        String baseSecret = "sooooooosecretanddefinitelynotlongenough";
        String baseIdentifier = "hascode-base";

        Macaroon base = new MacaroonsBuilder(baseLocation, baseSecret, baseIdentifier)
                .getMacaroon();

        printInfo("base macaroon", base);

        String thirdPartyLocation = "http://auth.mybank/";
        String thirdPartySecret = "theroflcopterhaslanded";
        String thirdPartyIdentifier = "hascode-3rd-party";

        Macaroon withThirdPartyCaveat = new MacaroonsBuilder(base)
                .add_third_party_caveat(thirdPartyLocation, thirdPartySecret, thirdPartyIdentifier)
                .getMacaroon();

        printInfo("with banking caveat", withThirdPartyCaveat);

        Macaroon discharge = new MacaroonsBuilder(thirdPartyLocation, thirdPartySecret,
                thirdPartyIdentifier)
                .add_first_party_caveat("time < 2025-01-01T00:00")
                .getMacaroon();

        printInfo("discharge", discharge);


        Macaroon thirdPartyDischarged = MacaroonsBuilder.modify(withThirdPartyCaveat)
                .prepare_for_request(discharge)
                .getMacaroon();

        printInfo("3rd-p-discharge", thirdPartyDischarged);

        boolean valid = new MacaroonsVerifier(withThirdPartyCaveat)
                .satisfyGeneral(new TimestampCaveatVerifier())
                .satisfy3rdParty(thirdPartyDischarged)
                .isValid(baseSecret);
        System.out.printf("macaroon is valid: %s", valid);
    }

    private static void printInfo(String hint, Macaroon macaroon) {
        System.out.println("-----------------------------------\n");
        System.out.printf("-- %s:\n", hint.toUpperCase());
        System.out.printf("-- Human readable:\n%s\n", macaroon.inspect());
        System.out.printf("-- Serialized (Base64 URL safe):\n%s\n", macaroon.serialize());
        System.out.println("-----------------------------------\n");
    }
}