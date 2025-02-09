package com.app.mifi.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;

@Service
public class SendGridEmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.sender.email}")
    private String senderEmail;


    public void sendOtpEmail(String email, String otp) throws IOException{
            String htmlContent = loadEmailTemplate().replace("{{OTP}}", otp);
            Email from = new Email(senderEmail);
            Email to = new Email(email);
            String subject = "🔐 Password Reset OTP";
            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            response.getStatusCode();
        }

    private String loadEmailTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/otp-email-template.html");
        return Files.readString(resource.getFile().toPath());
    }
}
