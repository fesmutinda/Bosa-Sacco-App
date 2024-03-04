/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.loans;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.processing.Result;
import static com.codename1.ui.CN.getCurrentForm;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.util.StringUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author Festus
 */
public class LoanGuaranteedForm {
    
    public void showLoanGuaranteedScreen() throws IOException{
        showScreen("Loans Guaranteed", createLoansGuarantors());
    }

    private Container createLoansGuarantors()throws UnsupportedEncodingException, IOException{
        
        Container loanGuaranteedContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        String str_content = (String) Storage.getInstance().readObject("Guaranteed.json");
        byte[] bytes_content = str_content.getBytes();

        JSONParser json_content = new JSONParser();

        try(Reader is_content = new InputStreamReader(new ByteArrayInputStream(bytes_content), "UTF-8"))
        {
            Map<String, Object> data = json_content.parseJSON(is_content); 
            ArrayList transactions = (ArrayList)data.get("guaranteed");
            System.out.println("guaranteed : " + transactions);

            for(int i=0; i<transactions.size(); i++){
                Map<String, Object> transaction = (Map<String, Object>)transactions.get(i);

                String loanNo = (String)transaction.get("loanNo");
                String mno = (String)transaction.get("mno");
                String name = (String)transaction.get("name");
                String amount  = (String)transaction.get("amount");
                
                loanGuaranteedContainer.add(createGuarantor(loanNo, name, amount));

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