/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.forms;

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
public class BalancesForm {
    private String sharecapBalStr = "0";
    private String depoContrBal = "0";
    private String AccBalanceLable = "0";
    
    private String AccountName, AccountBalance, SalaryAccBalance;
    
    public void balancesScreen(){
        try {
            showScreen("Balances", createAccountsTab());
        } catch (IOException ex) {
        }
    }

    private Container createAccountsTab() throws UnsupportedEncodingException, IOException{
        Container memberDetailsContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        String str_content =(String) Storage.getInstance().readObject("MemberProfile.json");
        Log.p("Reading >>>>>>>>>>>>>>\n"+str_content+"\n<<<<<<<<<<< .json from storage.", 1);

        byte[] bytes_content = str_content.getBytes();
        
        JSONParser parser = new JSONParser();

        //Parse the JSON
        try(Reader is_content = new InputStreamReader(new ByteArrayInputStream(bytes_content), "UTF-8"))
        {
            Map<String, Object> data = parser.parseJSON(is_content); 
            
            ArrayList accounts = (ArrayList)data.get("accounts");

            for(int i=0; i<accounts.size(); i++){
                Map<String, Object> account = (Map<String, Object>)accounts.get(i);

                String accountName = (String)account.get("accountName");
                String balance = (String)account.get("balance");
                
                memberDetailsContainer.add(createShow(accountName, balance));

            }
        }
        
        memberDetailsContainer.setScrollableY(true);
        memberDetailsContainer.setScrollVisible(false);
        
        return memberDetailsContainer;
    }
    
    
    private Container createShow(String AccountName, String AccountBalance){
        Container showContainer = BoxLayout.encloseY(new Label(""));
        showContainer.setUIID("LabelContainer");
        
        showContainer.addComponent(BorderLayout.east(new Label(AccountName, "detailHead")).
                        add(BorderLayout.WEST, new Label("Account Name", "detailHead")));
        showContainer.addComponent(BorderLayout.east(new Label("Ksh "+ AccountBalance, "detailHead")).
                        add(BorderLayout.WEST, new Label("Balance", "detailHead")));
        
        return BoxLayout.encloseY(showContainer);
    }
    
    protected void showScreen(String title, Component content)
    {
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