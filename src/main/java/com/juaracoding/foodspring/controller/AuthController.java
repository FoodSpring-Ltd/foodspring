package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/10/2023 12:16 PM
@Last Modified 8/10/2023 12:16 PM
Version 1.0
*/

import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.ViewPath;
import com.juaracoding.foodspring.dto.ForgetPasswordDTO;
import com.juaracoding.foodspring.dto.LoginDTO;
import com.juaracoding.foodspring.dto.UserDTO;
import com.juaracoding.foodspring.handler.FormatValidation;
import com.juaracoding.foodspring.model.User;
import com.juaracoding.foodspring.service.AuthService;
import com.juaracoding.foodspring.utils.ConstantMessage;
import com.juaracoding.foodspring.utils.MappingAttribute;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(ServicePath.AUTH)
public class AuthController {

    private final AuthService authService;
    private final MappingAttribute mappingAttribute = new MappingAttribute();
    @Autowired
    private ModelMapper modelMapper;
    private Map<String, Object> objectMapper = new HashMap<>();

    @Autowired
    public AuthController(AuthService authService) {
        String[] strExceptionArr = new String[2];
        strExceptionArr[0] = "AuthController";
        this.authService = authService;
    }

    /*
        VALIDASI FORM REGISTRASI
     */
    @PostMapping(ServicePath.REGISTER)
    public String doRegis(@ModelAttribute("user")
                          @Valid UserDTO user
            , BindingResult bindingResult
            , Model model
            , WebRequest request) {
        /* START VALIDATION */
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return ViewPath.AUTH_REGISTER_PAGE;
        }
        boolean isValid = true;
        if (!FormatValidation.phoneNumberFormatValidation(user.getPhone())) {
            isValid = false;
            bindingResult.rejectValue("phone", "phone.invalid", ConstantMessage.ERROR_PHONE_NUMBER_FORMAT_INVALID);
        }

        if (!FormatValidation.emailFormatValidation(user.getEmail())) {
            isValid = false;
           bindingResult.rejectValue("email", "email.invalid", ConstantMessage.ERROR_EMAIL_FORMAT_INVALID);
        }
        if (!isValid) {
            model.addAttribute("user", user);
            return ViewPath.AUTH_REGISTER_PAGE;
        }
        /* END OF VALIDATION */

        User users = modelMapper.map(user, new TypeToken<User>() {}.getType());
        objectMapper = authService.checkRegis(users, request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return ViewPath.REDIRECT_LOGOUT;
        }

        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model, objectMapper);
            model.addAttribute("verifyEmail", user.getEmail());
            model.addAttribute("users", new User());

            return ViewPath.AUTH_VERIFY;
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            model.addAttribute("users", users);
            return ViewPath.AUTH_REGISTER_PAGE;
        }
    }

    @GetMapping(ServicePath.REGISTER)
    public String getRegisterForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return ViewPath.AUTH_REGISTER_PAGE;
    }
    /*
        VERIFIKASI TOKEN SETELAH MENGINPUT FORM REGISTRASI
     */
    @GetMapping(ServicePath.NEW_TOKEN)
    public String requestToken(@ModelAttribute("user")
                               @Valid UserDTO user,
                               BindingResult bindingResult, Model model, @RequestParam String email, WebRequest request) {

        if (email == null || email.equals("") || !FormatValidation.emailFormatValidation(email)) {
            return ViewPath.REDIRECT_LOGOUT;//LANGSUNG LOGOUT KARENA FLOW TIDAK VALID / MUNGKIN HIT API INI BUKAN DARI WEB
        }

        objectMapper = authService.getNewToken(email, request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return ViewPath.REDIRECT_LOGOUT;
        }
        Boolean isSuccess = (Boolean) objectMapper.get("success");
        if (isSuccess) {
            model.addAttribute("verifyEmail", email);
            model.addAttribute("loginDTO", new LoginDTO());
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
        } else {
            model.addAttribute("loginDTO", new LoginDTO());
        }
        return ViewPath.LOGIN;
    }

    /*
        VERIFIKASI TOKEN SETELAH MENGINPUT FORM REGISTRASI
     */
    @PostMapping(ServicePath.VERIFY)
    public String verifyRegis(@ModelAttribute("user")
                              @Valid User user,
                              BindingResult bindingResult,
                              Model model,
                              @RequestParam String email,
                              WebRequest request) {

        String verToken = user.getToken();
        int lengthToken = verToken.length();
        if (email == null || email.equals("") || !FormatValidation.emailFormatValidation(email)) {
            return ViewPath.REDIRECT_LOGOUT;//Flow sudah salah kalau ini kosong ATAU FORMAT EMAIL TIDAK SESUAI
        }

        if (verToken.equals(""))//token tidak boleh kosong
        {
            mappingAttribute.setErrorMessage(bindingResult, ConstantMessage.ERROR_TOKEN_IS_EMPTY);
            model.addAttribute("verifyEmail", email);
            return ViewPath.AUTH_VERIFY;
        } else if (lengthToken != 6)//token HARUS 6 DIGIT
        {
            mappingAttribute.setErrorMessage(bindingResult, ConstantMessage.ERROR_TOKEN_INVALID);
            model.addAttribute("verifyEmail", email);
            return ViewPath.AUTH_VERIFY;
        }

        objectMapper = authService.confirmRegis(user, email, request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return ViewPath.REDIRECT_LOGOUT;
        }

        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setErrorMessage(bindingResult, "REGISTRASI BERHASIL SILAHKAN LOGIN");
            model.addAttribute("loginDTO", new LoginDTO());//agar field kosong
            return ViewPath.LOGIN;
        } else {
            model.addAttribute("verifyEmail", email);
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            return ViewPath.AUTH_VERIFY;
        }
    }

    @GetMapping(ServicePath.LOGIN)
    public String getLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return ViewPath.LOGIN;
    }

    /*
        API UNTUK LOGIN
     */
    @PostMapping(ServicePath.LOGIN)
    public String login(@ModelAttribute("loginDTO")
                        @Valid LoginDTO loginDto,
                        BindingResult bindingResult,
                        Model model,
                        WebRequest request) {

        if (bindingResult.hasErrors()) {
            return ViewPath.LOGIN;
        }

        objectMapper = authService.doLogin(loginDto, request);
        Boolean isSuccess = (Boolean) objectMapper.get("success");
        String userParse = objectMapper.get("data") == null ? null : "";
        if (userParse == null) {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            model.addAttribute("loginDTO", new LoginDTO());
            return ViewPath.LOGIN;
        }
        if (isSuccess) {
            User nextUser = (User) objectMapper.get("data");
//            nextUser.getAkses().getListMenuAkses().get(0).getMenuHeader().getNamaMenuHeader();
            //        System.out.println(WebRequest.SCOPE_REQUEST);//0
            //        System.out.println(WebRequest.SCOPE_SESSION);//1
            //0 = scope request artinya hanya saat login saja tidak menyimpan di memory server / database
            //1 = scope session artinya setelah login dan akan menyimpan data selama session masih aktif
            request.setAttribute("USR_ID", nextUser.getUserId(), 1);//cara ambil request.getAttribute("USR_ID",1)
            request.setAttribute("EMAIL", nextUser.getEmail(), 1);//cara ambil request.getAttribute("EMAIL",1)
            request.setAttribute("PHONE", nextUser.getPhone(), 1);//cara ambil request.getAttribute("NO_HP",1)
            request.setAttribute("USR_NAME", nextUser.getUsername(), 1);//cara ambil request.getAttribute("USR_NAME",1)
            request.setAttribute("IS_ADMIN", nextUser.getIsAdmin(), 1);
            mappingAttribute.setAttribute(model, objectMapper, request);//urutan nya ini terakhir
            return ViewPath.REDIRECT_SLASH;
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            return ViewPath.LOGIN;
        }
    }

    /*
        PENGECEKAN PERTAMA KALI UNTUK LUPA PASSWORD
     */
    @PostMapping(ServicePath.FORGET_PASSWORD)
    public String sendMailForgetPwd(@ModelAttribute("forgetpassword")
                                    @Valid ForgetPasswordDTO forgetPasswordDTO,
                                    BindingResult bindingResult
            , Model model
            , WebRequest request
    ) {
        String email = forgetPasswordDTO.getEmail();
        Boolean isInvalid = false;

        if (email == null || email.equals("")) {
            isInvalid = true;
            mappingAttribute.setErrorMessage(bindingResult, ConstantMessage.ERROR_EMAIL_IS_EMPTY);//FLOW AWAL UNTUK VALIDASI EMAIL

        }
        if (!FormatValidation.emailFormatValidation(email)) {
            isInvalid = true;
            mappingAttribute.setErrorMessage(bindingResult, ConstantMessage.ERROR_EMAIL_FORMAT_INVALID);
        }

        if (isInvalid)// AGAR KELUAR KEDUA VALIDASI INI DI NOTIFIKASI NYA
        {
            return ViewPath.AUTH_FORGET_PWD_EMAIL;
        }

        objectMapper = authService.sendMailForgetPwd(email, request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return ViewPath.REDIRECT_LOGOUT;
        }
        Boolean isSuccess = (Boolean) objectMapper.get("success");
        ForgetPasswordDTO nextForgetPasswordDTO = new ForgetPasswordDTO();
        if (isSuccess) {
            mappingAttribute.setAttribute(model, objectMapper);
            nextForgetPasswordDTO.setEmail(email);
            model.addAttribute("forgetpassword", nextForgetPasswordDTO);
            return ViewPath.AUTH_FORGET_PWD_VERIFIKASI;
        } else {
            model.addAttribute("forgetpassword", nextForgetPasswordDTO);
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            return ViewPath.AUTH_FORGET_PWD_EMAIL;
        }

    }

    @GetMapping(ServicePath.FORGET_PASSWORD)
    public String getForgetPasswordForm(Model model) {
        model.addAttribute("forgetpassword", new ForgetPasswordDTO());
        return ViewPath.AUTH_FORGET_PWD_EMAIL;
    }

    /*
        VERIFIKASI TOKEN YANG SUDAH DIKIRIM KE EMAIL UNTUK LUPA PASSWORD PROSES YANG PERTAMYA KALI
     */
    @PostMapping(ServicePath.VERIFY_TOKEN_FORGET_PASSWORD)
    public String verifyTokenForgetPwd(@ModelAttribute("forgetpassword")
                                       @Valid ForgetPasswordDTO forgetPasswordDTO,
                                       BindingResult bindingResult
            , Model model
            , WebRequest request
    ) {

        String email = forgetPasswordDTO.getEmail();
        String token = forgetPasswordDTO.getToken();
        int intTokenLength = token.length();
        boolean isValid = true;
        boolean isInvalidFlow = false;

        if (email == null || email.equals("")) {
            isInvalidFlow = true;// KALAU SUDAH KOSONG ARTINYA DIA GAK AKAN PERNAH BISA MELANJUTKAN PROSES JADI SEBAIKNYA DILOGOUT SAJA
        }
        if (!FormatValidation.emailFormatValidation(email)) {
            isInvalidFlow = true;// KALAU SUDAH KOSONG ARTINYA DIA GAK AKAN PERNAH BISA MELANJUTKAN PROSES JADI SEBAIKNYA DILOGOUT SAJA
        }

        if (isInvalidFlow) {
            return "redirect:/check/logout";
        }

        /*START VALIDATION*/
        if (intTokenLength != 6) {
            mappingAttribute.setErrorMessage(bindingResult, ConstantMessage.ERROR_TOKEN_INVALID);
            isValid = false;
        }
        if (token == null || token.equals("")) {
            mappingAttribute.setErrorMessage(bindingResult, ConstantMessage.ERROR_TOKEN_IS_EMPTY);
            isValid = false;
        }

        if (!isValid) {
            model.addAttribute("forgetpwd", forgetPasswordDTO);
            return "auth/forget-pwd-verifikasi";
        }/*END OF VALIDATION*/

        objectMapper = authService.confirmTokenForgotPwd(forgetPasswordDTO, request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID)) {
            return "redirect:/check/logout";
        }

        Boolean isSuccess = (Boolean) objectMapper.get("success");
        if (isSuccess) {
            ForgetPasswordDTO nextForgetPasswordDTO = new ForgetPasswordDTO();
            mappingAttribute.setAttribute(model, objectMapper);
            nextForgetPasswordDTO.setEmail(email);
            model.addAttribute("forgetpwd", nextForgetPasswordDTO);
            return "auth/auth-forget-pwd";
        } else {
            model.addAttribute("forgetpwd", forgetPasswordDTO);
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            return "auth/forget-pwd-verifikasi";
        }
    }

    /*
        LOGIC UNTUK COMPARE PASSWORD LAMA , PASSWORD BARU DAN PASSWORD KONFIRMASI
     */
    @PostMapping(ServicePath.VERIFY_FORGET_PASSWORD)
    public String verifyForgetPwd(@ModelAttribute("forgetpassword")
                                  @Valid ForgetPasswordDTO forgetPasswordDTO,
                                  BindingResult bindingResult
            , Model model
            , WebRequest request
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("forgetpassword", forgetPasswordDTO);
            return ViewPath.AUTH_FORGET_PWD;
        }

        String emailz = forgetPasswordDTO.getEmail();

        if (emailz == null || emailz.equals("")) {
            return ViewPath.REDIRECT_LOGOUT;
        }

        objectMapper = authService.confirmPasswordChange(forgetPasswordDTO, request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID)) {
            return ViewPath.REDIRECT_LOGOUT;
        }

        Boolean isSuccess = (Boolean) objectMapper.get("success");
        if (isSuccess) {
            mappingAttribute.setAttribute(model, objectMapper);
            model.addAttribute("user", new UserDTO());
            return ViewPath.LOGIN;
        } else {
            model.addAttribute("forgetpassword", forgetPasswordDTO);
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            return ViewPath.AUTH_FORGET_PWD;
        }
    }

    /*
        API GENERATE NEW TOKEN UNTUK LUPA PASSWORD PROSES SELANJUTNYA SETELAH PROSES KIRIM TOKEN YANG PERTAMA
     */
    @GetMapping(ServicePath.REQUEST_TOKEN_FORGET_PASSWORD)
    public String requestTokenForgetPwd(@ModelAttribute("forgetpassword")
                                        @Valid ForgetPasswordDTO forgetPasswordDTO,
                                        BindingResult bindingResult,
                                        Model model,
                                        @RequestParam String email,
                                        WebRequest request) {
        forgetPasswordDTO.setToken("");//DIKOSONGKAN UNTUK MENGHILANGKAN INPUTAN USER DI FIELD TOKEN
        forgetPasswordDTO.setEmail(email);

        /*
            API GENERATE NEW TOKEN UNTUK LUPA PASSWORD PROSES SELANJUTNYA SETELAH PROSES KIRIM TOKEN YANG PERTAMA
        */
        String forgetDTOEmail = forgetPasswordDTO.getEmail();
        if (forgetDTOEmail == null || forgetDTOEmail.equals("")) {
            return ViewPath.REDIRECT_LOGOUT;
        }

        objectMapper = authService.getNewToken(forgetDTOEmail, request);
        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID)) {
            return ViewPath.REDIRECT_LOGOUT;
        }
        Boolean isSuccess = (Boolean) objectMapper.get("success");
        if (isSuccess) {
            model.addAttribute("forgetPwd", forgetPasswordDTO);
            mappingAttribute.setAttribute(model, objectMapper);
            return ViewPath.AUTH_FORGET_PWD_VERIFIKASI;
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            model.addAttribute("forgetPwd", forgetPasswordDTO);
            return ViewPath.LOGIN;
        }
    }

    @GetMapping(ServicePath.LOGOUT)
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return ViewPath.REDIRECT_SLASH;
    }
}