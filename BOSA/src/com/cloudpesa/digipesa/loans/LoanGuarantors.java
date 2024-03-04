/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.loans;

import com.codename1.components.MultiButton;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.Storage;
import static com.codename1.ui.CN.getCurrentForm;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
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
public class LoanGuarantors 
{
    public void showLoanGuarantorsScreen()throws IOException{
        showScreen("My Guarantors", createLoansGuarantors());
    }
    
    private Container createLoansGuarantors()throws UnsupportedEncodingException, IOException{
        
        Container loanGuaranteedContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        String str_content = (String) Storage.getInstance().readObject("Guarantors.json");
        byte[] bytes_content = str_content.getBytes();

        JSONParser json_content = new JSONParser();

        try(Reader is_content = new InputStreamReader(new ByteArrayInputStream(bytes_content), "UTF-8"))
        {
            Map<String, Object> data = json_content.parseJSON(is_content); 
            //Get the transactions
            ArrayList transactions = (ArrayList)data.get("LoansGuaranteed");
            System.out.println("guaranteed : " + transactions);

            //Iterate through the transactions
            for(int i=0; i<transactions.size(); i++){
                Map<String, Object> transaction = (Map<String, Object>)transactions.get(i);

                //Get the transaction details
    
                String loanNo = (String)transaction.get("loanNo");
                String memberNo = (String)transaction.get("memberNo");
                String name = (String) transaction.get("name");
                String amountGuaranteed = (String) transaction.get("amountGuaranteed");
                loanGuaranteedContainer.add(createGuarantor(loanNo, name, amountGuaranteed));

            }
        }
        loanGuaranteedContainer.setScrollableY(true);
        loanGuaranteedContainer.setScrollVisible(false);
        return loanGuaranteedContainer;
    }
    
    private Container createGuarantor(String loanId, String loanType, String loanAmount){
        Container loanGuaranteedContainer = BoxLayout.encloseY(new Label(""));
        loanGuaranteedContainer.setUIID("LabelContainer");
        
        loanGuaranteedContainer.addComponent(BorderLayout.east(new Label(loanId, "detailHead")).
                add(BorderLayout.WEST, new Label("Loan ID", "detailHead")));
        loanGuaranteedContainer.addComponent(BorderLayout.east(new Label(loanType, "detailHead")).
                add(BorderLayout.WEST, new Label("Name", "detailHead")));
        loanGuaranteedContainer.addComponent(BorderLayout.east(new Label("Ksh. "+loanAmount, "detailHead")).
                add(BorderLayout.WEST, new Label("Loan Amount", "detailHead")));
        
        return BoxLayout.encloseY(loanGuaranteedContainer);
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
      // the separator line is implemented in the theme using padding and background color, by default labels
      // are hidden when they have no content, this method disables that behavior
      sep.setShowEvenIfBlank(true);
      return sep;
    }
}
