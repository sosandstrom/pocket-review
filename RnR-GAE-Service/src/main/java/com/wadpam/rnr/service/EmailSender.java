package com.wadpam.rnr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * This class is responsible for sending email.
 * @author mattiaslevin
 */
public class EmailSender {
    static final Logger LOG = LoggerFactory.getLogger(EmailSender.class);

    // Send an email
    public boolean sendEmail(String toEmail, String fromEmail, String subject, String body) {
        LOG.debug("Send email to:{}, subject:{}", toEmail, subject);

        final Session session = Session.getDefaultInstance(new Properties(), null);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            // Catch all exceptions and just log an error, do not interrupt flow
            LOG.error("Not possible to send email with reason:{}", e.getMessage());
            return false;
        }
    }

    public boolean sendEmail(String fromAddress, String fromName,
                             List<String> toAddresses,
                             List<String> ccAddresses,
                             List<String> bccAddresses,
                             String subject,
                             String text, String html,
                             byte[] attachment, String filename, String contentType) {
        LOG.info("Send email to:{}, subject:{}", toAddresses, subject);

        final Session session = Session.getDefaultInstance(new Properties(), null);
        try {
            // Build message
            final Message msg = new MimeMessage(session);

            // Set from address
            final InternetAddress from = new InternetAddress(fromAddress, fromName);
            msg.setFrom(from);
            // Set to address
            if (toAddresses != null) {
                for (String toAddress : toAddresses) {
                    final InternetAddress to = new InternetAddress(toAddress, null);
                    msg.addRecipient(Message.RecipientType.TO, to);
                }
            }

            if (null == toAddresses && null == ccAddresses && null == bccAddresses) {
                LOG.warn("One of to, cc or bcc address must be provided to send email");
                return false;
            }

            // Set cc address
            if (ccAddresses != null) {
                for (String ccAddress : ccAddresses) {
                    final InternetAddress cc = new InternetAddress(ccAddress, null);
                    msg.addRecipient(Message.RecipientType.CC, cc);
                }
            }
            // Set bcc address
            if (bccAddresses != null) {
                for (String bccAddress : bccAddresses) {
                    final InternetAddress bcc = new InternetAddress(bccAddress, null);
                    msg.addRecipient(Message.RecipientType.BCC, bcc);
                }
            }
            // Subject
            msg.setSubject(subject);

            // Check if plain text or multi-part message
            if (null != text) {
                // Plain text
                msg.setText(text);
            } else {
                // Multi-part message
                final Multipart mp = new MimeMultipart();

                // Add body as html
                final MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(html, "text/html");
                mp.addBodyPart(htmlPart);

                // Add attachments if any
                if (null != attachment) {
                    LOG.debug("Add attachment to email");
                    final MimeBodyPart attachmentPart = createBodyPart(attachment, contentType, filename);
                    mp.addBodyPart(attachmentPart);
                }

                msg.setContent(mp);
            }

            msg.saveChanges();
            Transport.send(msg);
            return true;
        } catch (UnsupportedEncodingException ex) {
            LOG.warn("Cannot create InternetAddress when sending email:{}", ex);
            return false;
        } catch (MessagingException me) {
            LOG.warn("Cannot send email with reason:{}", me);
            return false;
        }
    }

    // Build attachment part
    private static MimeBodyPart createBodyPart(byte[] data, String type, String filename) throws MessagingException {
        final MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setFileName(filename);
        ByteArrayDataSource source = new ByteArrayDataSource(data, type);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setHeader("Content-ID", createContentID(filename));
        attachmentPart.setDisposition(MimeBodyPart.INLINE);
        return attachmentPart;
    }

    //
    private static String createContentID(String filename) {
        return String.format("%s_no-reply@bassac.se", filename);
    }

}
