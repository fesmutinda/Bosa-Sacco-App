/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.loans;

import com.codename1.io.JSONParser;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.getCurrentForm;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Festus
 */
public class LoanBalancesForm {
    
    public void showLoanBalancesScreen() {
        showScreen("My Loans", createContentPane());
    }
    
    
    public Container createContentPane() {
        Container demoContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS), "DemoContainer");
        demoContainer.setScrollableY(true);
        
        String str_content = (String) Storage.getInstance().readObject("Loans.json");
        byte[] bytes_content = str_content.getBytes();

        JSONParser json_content = new JSONParser();

        try(Reader is_content = new InputStreamReader(new ByteArrayInputStream(bytes_content), "UTF-8"))
        {
            Map<String, Object> data = json_content.parseJSON(is_content); 
            //Get the transactions
            ArrayList transactions = (ArrayList)data.get("loans");
            System.out.println("guaranteed : " + transactions);

            //Iterate through the transactions
            for(int i=0; i<transactions.size(); i++){
                Map<String, Object> transaction = (Map<String, Object>)transactions.get(i);

                //Get the transaction details
                String loanNo = (String)transaction.get("loanNo");
                String loanType = (String)transaction.get("loanType");
                String balance = (String)transaction.get("balance");
                String dateTaken = (String)transaction.get("dateTaken");
                String installmentAmount = (String)transaction.get("installmentAmount");
                String dateDue = (String)transaction.get("dateDue");
                
                demoContainer.add(createLoanBalances(loanNo, loanType, balance, dateDue, installmentAmount));
            }
        } catch (UnsupportedEncodingException ex) {
        } catch (IOException ex) {
        }
        
        demoContainer.setScrollableY(true);
        demoContainer.setScrollVisible(false);
        return demoContainer;
    }
    
    private Container createLoanBalances(String loanId, String loanType, String loanBalance, String principle, String interest){
        Container cnt1 = BoxLayout.encloseY(new Label(""));
        cnt1.setUIID("LabelContainer");
        
        Button repayButton = new Button("Repay Now");

        cnt1.addComponent(BorderLayout.east(new Label(loanId, "detailHead")).
                add(BorderLayout.WEST, new Label("Loan ID", "detailHead")));
        cnt1.addComponent(BorderLayout.east(new Label(loanType, "detailHead")).
                add(BorderLayout.WEST, new Label("Loan Type", "detailHead")));
        cnt1.addComponent(BorderLayout.east(new Label("Ksh. "+principle, "detailHead")).
                add(BorderLayout.WEST, new Label("Principle", "detailHead")));
        cnt1.addComponent(BorderLayout.east(new Label("Ksh. "+interest, "detailHead")).
                add(BorderLayout.WEST, new Label("Interest", "detailHead")));
        cnt1.addComponent(BorderLayout.east(new Label("Ksh. "+loanBalance, "detailHead")).
                add(BorderLayout.WEST, new Label("Loan Balance", "detailHead")));
        cnt1.addComponent(BorderLayout.east(repayButton).
                add(BorderLayout.WEST, new Label("", "detailHead")));
        
        repayButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showScreen("Repay Loan", repaymentC(loanId));
            }
        });

        return BoxLayout.encloseY(cnt1);
    }
    
    public Container repaymentC(String loanId)
    {
        Container repaymentContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        
        TextField amountText = new TextField("", "", 20, TextField.NUMERIC);
        Button btnAdvance = new Button("Repay Loan", "cBtns");
        
        repaymentContainer.addComponent(BorderLayout.east(new Label(loanId, "detailHead")).
                                add(BorderLayout.WEST, new Label("Loan ID", "detailHead")));
        repaymentContainer.addComponent(new Label("Enter Amount", "FlagButton"));
        repaymentContainer.addComponent(amountText);
        repaymentContainer.addComponent(btnAdvance);
        repaymentContainer.setUIID("LabelContainer");
        
        
        btnAdvance.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent evt) 
            {
                if (amountText.getText().toString() == null
                        || amountText.getText().toString().equals("")) 
                {
                    Dialog.show("", "Please Enter a valid Amount", "Close", "");
                }
                else if (loanId.toString() == null
                        || loanId.toString().equals("")) 
                {
                    Dialog.show("", "You have not selected loan", "Close", "");
                }
                else 
                {
                    Command ok = new Command("Confirm");
                    Command cancel = new Command("Cancel");

                    if(Dialog.show("Confirm", "Please Confirm Payment of Loan "+loanId, ok, cancel)  == ok)
                    {
                        //repaid..
                    }
                }
            }
        });
                
        return BoxLayout.encloseY(repaymentContainer);
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
    private Label createSeparator() {
      Label sep = new Label(" ");
      sep.setUIID("Separator");
      sep.setShowEvenIfBlank(true);
      return sep;
    }
}
