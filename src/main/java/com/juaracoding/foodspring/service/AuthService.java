package com.juaracoding.foodspring.service;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/16/2023 7:49 AM
@Last Modified 8/16/2023 7:49 AM
Version 1.0
*/

import com.foodspring.constant.EmailType;
import com.foodspring.model.EmailVerification;
import com.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.config.AppConfig;
import com.juaracoding.foodspring.core.BcryptImpl;
import com.juaracoding.foodspring.dto.ForgetPasswordDTO;
import com.juaracoding.foodspring.dto.LoginDTO;
import com.juaracoding.foodspring.handler.ResourceNotFoundException;
import com.juaracoding.foodspring.handler.ResponseHandler;
import com.juaracoding.foodspring.model.Cart;
import com.juaracoding.foodspring.model.User;
import com.juaracoding.foodspring.publisher.MailPublisher;
import com.juaracoding.foodspring.repository.CartRepository;
import com.juaracoding.foodspring.repository.UserRepository;
import com.juaracoding.foodspring.utils.ConstantMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AuthService {

    private final String[] strExceptionArr = new String[2];
    private final String[] strProfile = new String[3];
    private final Map<String, String> mapColumnSearch = new HashMap<String, String>();
    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    @Autowired
    private MailPublisher mailPublisher;

    @Autowired
    public AuthService(UserRepository userRepository, CartRepository cartRepository) {
        this.cartRepository = cartRepository;
        strExceptionArr[0] = "UserService";
        this.userRepository = userRepository;
        mapColumn();
    }

    private void mapColumn() {
        mapColumnSearch.put("id", "ID USER");
        mapColumnSearch.put("nama", "NAMA LENGKAP");
        mapColumnSearch.put("email", "EMAIL");
        mapColumnSearch.put("noHP", "NO HP");
    }

    public Map<String, Object> registerUser(User user, WebRequest request) {
        int intVerification = new Random().nextInt(100000, 999999);
        User userDB = userRepository.findFirstByEmailOrPhoneOrUsername(user.getEmail(), user.getPhone(), user.getUsername());//INI VALIDASI USER IS EXISTS
        String emailForSMTP = user.getEmail();
        try {
            if (userDB != null)//kondisi mengecek apakah user terdaftar
            {

                emailForSMTP = user.getEmail();
                if (!userDB.getIsDelete())//sudah terdaftar dan aktif
                {
                    //PEMBERITAHUAN SAAT REGISTRASI BAGIAN MANA YANG SUDAH TERDAFTAR (USERNAME, EMAIL ATAU NOHP)
                    if (userDB.getEmail().equals(user.getEmail())) {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMAIL_ISEXIST,
                                HttpStatus.NOT_ACCEPTABLE, null, "FV01001", request);//EMAIL SUDAH TERDAFTAR DAN AKTIF
                    } else if (userDB.getPhone().equals(user.getPhone())) {//FV = FAILED VALIDATION
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_PHONE_ISEXIST,
                                HttpStatus.NOT_ACCEPTABLE, null, "FV01002", request);//NO HP SUDAH TERDAFTAR DAN AKTIF
                    } else if (userDB.getUsername().equals(user.getUsername())) {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USERNAME_ISEXIST,
                                HttpStatus.NOT_ACCEPTABLE, null, "FV01003", request);//USERNAME SUDAH TERDAFTAR DAN AKTIF
                    } else {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_ISACTIVE,
                                HttpStatus.NOT_ACCEPTABLE, null, "FV01004", request);//KARENA YANG DIAMBIL DATA YANG PERTAMA JADI ANGGAPAN NYA SUDAH TERDAFTAR SAJA
                    }
                } else {
                    userDB.setPassword(BcryptImpl.hash(user.getPassword() + user.getUsername()));
                    userDB.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
                    userDB.setTokenCounter(userDB.getTokenCounter() + 1);//setiap kali mencoba ditambah 1
                    userDB.setModifiedBy(Math.toIntExact(userDB.getUserId()));
                    userDB.setModifiedDate(new Date());
                }
            } else//belum terdaftar
            {
                user.setPassword(BcryptImpl.hash(user.getPassword() + user.getUsername()));
                user.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
                userRepository.save(user);
            }
            EmailVerification emailVerification = EmailVerification.builder()
                    .email(emailForSMTP)
                    .verification(String.valueOf(intVerification))
                    .subject("TOKEN UNTUK VERIFIKASI EMAIL")
                    .fullName(user.getFirstName().concat(" " + user.getLastName()))
                    .emailType(EmailType.VER_REGIS)
                    .build();


            /*EMAIL NOTIFICATION*/
            if (AppConfig.getFlagSMTPActive().equalsIgnoreCase("y") && !emailForSMTP.equals("")) {
                mailPublisher.sendEmailMessage(emailVerification);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "registerUser(User user, WebRequest request) --- LINE 70";
            LoggingFile.exceptionString(strExceptionArr, e, AppConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.NOT_FOUND, null, "FE01001", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CHECK_REGIS,
                HttpStatus.CREATED, null, null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> confirmRegis(User user, String emails, WebRequest request) {
        List<User> listUserResult = userRepository.findByEmail(emails);
        try {
            if (listUserResult.size() != 0) {
                User nextUser = listUserResult.get(0);
                if (!BcryptImpl.verifyHash(user.getToken(), nextUser.getToken())) {
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_TOKEN_INVALID,
                            HttpStatus.NOT_ACCEPTABLE, null, "FV01005", request);
                }
                // Initialize the cart for the user
                Cart userCart = new Cart();
                userCart.setUser(listUserResult.get(0));
                cartRepository.save(userCart);
                nextUser.setIsDelete(false);//SET REGISTRASI BERHASIL
            } else {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                        HttpStatus.NOT_FOUND, null, "FV01006", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "confirmRegis(User user)  --- LINE 103";
            LoggingFile.exceptionString(strExceptionArr, e, AppConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01002", request);
        }

        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CHECK_REGIS,
                HttpStatus.OK, null, null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> doLogin(LoginDTO loginDTO, WebRequest request) {
        User user = userRepository.findFirstByEmailOrPhoneOrUsername(loginDTO.getCredential(), loginDTO.getCredential(), loginDTO.getCredential());//DATANYA PASTI HANYA 1
        try {
            if (user != null) {
                if (!BcryptImpl.verifyHash(loginDTO.getPassword() + user.getUsername(), user.getPassword()))//dicombo dengan userName
                {
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_LOGIN_FAILED,
                            HttpStatus.NOT_ACCEPTABLE, null, "FV01007", request);
                }
                user.setLastLogin(LocalDateTime.now());
                user.setTokenCounter(0);//SETIAP KALI LOGIN BERHASIL , BERAPA KALIPUN UJI COBA REQUEST TOKEN YANG SEBELUMNYA GAGAL AKAN SECARA OTOMATIS DIRESET MENJADI 0
                user.setPasswordCounter(0);//SETIAP KALI LOGIN BERHASIL , BERAPA KALIPUN UJI COBA YANG SEBELUMNYA GAGAL AKAN SECARA OTOMATIS DIRESET MENJADI 0
            } else {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE, null, "FV01008", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "doLogin(User user,WebRequest request)  --- LINE 132";
            LoggingFile.exceptionString(strExceptionArr, e, AppConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_LOGIN_FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01003", request);
        }

        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_LOGIN,
                HttpStatus.OK, user, null, request);
    }

    public Map<String, Object> getNewToken(String email, WebRequest request) {
        List<User> listUserResult = userRepository.findByEmail(email);//DATANYA PASTI HANYA 1
        String emailForSMTP = "";
        int intVerification = 0;
        User user = null;
        try {
            if (listUserResult.size() != 0) {
                intVerification = new Random().nextInt(100000, 999999);
                user = listUserResult.get(0);
                user.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
                user.setModifiedDate(new Date());
                user.setModifiedBy(Math.toIntExact(user.getUserId()));
                emailForSMTP = user.getEmail();
            } else {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE, null, "FV01009", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "getNewToken(String email, WebRequest request)  --- LINE 185";
            LoggingFile.exceptionString(strExceptionArr, e, AppConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01004", request);
        }

        /*
                call method send SMTP
         */
        EmailVerification emailVerification = EmailVerification.builder()
                .email(emailForSMTP)
                .verification(String.valueOf(intVerification))
                .subject("TOKEN BARU UNTUK VERIFIKASI EMAIL")
                .fullName(user.getFirstName().concat(" " + user.getLastName()))
                .emailType(EmailType.VER_NEW_TOKEN_EMAIL)
                .build();

        /*EMAIL NOTIFICATION*/
        if (AppConfig.getFlagSMTPActive().equalsIgnoreCase("y") && !emailForSMTP.equals("")) {
            mailPublisher.sendEmailMessage(emailVerification);
        }


        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_LOGIN,
                HttpStatus.OK, null, null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMailForgetPwd(String email, WebRequest request) {
        int intVerification = 0;
        List<User> listUserResults = userRepository.findByEmail(email);
        User user = null;
        try {
            if (listUserResults.size() == 0) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                        HttpStatus.NOT_FOUND, null, "FV01010", request);
            }
            intVerification = new Random().nextInt(100000, 999999);
            user = listUserResults.get(0);
            user.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
            user.setModifiedDate(new Date());
            user.setModifiedBy(Math.toIntExact(user.getUserId()));
        } catch (Exception e) {
            strExceptionArr[1] = "sendMailForgetPwd(String email)  --- LINE 214";
            LoggingFile.exceptionString(strExceptionArr, e, AppConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01005", request);
        }
        EmailVerification emailVerification = EmailVerification.builder()
                .email(user.getEmail())
                .verification(String.valueOf(intVerification))
                .subject("TOKEN UNTUK VERIFIKASI LUPA PASSWORD")
                .fullName(user.getFirstName().concat(" " + user.getLastName()))
                .emailType(EmailType.FORGOT_PASSWORD)
                .build();


        /*EMAIL NOTIFICATION*/
        if (AppConfig.getFlagSMTPActive().equalsIgnoreCase("y") && !user.getEmail().equals("")) {
           mailPublisher.sendEmailMessage(emailVerification);
        }

        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_SEND_NEW_TOKEN,
                HttpStatus.OK, null, null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> confirmTokenForgotPwd(ForgetPasswordDTO forgetPasswordDTO, WebRequest request) {
        String email = forgetPasswordDTO.getEmail();
        String token = forgetPasswordDTO.getToken();

        List<User> listUserResults = userRepository.findByEmail(email);
        try {
            if (listUserResults.size() == 0) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                        HttpStatus.NOT_FOUND, null, "FV01011", request);
            }

            User user = listUserResults.get(0);

            if (!BcryptImpl.verifyHash(token, user.getToken()))//VALIDASI TOKEN
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_TOKEN_FORGOTPWD_NOT_SAME,
                        HttpStatus.NOT_FOUND, null, "FV01012", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "confirmTokenForgotPwd(ForgetPasswordDTO forgetPasswordDTO, WebRequest request)  --- LINE 250";
            LoggingFile.exceptionString(strExceptionArr, e, AppConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01006", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_TOKEN_MATCH,
                HttpStatus.OK, null, null, request);
    }


    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> confirmPasswordChange(ForgetPasswordDTO forgetPasswordDTO, WebRequest request) {
        String email = forgetPasswordDTO.getEmail();
        String newPassword = forgetPasswordDTO.getNewPassword();
        String confirmPassword = forgetPasswordDTO.getConfirmPassword();

        List<User> listUserResults = userRepository.findByEmail(email);
        try {
            if (listUserResults.size() == 0) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_FOUND, null, "FV01012", request);
            }

            User user = listUserResults.get(0);
            if (!confirmPassword.equals(newPassword))//PASSWORD BARU DENGAN PASSWORD KONFIRMASI TIDAK SAMA
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_PASSWORD_CONFIRM_FAILED,
                        HttpStatus.NOT_FOUND, null, "FV01014", request);
            }

            user.setPassword(BcryptImpl.hash(String.valueOf(newPassword + user.getUsername())));
            user.setIsDelete(false);
            user.setModifiedDate(new Date());
            user.setModifiedBy(Math.toIntExact(user.getUserId()));
        } catch (Exception e) {
            strExceptionArr[1] = "confirmPasswordChange(ForgetPasswordDTO forgetPasswordDTO, WebRequest request)  --- LINE 297";
            LoggingFile.exceptionString(strExceptionArr, e, AppConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01006", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CHANGE_PWD,
                HttpStatus.OK, null, null, request);
    }


    public Map<String, Object> linkMailVerify(String usrId, String token, String mail) {

        List<User> listUsr = userRepository.findByEmail(mail);

        if (listUsr.size() == 0) {
            strExceptionArr[1] = "linkMailVerify(String usrId, String token,String mail, WebRequest request) --- LINE 666";
            LoggingFile.exceptionString(strExceptionArr, new ResourceNotFoundException("Otentikasi Tidak Valid"), AppConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.NOT_FOUND, null, "FE01001", null);
        } else {
            User user = listUsr.get(0);
            if (user.getIsDelete()) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.USER_IS_ACTIVE,
                        HttpStatus.NOT_FOUND, null, null, null);
            } else {
                if (BcryptImpl.verifyHash(user.getToken(), token) && BcryptImpl.verifyHash(String.valueOf(user.getUsername()), usrId)) {
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.VERIFY_LINK_VALID,
                            HttpStatus.CREATED, null, null, null);
                } else {
                    strExceptionArr[1] = "linkMailVerify(String usrId, String token,String mail, WebRequest request) --- LINE 683";
                    LoggingFile.exceptionString(strExceptionArr, new ResourceNotFoundException("Otentikasi Tidak Valid"), AppConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                            HttpStatus.NOT_FOUND, null, "FE01001", null);

                }
            }
        }
    }

}
