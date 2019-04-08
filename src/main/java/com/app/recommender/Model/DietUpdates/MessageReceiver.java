package com.app.recommender.Model.DietUpdates;

import com.app.recommender.diet.IDietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    @Autowired
    IDietService service;

    @JmsListener(destination = "diet-updates")
    public void receiveMessage(DietUpdateMessage dietUpdateMessage) {
        service.updateDietValues(dietUpdateMessage.getFoodToUpdate(), dietUpdateMessage.getUserId());

    }

}
