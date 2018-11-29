package pl.przemek.Message;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.jms.JMSException;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailUtils {

    private static Logger logger=Logger.getLogger(EmailUtils.class.getName());

    static Transport getTransportProtocol(Session mailSession,javax.mail.Message mailMessage, String protocol, String host) throws MessagingException, JMSException {
        Transport transport=mailSession.getTransport(protocol);
        transport.addTransportListener(new TransportListener());
        transport.connect(host,EmailInformation.getLoginSenderEmail(),EmailInformation.getPasswordSenderEmail());
        return transport;
    }
    static Session createEmailSession(){
        Session mailSession= Session.getInstance(EmailInformation.getEmailSendingInformation(),null);
        mailSession.setDebug(true);
        return mailSession;
    }

    static javax.mail.Message createMailMessage(Session mailSession, MessageWrapper msg) throws MessagingException, IOException {
        Message mailMessage=prepareMailWithoutContent(mailSession,msg);
        mailMessage.setSubject("Email");
        mailMessage.setContent(createBodyOfEmail(msg.getMessage(),msg.getPublicKey()));
        return mailMessage;
    }

    static Message createFailureMailMessage(Session mailSession,MessageWrapper msg) throws MessagingException {
        Message mailMessage=prepareMailWithoutContent(mailSession,msg);
        mailMessage.setSubject("Failure");
        mailMessage.setContent(createBodyOfFailureEmail(FailureEmailMessageTemplate.getPreparedMessage()));
        return mailMessage;
    }

    private static Message prepareMailWithoutContent(Session mailSession, MessageWrapper msg) throws MessagingException {
        javax.mail.Message mailMessage=new MimeMessage(mailSession);
        mailMessage.setFrom(new InternetAddress(EmailInformation.getLoginSenderEmail()));
        mailMessage.setRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(msg.getUser().getEmail()));
        return mailMessage;
    }

    private static Multipart createBodyOfEmail(String message, String publicKey) throws MessagingException {
        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        BodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart.setContent(message,"text/html");
        File file=createFile(publicKey);
        DataSource source = new FileDataSource(file);
        messageBodyPart1.setDataHandler(new DataHandler(source));
        messageBodyPart1.setFileName("File");
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(messageBodyPart1);
        return multipart;
    }
    static Multipart createBodyOfFailureEmail(String message) throws MessagingException {
        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message,"text/html");
        multipart.addBodyPart(messageBodyPart);
        return multipart;
    }
    private static File createFile(String publicKey){
        File file= null;
        FileOutputStream outputStream=null;
        try {
            file = File.createTempFile("PublicKey",".txt");
            outputStream = new FileOutputStream(file);
            outputStream.write(publicKey.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE,"[EmailUtils] createFile()",e);
        }finally {
            try {
                if(outputStream!=null)
                    outputStream.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE,"[EmailUtils] createFile() close outputStream",e);
            }
        }
        return file;
    }
}