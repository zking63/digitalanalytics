package com.coding.LojoFundrasing.Services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.coding.LojoFundrasing.Models.User;
import com.coding.LojoFundrasing.Repos.UserRepo;

import net.bytebuddy.utility.RandomString;

@Service
public class UserService {
	@Autowired
	private UserRepo urepo;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Environment environment;
 
    @Transactional(rollbackFor = Exception.class)
    public boolean verify(String code, String email) {
		 System.out.println(code);
		 System.out.println(email);
        User user = urepo.findByVerificationCode(code, email);
        System.out.println(user.getId());
         
        if (user == null || user.isEnabled()) {
        	 System.out.println("enab");
            return false;
        } else {
        	System.out.println("enabler");
        	urepo.updateEnabled(code);
          	 System.out.println("veri");
          	urepo.updateVerificationCode(code);
     
             

            try {
            	
                urepo.save(user);
                
            } catch (RollbackException e) {
            	e.printStackTrace();
                }
  

         System.out.println("return");
            return true;
        }
         
    }
     
    private void sendVerificationEmail(User user, String page) 
    		throws UnsupportedEncodingException, MessagingException {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";

        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.transport.protocol", "smtp");
        // set any other needed mail.smtp.* properties here
        //Session session = Session.getInstance(props);
      
        // set the message content here
        
       /* props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.imap.ssl.enable", "true");
 
        props.setProperty("mail.smtp.socketFactory.port", "25");
       // props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "localhost");
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);*/
        // set message content here
        
    	
        String fromEmail = "zingsubs@gmail.com";
        String sender = "Fundraising";
        String subject = "Please verify your registration";

 		final String toEmail = user.getEmail(); // can be any email id 
 		
 		System.out.println("TLSEmail Start");
 		//Properties props = new Properties();
 		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
 		props.put("mail.smtp.port", "465"); //TLS Port
 		props.put("mail.smtp.auth", "true"); //enable authentication
 		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
 		
                 //create Authenticator object to pass in Session.getInstance argument
 		Authenticator auth = new Authenticator() {
 			//override the getPasswordAuthentication method
 			protected PasswordAuthentication getPasswordAuthentication() {
 				return new PasswordAuthentication(fromEmail, environment.getProperty("mypassword"));
 			}
 		};
 		Session session = Session.getInstance(props, auth);
 		 //MimeMessage message = new MimeMessage(session);
 		sendEmail(session, toEmail,subject, sender,fromEmail, page + "?code=" + user.getVerificationCode() 
 		+ "&email=" + user.getEmail());
 		
 	}
    public static void sendEmail(Session session, String toEmail, String subject, 
    		String sender, String fromEmail, String body){
		try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");

	      msg.setFrom(new InternetAddress(fromEmail, sender));

	      msg.setReplyTo(InternetAddress.parse(fromEmail, false));

	      msg.setSubject(subject, "UTF-8");

	      msg.setText(body, "UTF-8");

	      msg.setSentDate(new Date());

	      msg.setRecipients(RecipientType.TO, InternetAddress.parse(toEmail, false));
	      System.out.println("Message is ready");
    	  Transport.send(msg);  

	      System.out.println("EMail Sent Successfully!!");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}
    
	//register user
	public User registerUser(User user, String page) throws UnsupportedEncodingException, MessagingException {
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);
	    String randomCode = RandomString.make(64);
	    user.setVerificationCode(randomCode);
	    user.setEnabled(false);
	     
	    urepo.save(user);
	     
	    sendVerificationEmail(user, page);
		return user;
	}
	
	//find user by email
	public User findUserbyEmail(String email) {
		return urepo.findByEmail(email);
	}
	
	//find user by id
	public User findUserbyId(Long id) {
		return urepo.findById(id).orElse(null);
	}
	
	//authenticate user
	public boolean authenticateUser(String email, String password) {
		User user = urepo.findByEmail(email);
		if (user == null){
			return false;
		}
		else {
			if(BCrypt.checkpw(password, user.getPassword())) {
				return true;
			}
			return false;
		}
	}
    public List<User> listAll() {
        return urepo.findAll();
    }
}
