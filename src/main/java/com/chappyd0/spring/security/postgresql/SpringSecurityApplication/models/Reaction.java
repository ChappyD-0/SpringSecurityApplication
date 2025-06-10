/*
 * -----------------------------------
 *  Project: SpringSecurityApplication
 *  Author: chappyd-0
 *  Date: 6/3/25
 * -----------------------------------
 */
package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models;
import jakarta.persistence.*;

@Entity
@Table(name = "reactions")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EReaction description;

    public Reaction() {
    }
    public Reaction(EReaction description) {
        this.description = description;
    }
    public EReaction getDescription() {
        return description;
    }

    public void setDescription(EReaction description) {
        this.description = description;
    }

}
