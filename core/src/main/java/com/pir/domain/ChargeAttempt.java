package com.pir.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="chargeAttempt")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChargeAttempt extends AbstractPrimaryObject {

	private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private Long paidBy;

    @Column(nullable = false)
    private Long groupId;

    @Column(nullable = false, length = 200)
    private String cardName;

    @Column(nullable = true, columnDefinition = "text")
    private String tokenId;

    @Column(nullable = true, columnDefinition = "text")
    private String chargeId;

    @Column(nullable = true)
    private boolean chargePaid;

    @Column(nullable = true, columnDefinition = "text")
    private String chargeDescription;

    @Column(nullable = true, columnDefinition = "text")
    private String failureCode;

    @Column(nullable = true, columnDefinition = "text")
    private String failureMessage;

    @Column(nullable = true, columnDefinition = "text")
    private String genericExceptionMessage;


    public ChargeAttempt(Long paidBy, Long groupId, String cardName, String tokenId, String chargeId, Boolean chargePaid, String chargeDescription, String failureCode, String failureMessage, String genericExceptionMessage) {

        this.paidBy = paidBy;
        this.groupId = groupId;
        this.cardName = cardName;
        this.tokenId = tokenId;
        this.chargeId = chargeId;
        this.chargePaid = chargePaid;
        this.chargeDescription = chargeDescription;
        this.failureCode = failureCode;
        this.failureMessage = failureMessage;
        this.genericExceptionMessage = genericExceptionMessage;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Long paidBy) {
        this.paidBy = paidBy;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public boolean isChargePaid() {
        return chargePaid;
    }

    public void setChargePaid(boolean chargePaid) {
        this.chargePaid = chargePaid;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeDescription() {
        return chargeDescription;
    }

    public void setChargeDescription(String chargeDescription) {
        this.chargeDescription = chargeDescription;
    }

    public String getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(String failureCode) {
        this.failureCode = failureCode;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public String getGenericExceptionMessage() {
        return genericExceptionMessage;
    }

    public void setGenericExceptionMessage(String genericExceptionMessage) {
        this.genericExceptionMessage = genericExceptionMessage;
    }
}
