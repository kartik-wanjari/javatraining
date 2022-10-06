/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package zipper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

public class Mail {
    private static final Logger logger = LogManager.getLogger(Mail.class);

    public void sendMail(String receiver) {
        logger.info("Invoking mail");
        //configure properties
        logger.info("Configuring mail properties");
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.debug", true);

        logger.info("Creating session");
        Session session = Session.getInstance(props);
        logger.info("Constructing message");
        // Construct the message
        String from = "wanjarik70@gmail.com";
        String subject = "System Generated Reports for " + LocalDate.now();
        Message msg = new MimeMessage(session);
        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        MimeBodyPart mimeBodyPart = new MimeBodyPart();

        try {
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            msg.setSubject(subject);

            logger.info("Adding attachments");
            //add attachment to mail
            attachmentBodyPart.attachFile(new File("/Users/azuga/Desktop/reports.zip"));

            String body = "Hello team,"+"\n\n"+"Here are the charts and converted format reports."+"\n\n"+"Regards,"+"\n"+"Kartik Wanjari";

            mimeBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(attachmentBodyPart);
            msg.setContent(multipart);
            logger.info("mail constructed");
            // Send the message.

            Transport.send(msg,"wanjarik70@gmail.com","hljb vjhs stfh qekx");

            logger.info("Email sent to {}", receiver);
        } catch (IOException e) {
            logger.error("IOException: " + e);
        } catch (MessagingException e) {
            logger.error("MessagingException: " + e);
        }


    }
}
