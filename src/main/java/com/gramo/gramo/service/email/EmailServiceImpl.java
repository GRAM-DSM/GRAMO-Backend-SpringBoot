package com.gramo.gramo.service.email;

import com.gramo.gramo.entity.verifynumber.VerifyNumber;
import com.gramo.gramo.entity.verifynumber.VerifyNumberRepository;
import com.gramo.gramo.exception.MailSendError;
import com.gramo.gramo.payload.request.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private static final Random RANDOM = new Random();
    private final JavaMailSender javaMailSender;
    private final VerifyNumberRepository verifyNumberRepository;

    @Override
    public void sendAuthNumEmail(EmailRequest request) {
        String authNum = generateVerifyNumber();

        try {
            final MimeMessagePreparator preparator = mimeMessage -> {
                final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setFrom("gramo@gmail.com");
                helper.setTo(request.getEmail());
                helper.setSubject("인증번호는 " + generateVerifyNumber() + " 입니다");
            };

            javaMailSender.send(preparator);

            verifyNumberRepository.save(
                    VerifyNumber.builder()
                            .email(request.getEmail())
                            .verifyNumber(authNum)
                            .build()
            );

        } catch (Exception e) {
            throw new MailSendError();
        }
    }

    private String generateVerifyNumber() {
        RANDOM.setSeed(System.currentTimeMillis());
        return Integer.toString(RANDOM.nextInt(1000000) % 1000000);
    }

}
