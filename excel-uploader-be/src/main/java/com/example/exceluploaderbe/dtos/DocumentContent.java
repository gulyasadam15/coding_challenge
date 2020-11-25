package com.example.exceluploaderbe.dtos;

import java.util.List;

import lombok.Data;

@Data
public class DocumentContent{
     
    private List<DocumentRow> document;
/*
Passenger Class","Name","Sex","Age","No of Siblings or Spouses on Board","No of Parents or Children on Board","Ticket Number",
"Passenger Fare","Cabin","Port of Embarkation","Life Boat","Survived
First","Allen, Miss. Elisabeth Walton","Female","29.0",".0",".0","24160","211.3","B5","Southampton","2","Yes

*/
    @Data
    public static class DocumentRow{
        private String passengerClass;
        private String name;
        private String sex;
        private Integer age;
        private Integer noOfSiblingsOrSpousesOnBoard;
        private Integer noOfParentsOrChildrenOnBoard;
        private String ticketNumber;
        private Long passengerFare;
        private String cabin;
        private String portOfEmbarkation;
        private String lifeBoat;
        private String survived;
    }
}