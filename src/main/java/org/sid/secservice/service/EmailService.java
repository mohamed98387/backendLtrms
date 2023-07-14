package org.sid.secservice.service;

import org.sid.secservice.entities.Mail;

public interface EmailService {
    public void sendCodeByMail(Mail mail);
}
