/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.fineract.messagegateway.sms.providers.impl.africastkng;

import org.fineract.messagegateway.sms.providers.impl.infobip.*;
import java.util.Collections;
import java.util.HashMap;

import org.fineract.messagegateway.configuration.HostConfig;
import org.fineract.messagegateway.constants.MessageGatewayConstants;
import org.fineract.messagegateway.exception.MessageGatewayException;
import org.fineract.messagegateway.sms.domain.SMSBridge;
import org.fineract.messagegateway.sms.domain.SMSMessage;
import org.fineract.messagegateway.sms.providers.SMSProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africastalking.*;
import com.africastalking.sms.FetchMessageResponse;
import com.africastalking.sms.Recipient;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import org.fineract.messagegateway.sms.util.SmsMessageStatusType;

@Service(value = "Africastkng")
public class AfricasMessagePovider extends SMSProvider {

    public String msgId;
    public String status;
     public String no;
     private Integer statusCode;

    private static final Logger logger = LoggerFactory.getLogger(AfricasMessagePovider.class);

    //private HashMap<String, SendMultipleTextualSmsAdvanced> restClients = new HashMap<>() ; //tenantId, twilio clients
    //private final String callBackUrl ;
    private final StringBuilder builder;

    public static void log(String message) {
        System.out.println(message);
    }

    @Autowired
    public AfricasMessagePovider(final HostConfig hostConfig) {
        //callBackUrl = String.format("%s://%s:%d/infobip/report/", hostConfig.getProtocol(),  hostConfig.getHostName(), hostConfig.getPort());
        //logger.info("Registering call back to InfoBip:"+callBackUrl);
        builder = new StringBuilder();
    }

    @Override
    public void sendMessage(SMSBridge smsBridgeConfig, SMSMessage message) throws MessageGatewayException {
        //String statusCallback = callBackUrl+message.getId() ;
        //Based on message id, register call back. so that we get notification from Infobip about message status
        //SendMultipleTextualSmsAdvanced client = getRestClient(smsBridgeConfig) ;
        //Destination destination = new Destination();

        String userName = smsBridgeConfig.getConfigValue(MessageGatewayConstants.PROVIDER_ACCOUNT_ID);
        String password = smsBridgeConfig.getConfigValue(MessageGatewayConstants.PROVIDER_AUTH_TOKEN);
        AfricasTalking.initialize(userName, password);
        builder.setLength(0);
        
     String  mbl;
        mbl = message.getMobileNumber().substring(message.getMobileNumber().length() - 9);
        builder.append(smsBridgeConfig.getCountryCode());
        builder.append(mbl);
                
        String mobile = builder.toString();
        logger.info("Sending SMS to " + mobile + " ...");
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        try {
            List<Recipient> response = sms.send(message.getMessage(), smsBridgeConfig.getTenantKeyword(), new String[]{mobile}, false);
            for (Recipient recipient : response) {
                
                 msgId = recipient.messageId;
                status = recipient.status;
                no = recipient.status;
                
                System.out.println(status);
                 System.out.println(no);
               
                
              message.setExternalId(msgId);
              message.setDeliveryErrorMessage(status);

            if ("Success".equals(status)) {
                message.setDeliveryStatus(300);
            }else

            if ("Sent".equals(status)) {
                message.setDeliveryStatus(200);
            }else

            if ("Failed".equals(status)) {
                message.setDeliveryStatus(400);
            }else

            if ("Submitted".equals(status)) {
                message.setDeliveryStatus(100);
            }
            else {
                message.setDeliveryStatus(0);
            }   
            }
               
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //destination.setTo(mobile);
        //logger.debug("InfoBipMessageProvider.sendMessage():"+AfricasStatus.smsStatus(sentMessageInfo.getStatus().getGroupId()).getValue());
    }

}
