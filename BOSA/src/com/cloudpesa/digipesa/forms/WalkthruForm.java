/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.forms;

import com.cloudpesa.digipesa.signin.LoginForm;
import com.codename1.components.SpanLabel;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;

/**
 *
 * @author Festus
 */
public class WalkthruForm 
{
    public void showWalkthruForm()
    {
        Form Walkthru_Form = new Form();
        Walkthru_Form.setLayout(new LayeredLayout());

        Walkthru_Form.getTitleArea().removeAll();
        Walkthru_Form.getTitleArea().setUIID("Container");
        
        Walkthru_Form.setTransitionOutAnimator(CommonTransitions.createUncover(CommonTransitions.SLIDE_HORIZONTAL, true, 400));
        
        Tabs walkthruTabs = new Tabs();
        walkthruTabs.setUIID("Container");
        walkthruTabs.getContentPane().setUIID("Container");
        walkthruTabs.getTabsContainer().setUIID("Container");
        walkthruTabs.hideTabs();
        
        Font materialFont = FontImage.getMaterialDesignFont();
        int w = Display.getInstance().getDisplayWidth()/3;
        int hTabs = Display.getInstance().getDisplayWidth() - 2;
        
        FontImage accountImage = FontImage.createFixed("\ue84f", materialFont, 0xffffff, w, w);
        FontImage withdrawImage = FontImage.createFixed("\ue850", materialFont, 0xffffff, w, w);
        FontImage transferImage = FontImage.createFixed("\ue8dd", materialFont, 0xffffff, w, w);
        FontImage transactionHistoryImage = FontImage.createFixed("\ue8ef", materialFont, 0xffffff, w, w);
        FontImage mapImage = FontImage.createFixed("\ue55b", materialFont, 0xffffff, w, w);
        
        Label notesPlaceholder = new Label("","ProfilePic");
        Label notesLabel = new Label(accountImage, "ProfilePic");
        Component.setSameHeight(notesLabel, notesPlaceholder);
        Component.setSameWidth(notesLabel, notesPlaceholder);
        
        Label bottomSpace = new Label();
        
        Container tab1 = BorderLayout.centerAbsolute(BoxLayout.encloseY
        (
                notesPlaceholder,
                new Label("DigiPesa", "WalkthruWhite"),
                new SpanLabel("" +
                                            "The most Agile Dynamic Mobile " +
                                            "Banking Solution",  "WalkthruBody"),
                bottomSpace
        ));
        tab1.setUIID("WalkthruTabs");
        tab1.setWidth(hTabs);
        
        walkthruTabs.addTab("", tab1);
        
        Label bottomSpaceTab3 = new Label("");
        Container tab3 = BorderLayout.centerAbsolute(BoxLayout.encloseY(
                new Label(transferImage, "ProfilePic"),
                new Label("Deposit", "WalkthruWhite"),
                new SpanLabel("Transfer money from your MPESA to your accounts" +
                                            "",  "WalkthruBody"),
                bottomSpaceTab3
        ));
        
        tab3.setUIID("WalkthruTabs");
        tab3.setWidth(hTabs);

        walkthruTabs.addTab("", tab3);
        
        Label bottomSpaceTab4 = new Label("");
        Container tab4 = BorderLayout.centerAbsolute(BoxLayout.encloseY(
                new Label(transactionHistoryImage, "ProfilePic"),
                new Label("DigiPesa E Loan", "WalkthruWhite"),
                new SpanLabel("" +
                                            "Save consistently for six months and earn qualify for a DigiPesa E loan " +
                                            "",  "WalkthruBody"),
                bottomSpaceTab4
        ));
        
        tab4.setUIID("WalkthruTabs");

        walkthruTabs.addTab("", tab4);
        
        Walkthru_Form.add(walkthruTabs);
        
        RadioButton firstTab = new RadioButton("");
        RadioButton secondTab = new RadioButton("");
        RadioButton thirdTab = new RadioButton("");
        
        firstTab.setUIID("Container");
        secondTab.setUIID("Container");
        thirdTab.setUIID("Container");
        
        new ButtonGroup(firstTab, secondTab, thirdTab);
        firstTab.setSelected(true);
        Container tabsFlow = FlowLayout.encloseCenter(firstTab, secondTab, thirdTab);
        walkthruTabs.addSelectionListener((i1, i2) -> {
        switch(i2) {
            case 0:
                if(!firstTab.isSelected()) {
                    firstTab.setSelected(true);
                }
                break;
            case 1:
                if(!secondTab.isSelected()) {
                    secondTab.setSelected(true);
                }
                break;
            case 2:
                if(!thirdTab.isSelected()) {
                    thirdTab.setSelected(true);
                }
                break;
         }
    });
                
        Button skip = new Button("Proceed");
        skip.setUIID("SkipButton");
        skip.addActionListener(e -> 
        {
            String Pin = "";
                String SaccoID = "00032";
                String smsCode = "";
                String key = ":" + smsCode + ":" + Pin + ":" + SaccoID;

                Storage.getInstance().writeObject("Key", key);
                Storage.getInstance().writeObject("SaccoID", SaccoID);
                Storage.getInstance().writeObject("PhoneNumber", "");
                Storage.getInstance().writeObject("Pin", "");
                Storage.getInstance().writeObject("SMScode", smsCode);
                
                new LoginForm().show();
        });
                
                
        
        Container southLayout = BoxLayout.encloseY(tabsFlow,skip);
        Walkthru_Form.add(BorderLayout.south(southLayout));
        
        Component.setSameWidth(bottomSpace, bottomSpaceTab3, bottomSpaceTab4, southLayout);
        Component.setSameHeight(bottomSpace, bottomSpaceTab3, bottomSpaceTab4, southLayout);
        
        // visual effects in the first show
        Walkthru_Form.addShowListener(e -> 
        {
            notesPlaceholder.getParent().replace(notesPlaceholder, notesLabel, CommonTransitions.createFade(1500));
        });
        Walkthru_Form.showBack();

    }
}
