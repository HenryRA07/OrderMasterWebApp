package edu.unl.cc.ordermaster.util;


import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

public class Gmail {
    private final static String EMAIL = "arevalofranz2007@gmail.com";
    private final static String PASS = "bkpf eyga ekbj kxxb";
    private String destinatario;
    private String asunto;
    private String contenido;
    private LocalDate fecha;

    //Session
    private Session sesion;

    private void propiedadesEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.user", EMAIL);
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        props.setProperty("mail.smtp.auth", "true");

        Authenticator auth = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication (){
                return new PasswordAuthentication(EMAIL, PASS);
            }
        };
        sesion = Session.getInstance(props, auth);
    }

    public void enviarEmailGmail(String destinatario, String asunto, String contenido, File pdf){
        propiedadesEmail();
        try {
            MimeMessage mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(EMAIL));
            mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            mensaje.setSubject(this.asunto);
            mensaje.setSentDate(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            MimeBodyPart cuerpoMensaje = new MimeBodyPart();
            cuerpoMensaje.setContent(contenido, "text/html;charset=utf-8");
            Multipart multiparte = new MimeMultipart();
            multiparte.addBodyPart(cuerpoMensaje);
            MimeBodyPart adjunto = new MimeBodyPart();
            adjunto.attachFile(pdf);
            multiparte.addBodyPart(adjunto);
            mensaje.setContent(multiparte);
            Transport.send(mensaje);
        } catch (AddressException e) {
            System.out.println("error AddressException: "+e.getMessage());
        } catch (MessagingException ex) {
            System.out.println("error MessagingException: " + ex.getMessage());
        } catch (IOException e) {
            System.out.println("error IOException: "+e.getMessage());
        }
    }
}
