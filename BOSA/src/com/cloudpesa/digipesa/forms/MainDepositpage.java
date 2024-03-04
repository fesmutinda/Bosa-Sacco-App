/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.forms;

import com.cloudpesa.digipesa.transactions.DepostLoans;
import com.cloudpesa.digipesa.transactions.DepositContribution;
import com.cloudpesa.digipesa.transactions.DepositInsurance;
import com.cloudpesa.digipesa.transactions.DepositShares;
import com.codename1.components.MultiButton;
import static com.codename1.ui.CN.getCurrentForm;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;

/**
 *
 * @author Festus
 */
public class MainDepositpage 
{
    public void showdepo()
    {
        showScreen("Cash Deposit", deposit());
    }
    public Container deposit()
    {
        int w = Display.getInstance().getDisplayWidth()/6;
        
        Image depoLoans_icon = FontImage.createMaterial(FontImage.MATERIAL_SEND, UIManager.getInstance().getComponentStyle("MultiIcon"));
        depoLoans_icon.scaledWidth(w);
        Image depo_iconww = FontImage.createMaterial(FontImage.MATERIAL_MONEY, UIManager.getInstance().getComponentStyle("MultiIcon"));
        Image airt_iconww = FontImage.createMaterial(FontImage.MATERIAL_PHONE_ANDROID, UIManager.getInstance().getComponentStyle("MultiIcon"));
        Image withd_iconww = FontImage.createMaterial(FontImage.MATERIAL_SEND, UIManager.getInstance().getComponentStyle("MultiIcon"));

        MultiButton testDepo = new MultiButton("Share Capital");
        testDepo.setIcon(depo_iconww);
        testDepo.setUIID("TransactionButton");
        testDepo.setUIIDLine1("MultiButtonText");
        testDepo.setTextLine2("M-Pesa to Share capital");
        testDepo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new DepositShares().sharesScreen();
            }
        });

        MultiButton depoContr = new MultiButton("Deposit Contribution");
        depoContr.setIcon(depo_iconww);
        depoContr.setUIID("TransactionButton");
        depoContr.setUIIDLine1("MultiButtonText");
        depoContr.setTextLine2("M-Pesa to Deposit Contribution");
        depoContr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new DepositContribution().contriScreen();
            }
        });

        MultiButton fundsWithdrawBtn = new MultiButton("Insurance");
        fundsWithdrawBtn.setIcon(airt_iconww);
        fundsWithdrawBtn.setUIID("TransactionButton");
        fundsWithdrawBtn.setUIIDLine1("MultiButtonText");
        fundsWithdrawBtn.setTextLine2("M-Pesa to Insurance");
        fundsWithdrawBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new DepositInsurance().insuranceScreen();
            }
        });

        MultiButton transferBtwnAccsBtn = new MultiButton("Repay Loans");
        transferBtwnAccsBtn.setIcon(withd_iconww);
        transferBtwnAccsBtn.setUIID("TransactionButton");
        transferBtwnAccsBtn.setUIIDLine1("MultiButtonText");
        transferBtwnAccsBtn.setTextLine2("M-Pesa to Loans");
        transferBtwnAccsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new DepostLoans().reloansScreen();
            }
        });
        
        Container transactionContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        transactionContainer.addAll(new Label(" ", "TabLabel"), testDepo, depoContr, fundsWithdrawBtn);
        transactionContainer.setScrollableY(true);
        transactionContainer.setScrollVisible(false);  
        
        return transactionContainer;
    }
    protected void showScreen(String title, Component content){
        Form depoForm = new Form(title, new BorderLayout());
        content.setUIID("ComponentContainer");
        Toolbar toolbar = depoForm.getToolbar();

        Form previous = getCurrentForm();
        toolbar.setBackCommand("", Toolbar.BackCommandPolicy.AS_ARROW,
            e -> previous.showBack());

        depoForm.add(BorderLayout.CENTER, content);
        depoForm.show();
    }
}
