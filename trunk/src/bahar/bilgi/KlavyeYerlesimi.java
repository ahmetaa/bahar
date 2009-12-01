package bahar.bilgi;

public enum KlavyeYerlesimi {
    TURKCE_F, TURKCE_Q, AMERIKAN_Q;

    public static KlavyeYerlesimi fromString(String str) {
        if (str.equalsIgnoreCase("turkce_f") || str.equalsIgnoreCase("turkish_f"))
            return TURKCE_F;
        if (str.equalsIgnoreCase("turkce_q") || str.equalsIgnoreCase("turkish_q"))
            return TURKCE_Q;
        if (str.equalsIgnoreCase("amerikan_q") || str.equalsIgnoreCase("american_q"))
            return AMERIKAN_Q;

        throw new IllegalArgumentException("unexpected layout name:" + str);

    }


}
