/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.forms;

import com.cloudpesa.digipesa.signin.ChangePinForm;
import com.cloudpesa.digipesa.loans.LoanBalancesForm;
import com.cloudpesa.digipesa.loans.LoanGuaranteedForm;
import com.cloudpesa.digipesa.loans.LoanGuarantors;
import com.cloudpesa.digipesa.loans.LoanRepaymentForm;
import com.cloudpesa.digipesa.wendani.account.Ministatement;
import com.codename1.components.ScaleImageButton;
import com.codename1.components.ScaleImageLabel;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.BOTTOM;
import static com.codename1.ui.CN.convertToPixels;
import static com.codename1.ui.CN.isTablet;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Style;
import static com.codename1.ui.util.Resources.getGlobalResources;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/**
 *
 * @author Festus
 */
public class HomePageForm 
{
    private String greating = "Hello Member";
    private Boolean bool_firstname;
    private String fullName;
    private String token = "";
    
    public void showMain()
    {
        try {
            BosaDemoHome();
        } catch (IOException ex) {
        }
    }
    
    public void BosaDemoHome() throws UnsupportedEncodingException, IOException
    {
        Form homeform = new Form("Dashboard", new BoxLayout(BoxLayout.Y_AXIS));
        Container finalContainer = new Container(new GridLayout(5, 2));
        Container contentpane2 = homeform.getContentPane();
        contentpane2.setUIID("MainWindowContainer");
        contentpane2.setScrollableY(true);
        
        Button menuButton = new Button("");
        menuButton.setUIID("menuButton");
        FontImage.setMaterialIcon(menuButton, FontImage.MATERIAL_MENU);
        menuButton.addActionListener(e -> homeform.getToolbar().openSideMenu());

        Toolbar tb = homeform.getToolbar();
        tb.setTitleCentered(false);
        homeform.setTransitionOutAnimator(CommonTransitions.createUncover(CommonTransitions.SLIDE_HORIZONTAL, true, 400));
        
        if (!isTablet()){
            ScaleImageLabel sideIcon = new ScaleImageLabel(getGlobalResources().getImage("profile.png"));
            sideIcon.setUIID("SideMenuIcon");
            int size = convertToPixels(20);
            sideIcon.setPreferredH(size);
            sideIcon.setPreferredW(size);
            Container sideMenuHeader = BoxLayout.encloseY(sideIcon, new Label("BOSA Sacco", "SideMenuHeader"));
            sideMenuHeader.setUIID("SidemenuTop");
            tb.addComponentToSideMenu(sideMenuHeader);  //SidemenuTop
        }
        
        tb.addMaterialCommandToSideMenu("  Change PIN", FontImage.MATERIAL_VPN_KEY,  e -> new ChangePinForm().changepinScreen());
        tb.addMaterialCommandToSideMenu("  My Profile", FontImage.MATERIAL_PERSON,  e -> {
            try {
                new MemberInfoForm().showMemberScreen();
            } catch (IOException ex) {
            }
        });
        tb.addMaterialCommandToSideMenu("  Website", FontImage.MATERIAL_WEB,  e -> new FindUmojaWendani().showWebView());
        tb.addMaterialCommandToSideMenu("  Exit", FontImage.MATERIAL_EXIT_TO_APP,  e -> Display.getInstance().exitApplication());
            
        Font materialFont = FontImage.getMaterialDesignFont();
        int w = Display.getInstance().getDisplayWidth()/6;
        int h = Display.getInstance().getDisplayHeight() /9;
        FontImage profileImage = FontImage.createFixed("\ue7ff", materialFont, 0x00a69c, w, w);
        FontImage depositImage = FontImage.createFixed("\ue850", materialFont, 0x00a69c, w, w);
        FontImage statementImage = FontImage.createFixed("\ue235", materialFont, 0x00a69c, w, w);
        FontImage guaImage = FontImage.createFixed("\ue7fc", materialFont, 0x00a69c, w, w);
        
        int b = Display.getInstance().getDisplayWidth()/3;
        Button profileBtn =new Button("Profile", "LoanBtns");
        profileBtn.setTextPosition(BOTTOM);
        profileBtn.setIcon(profileImage);
        profileBtn.addActionListener(e-> { try {
            new MemberInfoForm().showMemberScreen();
            } catch (IOException ex) {
            }
        });
        
        Button minsta =new Button("Ministatements", "LoanBtns");
        minsta.setTextPosition(BOTTOM);
        minsta.setIcon(statementImage);
        minsta.addActionListener(e-> {
            new Ministatement().showThis();
        });
        
        Button balBtn =new Button("Balances", "LoanBtns");
        balBtn.setTextPosition(BOTTOM);
        balBtn.setIcon(statementImage);
        balBtn.addActionListener(e-> {
            new BalancesForm().balancesScreen();
        });
        
        int depositH = Display.getInstance().getDisplayHeight()/3;
        
        Button cashDepositBtn = new Button("Cash Deposit", "LoanBtns");
        cashDepositBtn.setTextPosition(BOTTOM);
        cashDepositBtn.setIcon(depositImage);
        cashDepositBtn.setHeight(depositH);
        cashDepositBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new MainDepositpage().showdepo();
            }
        });
        FontImage loanRepaymentImage = FontImage.createFixed("\ue862", materialFont, 0x00a69c, w, w);
        FontImage loanGuaranteedImage = FontImage.createFixed("\ue7f0", materialFont, 0x00a69c, w, w);
        FontImage loanBalancesImage = FontImage.createFixed("\ue85d", materialFont, 0x00a69c, w, w);
        
        Button loanBalancesBtn = new Button("Loan Balances", "LoanBtns");
        loanBalancesBtn.setTextPosition(BOTTOM);
        loanBalancesBtn.setIcon(loanBalancesImage);
        loanBalancesBtn.addActionListener(e-> {
            new LoanBalancesForm().showLoanBalancesScreen();
        });
        
        Button loanRepaymentBtn = new Button("Loan Repayment", "LoanBtns");
        loanRepaymentBtn.setTextPosition(BOTTOM);
        loanRepaymentBtn.setIcon(loanRepaymentImage);
        loanRepaymentBtn.addActionListener(e-> {
            try {
                new LoanRepaymentForm().repaymentScreen();
            } catch (IOException ex) {
            }
        });
        
        Button loanGuaranteedBtn =new Button("Loan Guaranteed", "LoanBtns");
        loanGuaranteedBtn.setTextPosition(BOTTOM);
        loanGuaranteedBtn.setIcon(loanGuaranteedImage);
        loanGuaranteedBtn.addActionListener(e-> {
            try {
                new LoanGuaranteedForm().showLoanGuaranteedScreen();
            } catch (IOException ex) {
            }
        });
         
        Button loanGuarantorBtn = new Button("Guarantors", "LoanBtns");
        loanGuarantorBtn.setTextPosition(BOTTOM);
        loanGuarantorBtn.setIcon(guaImage);
        loanGuarantorBtn.addActionListener(e-> {
            try {
                new LoanGuarantors().showLoanGuarantorsScreen();
            } catch (IOException ex) {
            }
        });
        
        Container accountsContainer = new Container(new GridLayout(2, 2));
        accountsContainer.add(profileBtn);
        accountsContainer.add(balBtn); 
        accountsContainer.add(minsta); 
        accountsContainer.add(cashDepositBtn); 
        accountsContainer.add(loanBalancesBtn);
        accountsContainer.add(loanGuaranteedBtn);
        accountsContainer.add(loanGuarantorBtn);
        accountsContainer.setScrollableY(true);
        accountsContainer.setScrollVisible(false);
        
        Container mainTab1 = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        mainTab1.add(createComponent(getGlobalResources().getImage("profile.png"),
                                greeting(),"What would you like to do today?",
                                null));
        mainTab1.addAll(new Label("Account Menu", "TabLabel2"),accountsContainer);
        mainTab1.setScrollableY(true);
        mainTab1.setScrollVisible(false);
        
        homeform.add(mainTab1);
        homeform.setScrollableY(false);
        homeform.setScrollVisible(false);
        
        homeform.show();
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
    
    public Component createComponent(Image image, String header, String firstLine, ActionListener listener){
        Container demoContent = new AccordionComponent(image, header, firstLine, listener);
        return demoContent;
    }
    private class AccordionComponent extends Container{
        private boolean isOpen = false;
        private Button firstLine;
        
        private AccordionComponent(Image image, String header, String firstLine, ActionListener listener) {
            super(new BorderLayout());
            this.firstLine = new Button(firstLine, "DemoContentBody");
            this.firstLine.addActionListener(listener);

            setUIID("DemoContentAccordion");
            ScaleImageButton contentImage = new ScaleImageButton(image){
                @Override
                protected Dimension calcPreferredSize() {

                    Dimension preferredSize =  super.calcPreferredSize();
                    preferredSize.setHeight(Display.getInstance().getDisplayHeight() / 10);
                    return preferredSize;
                }
            };
            contentImage.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED);
            contentImage.addActionListener(listener);
            contentImage.setUIID("DemoContentImage");
            Button contentHeader = new Button(header, "DemoContentHeader");
            contentHeader.addActionListener(listener);

            Container cnt = new Container(new BorderLayout());
            cnt.add(BorderLayout.NORTH, contentImage);
            cnt.add(BorderLayout.WEST, BoxLayout.encloseY(contentHeader, this.firstLine));
            add(BorderLayout.NORTH, cnt);
        }
    }
}
