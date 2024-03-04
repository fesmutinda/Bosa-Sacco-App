/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.signin;

import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.layouts.BorderLayout.SOUTH;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.TextModeLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;
import java.util.Calendar;

/**
 *
 * @author Festus
 */
public class LoginForm extends Form{
    private int iter = 0;
    Image withd_icon = FontImage.createMaterial(FontImage.MATERIAL_BACKSPACE, UIManager.getInstance().getComponentStyle("MultiIcon"));
    private TextField phoneText = new TextField("", " Phone Number", 5, (TextArea.PHONENUMBER));
    private TextField pinText = new TextField("", " M-banking pin", 5, (TextArea.NUMERIC | TextArea.PASSWORD));
    
    private String greating = "Hello there, Welcome", nameLabel;
    private Boolean bool_firstname;
    private String fullName;
    private String token = "";
        
    public LoginForm(){
        super(new BorderLayout());
        
        Command backCommand = Command.create("", FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, UIManager.getInstance().getComponentStyle("cartTitleCommand")),
                (ee ->{
            if (iter == 3){
                Display.getInstance().exitApplication();
            }else{
                ToastBar.Status saved = ToastBar.getInstance().createStatus();
                saved.setMessage("press again to exit");
                saved.showDelayed(4000);
                saved.setExpires(2000);
                saved.show();
                iter ++;
            }
        }));
        setBackCommand(backCommand);
        
        getToolbar().hideToolbar();
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
        
        Button social = new Button("FORGOT PIN", "ConnectWithSocialButton");
        social.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Dialog.show("Pin Recovery", "Please conduct or visit our Sacco offices and explain why you need to reset your pin", "Ok", "Cancel");
            }
        });
        
        Container box = new Container(BoxLayout.y());
        box.setScrollableY(true);
        
        Container salamuContainer = new Container(new TextModeLayout(6, 1));
        salamuContainer.add(new Label(greeting(), "saccoName"));
        
        Button profileIcon = new Button();
        Style closeStyle = profileIcon.getAllStyles();
        closeStyle.setFgColor(0xffffff);
        closeStyle.setBgTransparency(0);
        closeStyle.setPaddingUnit(Style.UNIT_TYPE_DIPS);
        closeStyle.setPadding(3, 3, 3, 3);
        closeStyle.setBorder(RoundBorder.create().shadowOpacity(100));
        FontImage.setMaterialIcon(profileIcon, FontImage.MATERIAL_PERSON_PIN);
        
        Container topContainer = LayeredLayout.encloseIn(salamuContainer, FlowLayout.encloseCenter(profileIcon));
        Style boxStyle = salamuContainer.getUnselectedStyle();
        boxStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        boxStyle.setPaddingUnit(Style.UNIT_TYPE_DIPS);
        boxStyle.setMargin(4, 3, 2, 2);
        boxStyle.setPadding(2, 2, 1, 1);
        
        box.add(_spaceLabel);
        box.add(topContainer);
        
        Button confirmButton = new Button("Confirm", "cBtns");
        Container cnt2 = BoxLayout.encloseY(new SpanLabel("Enter your phone number."), phoneText, new SpanLabel("Enter your pin."), pinText, confirmButton);
        cnt2.setUIID("LabelContainer");
        
        box.add( cnt2);
        add(CENTER, box);
        add(SOUTH, BoxLayout.encloseXCenter(social));
        Validator v = new Validator();
        v.addConstraint(pinText, new LengthConstraint(4));
        v.addConstraint(phoneText, new LengthConstraint(9));
        v.addSubmitButtons(confirmButton);
        
        confirmButton.addActionListener(e -> 
        {
           String pin = pinText.getText();
           String phone = phoneText.getText();
           new EnterSMS(phone, pin).show();
        });
    }
    
    public String greeting(){
        String greeting = "Hello there, Welcome";
        bool_firstname = Storage.getInstance().exists("HazinaName");
        
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        
        if (hourOfDay >= 0 && hourOfDay < 12) {
            greeting = "Good morning";
        } else if (hourOfDay >= 12 && hourOfDay < 17) {
            greeting = "Good afternoon";
        } else {
            greeting = "Good evening";
        }
        
        if(bool_firstname)
        {
            fullName = "Festus Mutinda";
            int spaceIndex = fullName.indexOf(" "); // find the first space character
            String firstName = fullName.substring(0, spaceIndex); // extract the first word
            
            greeting = greeting+", "+firstName;
        }
        return greeting;
    }
}
