package com.credit.suisse.event.processor.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventData {

    private String id;
    private String state;
    private Long timestamp;
    private String type;
    private String host;

}
