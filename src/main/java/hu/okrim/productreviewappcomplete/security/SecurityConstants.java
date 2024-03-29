package hu.okrim.productreviewappcomplete.security;

public class SecurityConstants {
    //Secret key should always be strong (uppercase, lowercase, numbers, symbols) so that nobody can potentially decode the signature.
    public static final String SECRET_KEY = "4AnrrTCJ#Qcjrk3Hfax#vzybvF68xXT%mt%HW&zrph3W!TXyXLDKRWwvtf%$k*wcqYnGmEPwT25UTJ4uDsbzgU%yVxWVsfmxyJr^";
    public static final int TOKEN_EXPIRATION = 7200000; // 7200000 milliseconds = 2 hours.
    public static final String BEARER = "Bearer "; // Authorization : "Bearer " + Token
    public static final String AUTHORIZATION = "Authorization"; // "Authorization" : Bearer Token
    public static final String REGISTER_PATH = "/user/register"; // Public path that users can use to register.
}
