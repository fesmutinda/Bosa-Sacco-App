/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.forms;

import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.getCurrentForm;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.TextModeLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

/**
 *
 * @author Festus
 */
public class MemberInfoForm {
            
    private String memberNoLabel = "";
    private String nameLabel = "";
    private String idNoLbl = "";
    private String staffNoLbl = "";
    private String emailAddLbl = "";
    private String Status = "";
    private String Mobile = "";
    private String PhoneNumber = "";
    
    public void showMemberScreen() throws IOException {
        showDemo("Member Details", createProfileTab());
    }
    
    public Container createProfileTab() throws IOException
    {
        Container namesC = new Container(new TextModeLayout(6, 1));
        
        Button profileIcon = new Button();
        Style closeStyle = profileIcon.getAllStyles();
        closeStyle.setFgColor(0xffffff);
        closeStyle.setBgTransparency(0);
        closeStyle.setPaddingUnit(Style.UNIT_TYPE_DIPS);
        closeStyle.setPadding(3, 3, 3, 3);
        closeStyle.setBorder(RoundBorder.create().shadowOpacity(100));
        FontImage.setMaterialIcon(profileIcon, FontImage.MATERIAL_PERSON_PIN);
        
        Container profileNamesDisplay = LayeredLayout.encloseIn(namesC, FlowLayout.encloseCenter(profileIcon));
        Style boxStyle = namesC.getUnselectedStyle();
        boxStyle.setBgTransparency(255);
        boxStyle.setBgColor(0xeeeeee);
        boxStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        boxStyle.setPaddingUnit(Style.UNIT_TYPE_DIPS);
        boxStyle.setMargin(4, 3, 2, 2);
        boxStyle.setPadding(2, 2, 1, 1);
        
        Container mainProfile = BoxLayout.encloseY
        (
            profileNamesDisplay
        );
        
        String str_content =(String) Storage.getInstance().readObject("MemberProfile.json");
        Log.p("Reading >>>>>>>>>>>>>>\n"+str_content+"\n<<<<<<<<<<< .json from storage.", 1);

        byte[] bytes_content = str_content.getBytes();
        
        JSONParser parser = new JSONParser();

        //Parse the JSON
        try(Reader is_content = new InputStreamReader(new ByteArrayInputStream(bytes_content), "UTF-8"))
        {
            Map<String, Object> data = parser.parseJSON(is_content); 

            Map<String, Object> member = (Map<String, Object>)data.get("member");

            memberNoLabel = (String)member.get("mNo");
            nameLabel = (String)member.get("name");
            idNoLbl = (String)member.get("nationalId");
            PhoneNumber = (String)member.get("mobileNo");
            Status = (String)member.get("status");
        }
                
        int spaceIndex = nameLabel.indexOf(" "); 
        String firstName = nameLabel.substring(0, spaceIndex);

        String maskedPhoneNumber = maskPhoneNumber(PhoneNumber);

        String AccountNumber = "12345";
        String maskedAccountNumber = maskAccountNumber(AccountNumber);
                
        String Account_Type = "Savings Acc";

        namesC.add(new Label(firstName, "profileName"));
        namesC.add(new Label("BOSA Sacco", "saccoName"));     


        mainProfile.addComponent(BorderLayout.east(new Label(memberNoLabel, "detailHead")).
                        add(BorderLayout.WEST, new Label("Member Number", "detailHead")));
        mainProfile.add(createSeparator());
        mainProfile.addComponent(BorderLayout.east(new Label(maskedAccountNumber, "detailHead")).
                        add(BorderLayout.WEST, new Label("Account Number", "detailHead")));
        mainProfile.add(createSeparator());
        mainProfile.addComponent(BorderLayout.east(new Label(maskedPhoneNumber, "detailHead")).
                        add(BorderLayout.WEST, new Label("Phone Number", "detailHead")));
        mainProfile.add(createSeparator());
        mainProfile.addComponent(BorderLayout.east(new Label(Account_Type, "detailHead")).
                        add(BorderLayout.WEST, new Label("Account Type", "detailHead")));
        mainProfile.add(createSeparator());

        mainProfile.setUIID("LabelContainer");
        mainProfile.setScrollableY(true);
        mainProfile.setScrollVisible(false);
        
        return BoxLayout.encloseY(mainProfile);
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
    
    public static String maskAccountNumber(String accountNumber) {

        String prefix = accountNumber.substring(0, accountNumber.length() - 4);
        // Replace the last 4 characters with asterisks
        String maskedAccountNumber = prefix + "****";

        return maskedAccountNumber;
    }
    
    protected void showDemo(String title, Component content){
        Form demoForm = new Form(title, new BorderLayout());
        content.setUIID("ComponentDemoContainer");
        Toolbar toolbar = demoForm.getToolbar();

        Form previous = getCurrentForm();
        toolbar.setBackCommand("", Toolbar.BackCommandPolicy.AS_ARROW,
            e -> previous.showBack());

        demoForm.add(BorderLayout.CENTER, content);
        demoForm.show();
    }
    private Label createSeparator() {
      Label sep = new Label(" ");
      sep.setUIID("Separator");
      sep.setShowEvenIfBlank(true);
      return sep;
    }
}
