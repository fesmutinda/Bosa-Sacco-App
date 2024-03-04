/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.signin;

import com.cloudpesa.digipesa.forms.HomePageForm;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.getCurrentForm;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;
import java.io.IOException;

/**
 *
 * @author Festus
 */
public class EnterSMS extends Form{
    private TextField smsCode = new TextField("", " SMS Code", 5, TextField.NUMERIC);
    public String response_String;
        
    public EnterSMS(String PhoneNumber, String pin_no){
        super(new BorderLayout());
        String maskedPhoneNumber = maskPhoneNumber(PhoneNumber);
        
        Form previous = getCurrentForm();
        getToolbar().setBackCommand("", Toolbar.BackCommandPolicy.AS_ARROW, e -> previous.showBack());
        Container box = new Container(BoxLayout.y());
        Label spaceLabel;
        if(!Display.getInstance().isTablet() && Display.getInstance().getDeviceDensity() < Display.DENSITY_VERY_HIGH) {
            spaceLabel = new Label();
        } else {
            spaceLabel = new Label(" ");
        }
        Label _spaceLabel;
        if(!Display.getInstance().isTablet() && Display.getInstance().getDeviceDensity() < Display.DENSITY_VERY_HIGH) {
            _spaceLabel = new Label();
        } else {
            _spaceLabel = new Label(" ");
        }
        
        Button loginButton = new Button("Verify OTP", "cBtns");
        Container cnt2 = BoxLayout.encloseY(new SpanLabel("Enter the 6 digit code sent to you at " + maskedPhoneNumber, "FlagButton"), smsCode, loginButton);
        cnt2.setUIID("LabelContainer");
        
        box.add( cnt2);
        add(CENTER, box);
        Validator v = new Validator();
        v.addConstraint(smsCode, new LengthConstraint(4));
        v.addSubmitButtons(loginButton);
        
        loginButton.addActionListener(e -> 
        {
            try {
                new HomePageForm().BosaDemoHome();
            } catch (IOException ex) {
            }
        });
    }
    public static String maskPhoneNumber(String phoneNumber) {
        // Remove leading "+" if present
        phoneNumber = phoneNumber.startsWith("+") ? phoneNumber.substring(1) : phoneNumber;

        if (phoneNumber.length() == 10) {
            // Case: "0743901110"
            String prefix = phoneNumber.substring(0, 2);
            String suffix = phoneNumber.substring(8);
            return prefix + "*****" + suffix;
        } else if (phoneNumber.length() == 12) {
            // Case: "254743901110" or "+254743901110"
            String prefix = phoneNumber.substring(0, 5);  // +2547
            String suffix = phoneNumber.substring(10);
            return prefix + "*****" + suffix;
        } else {
            return phoneNumber;
        }
    }
    
}
