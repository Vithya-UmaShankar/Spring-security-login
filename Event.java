package com.example.login.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table
@Entity
@Getter
@Setter
public class Event extends BaseModel implements Serializable {
    @Id
    @Type(type = "uuid-char")
    private UUID id = UUID.randomUUID();

    @Column(name = "is_internal", columnDefinition = "char")
    private String isInternal;

    @Column(name = "object_type", columnDefinition = "varchar(400)")
    private Status objectType;

    @Column(name = "object_profile", columnDefinition = "varchar(400)")
    private Status objectProfile;

    @Column(name = "object_status", columnDefinition = "varchar(400)")
    private Status objectStatus;

    @Column(name = "time_event_start", columnDefinition = "varchar(400)")
    private Status timeEventStart;

    @Column(name = "time_event_end", columnDefinition = "varchar(400)")
    private Status timeEventEnd;

    @Column(name = "is_event_canceled", columnDefinition = "char")
    private String isEventCanceled;

    @Column(name = "is_event_completed", columnDefinition = "char")
    private String isEventCompleted;

    @JsonBackReference
    @ManyToOne(cascade=CascadeType.REMOVE)
    @JoinColumn(name="parent_event")
    private Event parentEvent;

    @OneToMany(mappedBy="parentEvent")
    private Set<Event> childEvents = new HashSet<Event>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private UserGroup groupId;

}
