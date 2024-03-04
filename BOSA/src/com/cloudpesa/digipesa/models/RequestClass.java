/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudpesa.digipesa.models;

import com.cloudpesa.digipesa.forms.MemberInfoForm;
import com.cloudpesa.digipesa.forms.BalancesForm;
import com.codename1.components.InfiniteProgress;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.processing.Result;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Festus
 */
public class RequestClass 
{
    
    public String digitCode = "";
    public String response_String = "";
    private String PhoneNumber = Storage.getInstance().readObject("PhoneNumber").toString();
    private String SaccoID = Storage.getInstance().readObject("SaccoID").toString();
    private String Key = Storage.getInstance().readObject("Key").toString();
    private String myUrl;
    private String str_response, str_train, str_carriage, str_data;
    
    public static void okayDialog(String title,String messageToDisplay)
    {
        final Dialog dialog = new Dialog(title);
        dialog.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Button button = new Button("Okay");
        button.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) 
            {
                dialog.dispose();
            }
        });
        Container okayDialog = new Container();
        Container messageDialog = new Container();
        TextArea message = new TextArea(messageToDisplay);
        message.setEditable(false);
        okayDialog.setLayout(new FlowLayout(Component.CENTER));
        messageDialog.setLayout(new FlowLayout(Component.CENTER));
        messageDialog.addComponent(message);
        okayDialog.addComponent(button );
        messageDialog.addComponent(okayDialog);
        dialog.addComponent(messageDialog);
        dialog.show();
    }
    public void toProfile(){
        try {
            memberProfile();
        } catch (IOException ex) {
        } catch (JSONException ex) {
        }
    }
    public void memberProfile() throws IOException, JSONException{
        
        int w = Display.getInstance().getDisplayWidth()-1;
        myUrl = "https://suresms.co.ke:45322/capi/RegisteredMemberDetails";
        
        Hashtable hash =new Hashtable();
        hash.put("phone", PhoneNumber);
        hash.put("saccoID", SaccoID);        
        hash.put("Key", Key);        

        final Result res=Result.fromContent(hash);
        final String checkthis=res.toString();
        
        ConnectionRequest request=new ConnectionRequest()
        {
            public void buildRequestBody(OutputStream thisstream) throws IOException
            {
                Writer andika=null;
                andika=new OutputStreamWriter(thisstream,"UTF-8");
                andika.write(checkthis);
                andika.flush();
                andika.close();
            }
        };
        request.setUrl(myUrl);
        request.setContentType("application/json");                    
        request.addRequestHeader("Accept","application/json");
        request.setPost(true);
        request.setWriteRequest(true);
        ///----------loading......
        InfiniteProgress inftprogress = new InfiniteProgress();
        Dialog dlg_progress = new Dialog();
        dlg_progress.setDialogUIID("dlg_progress");
        dlg_progress.setLayout(new BorderLayout());
        dlg_progress.setWidth(w);
        
        Label lbl_progress = new Label("  Loading Details");
        lbl_progress.getStyle().setFgColor(0x000000, false);
        lbl_progress.getStyle().setBgTransparency(0);
        dlg_progress.addComponent(BorderLayout.CENTER, FlowLayout.encloseCenterBottom(inftprogress, lbl_progress));
        dlg_progress.setTransitionInAnimator(CommonTransitions.createEmpty());
        dlg_progress.setTransitionOutAnimator(CommonTransitions.createEmpty());
        dlg_progress.showPacked(BorderLayout.CENTER, false);
        request.setDisposeOnCompletion(dlg_progress);

        NetworkManager.getInstance().addToQueueAndWait(request);
        request.setDisposeOnCompletion(dlg_progress);
        
        byte[] dataa = request.getResponseData();
        if (dataa != null) 
        {
            Reader reader = new InputStreamReader(new ByteArrayInputStream(dataa), "UTF-8");
            int chr;
            StringBuffer sb = new StringBuffer();
            String str_server_response = "";
            while ((chr = reader.read()) != -1)
            {
                sb.append((char) chr);
            }
            str_server_response = sb.toString(); 
            
            if(request.getResponseCode() == 200 || request.getResponseCode() == 201)
            {
                str_response = str_server_response;

                JSONParser json_parser = new JSONParser();
                Map map_response = json_parser.parseJSON(new InputStreamReader(new ByteArrayInputStream(dataa), "UTF-8"));
                
                String memberDetails = map_response.get("memberDetails").toString();
                
                System.out.println("My Accounts>>>>>>>>>>>>>>>>>>>>>: " + memberDetails);
                if(memberDetails.equals("")){
                    Dialog.show("Sorry!!!",  "No details could be loaded", "Close", "Retry");
                }
                else if(memberDetails.equals("[]")){
                    Dialog.show("Sorry!!!",  "No details could be loaded", "Close", "Retry");
                }
                else if (memberDetails.equals(null)) 
                {
                    Dialog.show("Sorry!!!",  "No details could be loaded", "Close", "Retry");
                }
                else{
                
                    memberDetails = StringUtil.replaceAll(memberDetails, "---", "**");


                    String payload1 = StringUtil.tokenize(memberDetails, "**").get(0);
                    String memberno = StringUtil.tokenize(payload1, ":::").get(0); 
                    String name = StringUtil.tokenize(payload1, ":::").get(1); 
                    String idNo = StringUtil.tokenize(payload1, ":::").get(2);
                    String staffno = StringUtil.tokenize(payload1, ":::").get(3); 
                    String PhoneNumber = StringUtil.tokenize(payload1, ":::").get(4);

                    String myAccs = StringUtil.tokenize(memberDetails, "**").get(1);
                    String accNo = StringUtil.tokenize(myAccs, ":::").get(0); 
                    String accName = StringUtil.tokenize(myAccs, ":::").get(1);

                            System.out.println("My Accounts>>>>>>>>>>>>>>>>>>>>>: " + myAccs);
                            System.out.println("Account Number>>>>>>>>>>>>>>>>>>>>>: " + accNo);
                            System.out.println("Account Name>>>>>>>>>>>>>>>>>>>>>: " + accName);

                    Storage.getInstance().writeObject("MemberNumber", memberno);
                    Storage.getInstance().writeObject("Name", name);
                    Storage.getInstance().writeObject("IdNumber", idNo);
                    Storage.getInstance().writeObject("StaffNumber", staffno);
                    Storage.getInstance().writeObject("PhoneNumber", PhoneNumber);

                    Storage.getInstance().writeObject("AccountNumber", accNo);
                    Storage.getInstance().writeObject("AccountName", accName);

                    new MemberInfoForm().showMemberScreen();
                }
            }
            else
            {
                handleError();
            }
        }
        
    }
    public void accountBalances() throws IOException, JSONException
    {
        myUrl = "https://suresms.co.ke:45322/capi/Balance";
        Hashtable hash = new Hashtable();
        hash.put("phone", PhoneNumber);
        hash.put("saccoID", SaccoID);
        hash.put("Key", Key);     

        final Result res=Result.fromContent(hash);
        final String checkthis=res.toString();
        
        ConnectionRequest request=new ConnectionRequest()
        {
            public void buildRequestBody(OutputStream thisstream) throws IOException
            {
                Writer andika=null;
                andika=new OutputStreamWriter(thisstream,"UTF-8");
                andika.write(checkthis);
                andika.flush();
                andika.close();
            }
        };
        request.setUrl(myUrl);
        request.setContentType("application/json");                    
        request.addRequestHeader("Accept","application/json");
        request.setPost(true);
        request.setWriteRequest(true);
        ///----------loading......
        InfiniteProgress inftprogress = new InfiniteProgress();
        Dialog dlg_progress = new Dialog();
        dlg_progress.setDialogUIID("dlg_progress");
        dlg_progress.setLayout(new BorderLayout());
        
        Label lbl_progress = new Label("  Getting Balances");
        lbl_progress.getStyle().setFgColor(0x000000, false);
        lbl_progress.getStyle().setBgTransparency(0);
        dlg_progress.addComponent(BorderLayout.CENTER, FlowLayout.encloseCenterBottom(inftprogress, lbl_progress));
        dlg_progress.setTransitionInAnimator(CommonTransitions.createEmpty());
        dlg_progress.setTransitionOutAnimator(CommonTransitions.createEmpty());
        dlg_progress.showPacked(BorderLayout.CENTER, false);
        request.setDisposeOnCompletion(dlg_progress);

        NetworkManager.getInstance().addToQueueAndWait(request);
        request.setDisposeOnCompletion(dlg_progress);
        
        Log.p("Reading response from server.", 1);
        
        byte[] dataa = request.getResponseData();
        if (dataa != null) 
        {
            Reader reader = new InputStreamReader(new ByteArrayInputStream(dataa), "UTF-8");
            int chr;
            StringBuffer sb = new StringBuffer();
            String str_server_response = "";
            while ((chr = reader.read()) != -1)
            {
                sb.append((char) chr);
            }
            str_server_response = sb.toString(); 

            Log.p("Processing balance's json response for from server.", 1);

            if(request.getResponseCode() == 200 || request.getResponseCode() == 201)
            {
                JSONParser json_parser = new JSONParser();
                Map map_response = json_parser.parseJSON(new InputStreamReader(new ByteArrayInputStream(dataa), "UTF-8"));

                String ResponseBody  = map_response.get("ResponseBody").toString();
                System.out.println("My Balances>>>>>>>>>>>>>>>>>>>>>: " + ResponseBody);
                
                String full_response = map_response.get("ResponseBody").toString();
                
                full_response = StringUtil.replaceAll(full_response, ":::", "**");
                
                full_response = StringUtil.replaceAll(full_response, "[", "");
                full_response = StringUtil.replaceAll(full_response, "]", "");
                
                System.out.println("New Balances Balances>>>>>>>>>>>>>>>>>>>>>: " + full_response);
                List<String> items = StringUtil.tokenize(full_response, ",");
                
                String response_0 = items.get(0);
                System.out.println("ResponseCode Name: " + response_0);
                
                if (response_0 == null) 
                {
                    Dialog.show("FAILED", " An error occured while processing request, kindly retry later.", "Close", "Retry");
                }
                else if(full_response.equals("[]")){
                    Dialog.show("Sorry!!!",  "No details could be loaded", "Close", "Retry");
                }
                else if (full_response.equals(null)) 
                {
                    Dialog.show("FAILED", " An error occured while processing request, kindly retry later.", "Close", "Retry");
                }
                else if (response_0.equalsIgnoreCase("INSUFFICIENT")) 
                {
                    Dialog.show("FAILED", "You have insufficient funds to request for this service.", "Close", "Retry");
                }
                else if (response_0.equalsIgnoreCase("INVALID TRAN")) 
                {
                    Dialog.show("FAILED", "Could not load balances.", "Close", "Retry");
                }
                else
                {
                    StringBuilder sb_options = new StringBuilder(); 
                    String [] str_acoptions = new String[items.size()];
            
                    for (int i = 0; i < items.size(); i++) 
                    {
                        String item = items.get(i);

                        String accountName = StringUtil.tokenize(item, "**").get(0);
                        String balance = StringUtil.tokenize(item, "**").get(1);
                        System.out.println("Account Name: " + accountName + ", Balance: " + balance);
                        
                        if(i == (items.size()-1))
                        {
                            str_acoptions[i] = "{\"AccountName\" : \""+accountName +"\", \"AccountBalance\" : \""+balance+"\"}";
                        }
                        else
                        {
                            str_acoptions[i] = "{\"AccountName\" : \""+accountName +"\", \"AccountBalance\" : \""+balance+"\"}, ";
                        }
                        
                        sb_options.append(str_acoptions[i]);
                    }
                    System.out.println("Full Details " + sb_options.toString());
                    Storage.getInstance().writeObject("balances.json", sb_options.toString());
                    
                    new BalancesForm().balancesScreen();
                }
            }
            else
            {
                handleError();
            }
        }
    }
    public String postRequest(String checkthis, String myUrl)throws IOException, JSONException
    {
        ConnectionRequest request=new ConnectionRequest()
        {
            public void buildRequestBody(OutputStream thisstream) throws IOException
            {
                Writer andika=null;
                andika=new OutputStreamWriter(thisstream,"UTF-8");
                andika.write(checkthis);
                andika.flush();
                andika.close();
            }
        };
        request.setUrl(myUrl);
        request.setContentType("application/json");                    
        request.addRequestHeader("Accept","application/json");
        request.setPost(true);
        request.setWriteRequest(true);
        
        ///----------loading......
        InfiniteProgress inftprogress = new InfiniteProgress();
        Dialog dlg_progress = new Dialog();
        dlg_progress.setDialogUIID("Container");
        dlg_progress.setLayout(new BorderLayout());

        Label lbl_progress = new Label("Connecting...\n, Please Wait...");
        lbl_progress.getStyle().setFgColor(0xffffff, false);
        lbl_progress.getStyle().setBgTransparency(0);
        dlg_progress.addComponent(BorderLayout.CENTER, FlowLayout.encloseCenterBottom(lbl_progress, inftprogress));
        dlg_progress.setTransitionInAnimator(CommonTransitions.createEmpty());
        dlg_progress.setTransitionOutAnimator(CommonTransitions.createEmpty());
        dlg_progress.showPacked(BorderLayout.CENTER, false);
        request.setDisposeOnCompletion(dlg_progress);

        NetworkManager.getInstance().addToQueueAndWait(request);
        request.setDisposeOnCompletion(dlg_progress);
        
//        start response;
//       
 //Informational 1xx
        /*
        100 => '100 Continue',
        101 => '101 Switching Protocols',
        
        //Successful 2xx
        200 => '200 OK',
        201 => '201 Created',
        202 => '202 Accepted',
        203 => '203 Non-Authoritative Information',
        204 => '204 No Content',
        205 => '205 Reset Content',
        206 => '206 Partial Content',
        
        //Redirection 3xx
        300 => '300 Multiple Choices',
        301 => '301 Moved Permanently',
        302 => '302 Found',
        303 => '303 See Other',
        304 => '304 Not Modified',
        305 => '305 Use Proxy',
        306 => '306 (Unused)',
        307 => '307 Temporary Redirect',
        
        //Client Error 4xx
        400 => '400 Bad Request',
        401 => '401 Unauthorized',
        402 => '402 Payment Required',
        403 => '403 Forbidden',
        404 => '404 Not Found',
        405 => '405 Method Not Allowed',
        406 => '406 Not Acceptable',
        407 => '407 Proxy Authentication Required',
        408 => '408 Request Timeout',
        409 => '409 Conflict',
        410 => '410 Gone',
        411 => '411 Length Required',
        412 => '412 Precondition Failed',
        413 => '413 Request Entity Too Large',
        414 => '414 Request-URI Too Long',
        415 => '415 Unsupported Media Type',
        416 => '416 Requested Range Not Satisfiable',
        417 => '417 Expectation Failed',
        418 => '418 I\'m a teapot',
        422 => '422 Unprocessable Entity',
        423 => '423 Locked',
        
        //Server Error 5xx
        500 => '500 Internal Server Error',
        501 => '501 Not Implemented',
        502 => '502 Bad Gateway',
        503 => '503 Service Unavailable',
        504 => '504 Gateway Timeout',
        505 => '505 HTTP Version Not Supported'*/
        
    byte[] dataa2=request.getResponseData();
    if(dataa2!=null)
    {
       Reader reader2 = null;
        try 
        {
            reader2 = new InputStreamReader(new ByteArrayInputStream(dataa2), "UTF-8");
        } catch (UnsupportedEncodingException ex) 
        {
            //Logger.getLogger(SampleLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        int chr2;
        StringBuffer sb2=new StringBuffer();
        String response2="";
        try 
        {
            while((chr2=reader2.read()) != -1)
            {
                sb2.append((char) chr2);
            }
        } 
        catch (IOException ex) 
        {
            //Logger.getLogger(SampleLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        response2=sb2.toString();

        JSONParser json_parser2=new JSONParser();
        Map map_response2 = null;
        try 
        {
            map_response2 = json_parser2.parseJSON(new InputStreamReader(new ByteArrayInputStream(dataa2),"UTF-8"));
        } catch (UnsupportedEncodingException ex) 
        {
            //Logger.getLogger(SampleLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) 
        {
            //Logger.getLogger(SampleLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        response_String = map_response2.get("ResponseBody").toString();
    }
    else
    {
        //noth
    }

    return response_String;
    }
    public void loadLoans() throws UnsupportedEncodingException, IOException{
        PhoneNumber = Storage.getInstance().readObject("PhoneNumber").toString();
        SaccoID = Storage.getInstance().readObject("SaccoID").toString();
        
        int w = Display.getInstance().getDisplayWidth()-1;
        myUrl = "https://suresms.co.ke:45322/capi/CurrentLoans";
        
        Hashtable hash =new Hashtable();
        hash.put("phone", PhoneNumber);
        hash.put("SaccoID", SaccoID);
        hash.put("Key", Key);           

        final Result res=Result.fromContent(hash);
        final String checkthis=res.toString();
        
        ConnectionRequest request=new ConnectionRequest()
        {
            public void buildRequestBody(OutputStream thisstream) throws IOException
            {
                Writer andika=null;
                andika=new OutputStreamWriter(thisstream,"UTF-8");
                andika.write(checkthis);
                andika.flush();
                andika.close();
            }
        };
        request.setUrl(myUrl);
        request.setContentType("application/json");                    
        request.addRequestHeader("Accept","application/json");
        request.setPost(true);
        request.setWriteRequest(true);
        ///----------loading......
        InfiniteProgress inftprogress = new InfiniteProgress();
        Dialog dlg_progress = new Dialog();
        dlg_progress.setDialogUIID("dlg_progress");
        dlg_progress.setLayout(new BorderLayout());
        dlg_progress.setWidth(w);
        
        Label lbl_progress = new Label("  Loading Details");
        lbl_progress.getStyle().setFgColor(0x000000, false);
        lbl_progress.getStyle().setBgTransparency(0);
        dlg_progress.addComponent(BorderLayout.CENTER, FlowLayout.encloseCenterBottom(inftprogress, lbl_progress));
        dlg_progress.setTransitionInAnimator(CommonTransitions.createEmpty());
        dlg_progress.setTransitionOutAnimator(CommonTransitions.createEmpty());
        dlg_progress.showPacked(BorderLayout.CENTER, false);
        request.setDisposeOnCompletion(dlg_progress);

        NetworkManager.getInstance().addToQueueAndWait(request);
        request.setDisposeOnCompletion(dlg_progress);
        
        byte[] dataa = request.getResponseData();
        if (dataa != null) 
        {
            Reader reader = new InputStreamReader(new ByteArrayInputStream(dataa), "UTF-8");
            int chr;
            StringBuffer sb = new StringBuffer();
            String str_server_response = "";
            while ((chr = reader.read()) != -1)
            {
                sb.append((char) chr);
            }
            str_server_response = sb.toString(); 
            
            if(request.getResponseCode() == 200 || request.getResponseCode() == 201)
            {

                JSONParser json_parser = new JSONParser();
                Map map_response = json_parser.parseJSON(new InputStreamReader(new ByteArrayInputStream(dataa), "UTF-8"));
                
                String allloans_response = map_response.get("ResponseBody").toString();
                System.out.println("Response: " + allloans_response);
                
                if(allloans_response.equals("[]")||allloans_response.equals("")){
                    Dialog.show("Sorry!!!",  "No Loans Record were found", "Close", "Retry");
                }
                else
                {
                    allloans_response = StringUtil.replaceAll(allloans_response, ", ", "**");

                    System.out.println("My loans: " + allloans_response);

                    List<String> loan_items = StringUtil.tokenize(allloans_response, "**");

                    StringBuilder sb_options = new StringBuilder(); 
                        String [] str_acoptions = new String[loan_items.size()];

                    for (int i = 0; i < loan_items.size(); i++) 
                    {
                        String item = loan_items.get(i);
                        item = StringUtil.replaceAll(item, "[", "");

                        String loanID = StringUtil.tokenize(item, ":::").get(0);
                        String loanName = StringUtil.tokenize(item, ":::").get(1);
                        String balance = StringUtil.tokenize(item, ":::").get(2);

                        String loantoPay = loanID+": "+loanName;

                        if(i == (loan_items.size()-1))
                            {
                                str_acoptions[i] = "\""+loantoPay +"\"";
                            }
                            else
                            {
                                str_acoptions[i] = "\""+loantoPay +"\", ";
                            }

                            sb_options.append(str_acoptions[i]);

                    }
                    Storage.getInstance().writeObject("loansToPay", sb_options.toString());
                    System.out.println("loansToPay : "+sb_options);
                }
            }
            else
            {
                Dialog.show("Sorry!!!",  "Failed to load Loans", "Close", "Retry");
            }
        }
    }
    public void handleError()// throws UnsupportedEncodingException, IOException//, JSONException 
    {
        System.err.println("========= handling Error ========");
        
        String rescode = (String) Storage.getInstance().readObject("respcode");
        String resmesg = (String) Storage.getInstance().readObject("resptext");
        

        if(Double.parseDouble(rescode) == 403.0) //if (Double.parseDouble(rescode) == 3002.0) 
        {
            okayDialog("Login Failed!\n: ", "Your username/password combination may be wrong.");
            //LoginMenu loginer = new LoginMenu();
            //loginer.showMainScreen();
        } 
        else if(Double.parseDouble(rescode) == 401.0) //if (Double.parseDouble(rescode) == 3002.0) 
        {
            //okayDialog("Login Failed!\n: ", "Your username is in the wrong format.... it should be an email."); //TODO: force email as username during registration
            okayDialog("Login Failed!\n: ", "Your username/password combination may be wrong.");
            //LoginMenu loginer = new LoginMenu();
            //loginer.showMainScreen();
        }
        else if(Double.parseDouble(rescode) == 3004.0) 
        {
            okayDialog("Sorry, Session Expired!\n: ", "The Process could not complete, please login and try again later.");
            //doBackgroundLogin();
////////                Log.p("Deleting content of stoage.", 1);
////////                storage.clearStorage();
////////
////////                Log.p("Writing logout status in storage.", 1);
////////                storage.writeObject("loginstatus", "NOL");
////////
////////                Log.p("Writing current screen in storage.", 1);
////////                storage.writeObject("screen", "home");

                Log.p("Going to the Main Menu.", 1);
                //MainMenu menuer = new MainMenu();
               //menuer.showMainScreen();
        } 
        else if (Double.parseDouble(rescode) == 3003.0) 
        {
            okayDialog("Sorry!\n: ", "Your Account is inactive, contact support for help.");
        } 
        else if (Double.parseDouble(rescode) == 4002.0) 
        {
            okayDialog("Error!\n: ", "The email you entered is not valid, check it and try again.");
        } 
        else if (Double.parseDouble(rescode) == 4003.0) 
        {
            okayDialog("Sorry!\n: ", "The email address you entered is already in use, please choose another one.");
        } 
        else if (Double.parseDouble(rescode) == 6001.0) 
        {
            okayDialog("Sorry!\n: ", "Your cart is empty, add a beneficiary to make the order.");
        } 
        else if (Double.parseDouble(rescode) == 7004.0) 
        {
            okayDialog("Error!\n: ", "The policy's start date cannot be in the past, select a valid date to continue.");
        } else if (Double.parseDouble(rescode) == 7005.0) {
            okayDialog("Error!\n: ", "The beneficiary already exists.");
        } else if (Double.parseDouble(rescode) == 1011.0) {
            okayDialog("Error!\n: ", "There are Input values that are missing.");
        }
        else if (Double.parseDouble(rescode) == 1012.0) {
            okayDialog("Note: \n: ", "Select the Policy Start Date to Proceed.");
        }
        else if (Double.parseDouble(rescode) == 2002.0) 
        {
            okayDialog("Card Invalid!", "The card you have entered has already been used or has expired.");
//            storage.writeObject("screen", "card");
            //viewCart();       
            
        }
        else if (Double.parseDouble(rescode) == 2001.0) 
        {
            okayDialog("Card Invalid!", "The card is Invalid. Check the PIN and try again");
        }
        else if (Double.parseDouble(rescode) == 2005.0) 
        {
            okayDialog("Payment Error!", "The scratch card can only be used to Pay for one individual at a time. Your Cart has more than one Item. Please delete the extra Items from the cart to continue.");
        }
        else if (Double.parseDouble(rescode) == 200.0) 
        {
            //Impossible... not handled here
        }
        else if (Double.parseDouble(rescode) == 409.0) 
        {
//            str_screen = Storage.getInstance().readObject("screen").toString();
//            if(str_screen.equals("registration"))
//            {
//                okayDialog("Sorry"+"!", "Email or phone number already used. Check that you have entered the correct ones and try again otherwise please proceed to login if you are already registered.");
//            }
//            else if(str_screen.equals("verification"))
//            {
//                okayDialog("Sorry"+"!", "Email or phone number already used. Check that you have entered the correct ones and try again otherwise please proceed to login if you are already registered.");
//            }
//            else
//            {
//                okayDialog("Sorry"+"!", "Already used or exists!\nPlease check that you have entered the data correctly and try again.");
//            }
        }
        
        
        /*
        //Informational 1xx
        100 => '100 Continue',
        101 => '101 Switching Protocols',
        
        //Successful 2xx
        200 => '200 OK',
        201 => '201 Created',
        202 => '202 Accepted',
        203 => '203 Non-Authoritative Information',
        204 => '204 No Content',
        205 => '205 Reset Content',
        206 => '206 Partial Content',
        
        //Redirection 3xx
        300 => '300 Multiple Choices',
        301 => '301 Moved Permanently',
        302 => '302 Found',
        303 => '303 See Other',
        304 => '304 Not Modified',
        305 => '305 Use Proxy',
        306 => '306 (Unused)',
        307 => '307 Temporary Redirect',
        
        //Client Error 4xx
        400 => '400 Bad Request',
        401 => '401 Unauthorized',
        402 => '402 Payment Required',
        403 => '403 Forbidden',
        404 => '404 Not Found',
        405 => '405 Method Not Allowed',
        406 => '406 Not Acceptable',
        407 => '407 Proxy Authentication Required',
        408 => '408 Request Timeout',
        409 => '409 Conflict',
        410 => '410 Gone',
        411 => '411 Length Required',
        412 => '412 Precondition Failed',
        413 => '413 Request Entity Too Large',
        414 => '414 Request-URI Too Long',
        415 => '415 Unsupported Media Type',
        416 => '416 Requested Range Not Satisfiable',
        417 => '417 Expectation Failed',
        418 => '418 I\'m a teapot',
        422 => '422 Unprocessable Entity',
        423 => '423 Locked',
        
        //Server Error 5xx
        500 => '500 Internal Server Error',
        501 => '501 Not Implemented',
        502 => '502 Bad Gateway',
        503 => '503 Service Unavailable',
        504 => '504 Gateway Timeout',
        505 => '505 HTTP Version Not Supported'*/
        
        else 
        {
            okayDialog("Sorry!\n: ", "Something went wrong,\n Error: "+rescode+" - "+ resmesg);
            
    
        }
    }
    public class JSONException extends Exception
    {
     public JSONException(String message)
     {    
     } 
      public JSONException(Throwable t)
      {    
      } 
//      public Throwable getClause()
//      {
//      }
    }
}
